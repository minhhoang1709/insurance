package com.ninelives.insurance.core.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.mybatis.mapper.PaymentChargeLogMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyPaymentMapper;
import com.ninelives.insurance.core.provider.payment.PaymentProvider;
import com.ninelives.insurance.model.PaymentChargeLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeDto;
import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeResponseDto;
import com.ninelives.insurance.provider.payment.midtrans.ref.MidtransDurationUnit;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.PaymentChargeStatus;
import com.ninelives.insurance.ref.PaymentStatus;
import com.ninelives.insurance.ref.PolicyStatus;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
			
	@Autowired NinelivesConfigProperties config;	
	@Autowired PaymentProvider paymentProvider;
	@Autowired OrderService orderService;
	
	@Autowired PolicyPaymentMapper policyPaymentMapper;
	@Autowired PaymentChargeLogMapper paymentChargeLogMapper;
	
	private List<String> tier1PaymentChannels = Arrays.asList(new String[]{"credit_card", "echannel", "gopay"}); //payment channel for specific price range
	private String creditCardProcessingBank = "bni";
	
	public ChargeResponseDto charge(final String userId, final ChargeDto chargeDto) throws AppException{
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		logger.info("Charge userId:<{}>, chargeDto:<{}>", userId, chargeDto);
		
		if(chargeDto==null || chargeDto.getTransactionDetails()==null || StringUtils.isEmpty(chargeDto.getTransactionDetails().getOrderId())){
			logger.debug("Process charge for user <{}> and charge <{}> result: empty chargeDto or transaction details", userId,	chargeDto);
			throw new AppBadRequestException(ErrorCode.ERR8002_CHARGE_INVALID,
					"Permintaan tidak dapat diproses, data pembayaran tidak ditemukan");
		}
		
		PolicyOrder order = orderService.fetchOrderByOrderId(userId, chargeDto.getTransactionDetails().getOrderId());
		
		if(order==null){
			logger.debug("Process charge for user <{}> and charge <{}> result: order not found", userId,	chargeDto);
			throw new AppBadRequestException(ErrorCode.ERR8003_CHARGE_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		
		if(!PolicyStatus.SUBMITTED.equals(order.getStatus())){
			logger.debug("Process charge for user <{}> and charge <{}> result: order not in submitted state, orderStatus:<{}>", userId, chargeDto, order.getStatus());
			throw new AppBadRequestException(ErrorCode.ERR8004_CHARGE_ORDER_NOT_VALID,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		
		if(!order.getTotalPremi().equals(chargeDto.getTransactionDetails().getGrossAmount())){
			logger.debug("Process charge for user <{}> and charge <{}> result: premi not match order <{}> ", userId, chargeDto, order.getTotalPremi());
			throw new AppBadRequestException(ErrorCode.ERR8005_CHARGE_PREMI_NOT_MATCH,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		
		LocalDateTime chargeExpiryTime = calculateExpiryDateTime(now, config.getPayment().getMidtransPaymentExpiryUnit(), config.getPayment().getMidtransPaymentExpiryDuration());
		PolicyPayment payment = order.getPayment();
		boolean isFirstPayment = false; 
		if(payment==null){
			isFirstPayment = true;
			payment = new PolicyPayment();
			payment.setId(generatePolicyPaymentId());
			payment.setOrderId(chargeDto.getTransactionDetails().getOrderId());
			payment.setUserId(userId);
			payment.setStartTime(now);
			payment.setChargeTime(now);
			payment.setChargeExpiryTime(chargeExpiryTime);
			payment.setTotalAmount(chargeDto.getTransactionDetails().getGrossAmount());		
			payment.setPaymentSeq(1);
		}else{
			payment.setChargeTime(now);
			payment.setChargeExpiryTime(chargeExpiryTime);
			payment.setPaymentSeq(payment.getPaymentSeq()+1);
		}

		ChargeDto midtransChargeDto = new ChargeDto();
		midtransChargeDto.setUserId(userId);
		//midtransChargeDto.setCreditCard(chargeDto.getCreditCard());
		midtransChargeDto.setCreditCard(new ChargeDto.CreditCard());
		midtransChargeDto.getCreditCard().setBank(creditCardProcessingBank);
		midtransChargeDto.getCreditCard().setSaveCard(false);
		midtransChargeDto.getCreditCard().setSecure(true);
		midtransChargeDto.setCustomerDetails(chargeDto.getCustomerDetails());
		midtransChargeDto.setItemDetails(chargeDto.getItemDetails());
		midtransChargeDto.setTransactionDetails(chargeDto.getTransactionDetails());
		midtransChargeDto.setPaymentSeq(String.valueOf(payment.getPaymentSeq()));
		midtransChargeDto.setExpiry(new ChargeDto.Expiry());
		midtransChargeDto.getExpiry().setDuration(String.valueOf(config.getPayment().getMidtransPaymentExpiryDuration()));
		midtransChargeDto.getExpiry().setUnit(config.getPayment().getMidtransPaymentExpiryUnit().toString());
		
		if (chargeDto.getTransactionDetails().getGrossAmount() >= 5000
				&& chargeDto.getTransactionDetails().getGrossAmount() < 10000) {
			midtransChargeDto.setEnabledPayments(tier1PaymentChannels);
		}
		
		if(midtransChargeDto.getCustomerDetails()!=null && midtransChargeDto.getCustomerDetails().getBillingAddress()!=null){
			midtransChargeDto.getCustomerDetails().getBillingAddress().setCountryCode("IDN");
		}
		if(midtransChargeDto.getCustomerDetails()!=null && midtransChargeDto.getCustomerDetails().getShippingAddress()!=null){
			midtransChargeDto.getCustomerDetails().getShippingAddress().setCountryCode("IDN");
		}
		
		PaymentChargeLog chargeLog = new PaymentChargeLog();
		chargeLog.setChargeDate(today);
		chargeLog.setPolicyPaymentId(payment.getId());
		chargeLog.setPaymentSeq(payment.getPaymentSeq());
		chargeLog.setOrderId(payment.getOrderId());		
		chargeLog.setUserId(payment.getUserId());
		chargeLog.setTotalAmount(payment.getTotalAmount());
		
		paymentChargeLogMapper.insert(chargeLog);
		
		ChargeResponseDto chargeResponseDto = paymentProvider.charge(midtransChargeDto);
		
		if(chargeResponseDto == null){
			chargeLog.setStatus(PaymentChargeStatus.ERRSYS);
		}else{
			chargeLog.setResponseHttpStatus(chargeResponseDto.getHttpStatus().value() +"");
			if(!StringUtils.isEmpty(chargeResponseDto.getToken())){
				chargeLog.setStatus(PaymentChargeStatus.CHARGE);
				chargeLog.setProviderTransactionId(chargeResponseDto.getToken());				
			}else{				
				chargeLog.setStatus(PaymentChargeStatus.ERRMID);
				if(!CollectionUtils.isEmpty(chargeResponseDto.getErrorMessages())){
					chargeLog.setResponseErrorMessage(chargeResponseDto.getErrorMessages().toString());
				}
				logger.error("Response error charge user <{}> with charge <{}> and error <{}>", userId, midtransChargeDto, chargeResponseDto.getErrorMessages());
			}
		}
		chargeLog.setResponseDate(LocalDateTime.now());
		
		paymentChargeLogMapper.updateChargeResponseById(chargeLog);
		
		if(chargeResponseDto!=null && !StringUtils.isEmpty(chargeResponseDto.getToken())){
			payment.setStatus(PaymentStatus.CHARGE);
			payment.setProviderTransactionId(chargeResponseDto.getToken());
			if(isFirstPayment){
				policyPaymentMapper.insertForStatusCharge(payment);
			}else{
				policyPaymentMapper.updateChargeResponseById(payment);
			}
		}
		
		if(!PaymentStatus.CHARGE.equals(payment.getStatus())){	
			throw new AppInternalServerErrorException(ErrorCode.ERR8001_CHARGE_API_ERROR, "Maaf, permintaan Anda belum dapat diproses");
		}
		
		return chargeResponseDto;
	}
	protected LocalDateTime calculateExpiryDateTime(LocalDateTime localDateTime, MidtransDurationUnit periodUnit, long periodValue) {
		LocalDateTime calculatedDateTime = null;
		if(periodUnit!=null){
			if(MidtransDurationUnit.HOURS.equals(periodUnit)){
				calculatedDateTime = localDateTime.plusHours(periodValue);
			}
		}
		return calculatedDateTime;
	}
	private String generatePolicyPaymentId(){
		return UUID.randomUUID().toString();
	}
}
