package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.NinelivesConfigProperties;
import com.ninelives.insurance.api.dto.ChargeDto;
import com.ninelives.insurance.api.dto.ChargeResponseDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.model.PaymentChargeLog;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyPayment;
import com.ninelives.insurance.api.mybatis.mapper.PaymentChargeLogMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyPaymentMapper;
import com.ninelives.insurance.api.provider.payment.PaymentProvider;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.PaymentChargeStatus;
import com.ninelives.insurance.api.ref.PaymentStatus;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
			
	@Autowired NinelivesConfigProperties config;	
	@Autowired PaymentProvider paymentProvider;
	@Autowired OrderService orderService;
	
	@Autowired PolicyPaymentMapper policyPaymentMapper;
	@Autowired PaymentChargeLogMapper paymentChargeLogMapper;
	
	
	public ChargeResponseDto charge(final String userId, final ChargeDto chargeDto) throws ApiException{
		//v send reqeust to payment provider
		//x log or get unique sequence id for payment? and payment provider log this unique sequence? or let this responsibility of payment provider?
		//x update order table?
		//v insert/update log also
		//v test
		//v insert payment table (+token response), kalo belum ada entry berarti belom ada success charge toh
		//v test
		//v retrieve order + payment obj
		//v if payment object is not exists, then insert else update
		//v test valid order trus valid  trus invalid trus valid 
		//v test invalid order trus order valid
		//? hit midtrans to check latest status?
		//v kalo latest payment status is not charge dan charge result in error, then dont update policy_payment?
		//    the thing is we want to know the latest payment status?
		//    incase of pending-fail, then we allow charge and then error 'cannot reuse id' then the latest status charging will be error
		//check that chargedto is valid (no null pointer)
		//validate that grossamount is same
		//validate order is valid in db
		//validate order is submitted
		//validate order is not overdue
		//validate order is ??? (any else?) policystart date other field?, check the possible status, check the property
		//check or validate payment info
		//check the coverage claim limit, check also other limit in orderservice (since we might need to prevent charge after free insurance cases)
		//rethink whether to update order table
		
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();

		logger.debug("Charge user <{}> with charge <{}>", userId, chargeDto);
		
		if(chargeDto==null || chargeDto.getTransactionDetails()==null || StringUtils.isEmpty(chargeDto.getTransactionDetails().getOrderId())){
			logger.debug("Process charge for user <{}> and charge <{}> result: empty chargeDto or transaction details", userId,	chargeDto);
			throw new ApiBadRequestException(ErrorCode.ERR8002_CHARGE_INVALID,
					"Permintaan tidak dapat diproses, data pembayaran tidak ditemukan");
		}
		
		PolicyOrder order = orderService.fetchOrderByOrderId(userId, chargeDto.getTransactionDetails().getOrderId());
		
		if(order==null){
			logger.debug("Process charge for user <{}> and charge <{}> result: empty chargeDto or transaction details", userId,	chargeDto);
			throw new ApiBadRequestException(ErrorCode.ERR8003_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		
		
		PolicyPayment payment = order.getPayment();
		if(payment==null){
			payment = new PolicyPayment();
			payment.setId(generatePolicyPaymentId());
			payment.setOrderId(chargeDto.getTransactionDetails().getOrderId());
			payment.setUserId(userId);
			payment.setStartTime(now);
			payment.setChargeTime(now);
			payment.setTotalAmount(chargeDto.getTransactionDetails().getGrossAmount());		
			payment.setPaymentSeq(1);
		}else{
			payment.setChargeTime(now);
			payment.setPaymentSeq(payment.getPaymentSeq()+1);
		}

		ChargeDto midtransChargeDto = new ChargeDto();
		midtransChargeDto.setUserId(userId);
		midtransChargeDto.setCreditCard(chargeDto.getCreditCard());
		midtransChargeDto.setCustomerDetails(chargeDto.getCustomerDetails());
		midtransChargeDto.setItemDetails(chargeDto.getItemDetails());
		midtransChargeDto.setTransactionDetails(chargeDto.getTransactionDetails());
		midtransChargeDto.setPaymentSeq(String.valueOf(payment.getPaymentSeq()));
		midtransChargeDto.setExpiry(new ChargeDto.Expiry());
		midtransChargeDto.getExpiry().setDuration(String.valueOf(config.getPayment().getMidtransPaymentExpiryDuration()));
		midtransChargeDto.getExpiry().setUnit(config.getPayment().getMidtransPaymentExpiryUnit());
		
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
			if(order.getPayment()==null){
				policyPaymentMapper.insertForStatusCharge(payment);
			}else{
				policyPaymentMapper.updateChargeResponseById(payment);
			}
		}
		
		if(!PaymentStatus.CHARGE.equals(payment.getStatus())){	
			throw new ApiInternalServerErrorException(ErrorCode.ERR8001_CHARGE_API_ERROR, "Maaf, permintaan Anda belum dapat diproses");
		}
		
		return chargeResponseDto;
	}
	
	private String generatePolicyPaymentId(){
		return UUID.randomUUID().toString();
	}
}
