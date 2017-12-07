package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.NinelivesConfigProperties;
import com.ninelives.insurance.api.dto.ChargeDto;
import com.ninelives.insurance.api.dto.ChargeResponseDto;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.model.PolicyPayment;
import com.ninelives.insurance.api.mybatis.mapper.PolicyPaymentMapper;
import com.ninelives.insurance.api.provider.payment.PaymentProvider;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.PaymentStatus;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
			
	@Autowired PaymentProvider paymentProvider;
	@Autowired NinelivesConfigProperties config;
	
	@Autowired PolicyPaymentMapper policyPaymentMapper;
	
	public ChargeResponseDto charge(String userId, ChargeDto chargeDto) throws ApiException{
		//v send reqeust to payment provider
		//x log or get unique sequence id for payment? and payment provider log this unique sequence? or let this responsibility of payment provider?
		//v update payment table (+token response)
		//update order table?
		//validate order is valid in db
		//validate order is submitted
		//validate order is not overdue
		
		LocalDate today = LocalDate.now();

		logger.debug("Charge user {} with charge {}", userId, chargeDto);
		
		PolicyPayment payment = new PolicyPayment();
		payment.setOrderId(chargeDto.getTransactionDetails().getOrderId());
		payment.setUserId(userId);
		payment.setChargeDate(today);
		payment.setTotalAmount(chargeDto.getTransactionDetails().getGrossAmount());		
		
		policyPaymentMapper.insertCharge(payment);
		
		ChargeResponseDto chargeResponseDto = paymentProvider.charge(chargeDto);
		
		if(chargeResponseDto == null){
			payment.setStatus(PaymentStatus.ERR001);
		}else if(StringUtils.isEmpty(chargeResponseDto.getToken())){
			payment.setStatus(PaymentStatus.ERR002);
			logger.error("Response error charge user {} with charge {} and error {}", userId, chargeDto, chargeResponseDto.getErrorMessages());
		}else{
			payment.setStatus(PaymentStatus.CHARGE);
			payment.setProviderPaymentToken(chargeResponseDto.getToken());						
		}
		payment.setChargeResponseDate(LocalDateTime.now());
		
		policyPaymentMapper.updateChargeByPaymentId(payment);
		
		if(payment.getStatus().equals(PaymentStatus.CHARGE)){
			//update order table
		}else {
			throw new ApiInternalServerErrorException(ErrorCode.ERR8001_PAYMENT_CHARGE_ERROR, "Maaf, permintaan Anda belum dapat diproses");
		}
		
		return chargeResponseDto;
	}
}
