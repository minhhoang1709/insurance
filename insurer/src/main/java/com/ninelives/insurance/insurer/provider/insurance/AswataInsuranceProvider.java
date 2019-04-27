package com.ninelives.insurance.insurer.provider.insurance;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.insurer.NinelivesInsurerConfigProperties;
import com.ninelives.insurance.insurer.mybatis.mapper.InsurerPaymentConfirmLogMapper;
import com.ninelives.insurance.model.InsurerPaymentConfirmLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.PaymentConfirmRequestDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.PaymentConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.ref.ServiceCode;

@Deprecated
public class AswataInsuranceProvider {
	private static final Logger logger = LoggerFactory.getLogger(AswataInsuranceProvider.class);
	
	@Autowired NinelivesInsurerConfigProperties config;
	
	@Autowired InsurerPaymentConfirmLogMapper insurerPaymentConfirmLogMapper;
	
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public static final class AswataResultSuccessCondition{
		public static final String responseCode="000000";
		public static final int httpStatus=200;
	}
	
	private RestTemplate template;
	private String providerUrl;	
	private String clientCode;
	private String clientKey;
	
	public ResponseDto<PaymentConfirmResponseDto> paymentConfirm(PolicyOrder order){
		LocalDateTime now = LocalDateTime.now();
		
		ResponseDto<PaymentConfirmResponseDto> result = new ResponseDto<>();
		
		PaymentConfirmRequestDto requestDto = new PaymentConfirmRequestDto();
		requestDto.setServiceCode(ServiceCode.PAYMENT_CONFIRM);
		requestDto.setUserRefNo(order.getUserId());
		requestDto.setClientCode(clientCode);
		requestDto.setRequestTime(now.format(timeFormatter));
		
		requestDto.setRequestParam(new PaymentConfirmRequestDto.RequestParam());
		requestDto.getRequestParam().setOrderNumber(order.getProviderOrderNumber());
		requestDto.getRequestParam().setPaymentToken(order.getPayment().getProviderTransactionId());
		requestDto.getRequestParam().setPaymentAmount(String.valueOf(order.getTotalPremi()));
		
		String authCode=requestDto.getServiceCode()+requestDto.getUserRefNo()+requestDto.getRequestTime()+requestDto.getClientCode()+clientKey;		
		requestDto.setAuthCode(DigestUtils.sha256Hex(authCode));
				
		logger.debug("Sending to aswata with request <{}>", requestDto);
		
		HttpHeaders restHeader = new HttpHeaders();
		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restHeader.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<PaymentConfirmRequestDto> entity = new HttpEntity<>(requestDto, restHeader);
		ResponseEntity<PaymentConfirmResponseDto> resp = null;
		LocalDateTime requestTime = LocalDateTime.now();
		try{
			resp  = template.exchange(providerUrl, HttpMethod.POST, entity, PaymentConfirmResponseDto.class);
			result.setHttpStatus(resp.getStatusCodeValue());
			result.setResponse(resp.getBody());
		}catch(HttpClientErrorException e){
			result.setHttpStatus(e.getStatusCode().value());
			result.setErrorMessage(e.getMessage());
			logger.error("Error on send order request to aswata", e);
		}catch(Exception e){
			result.setHttpStatus(-1);
			logger.error("Error on send order request to aswata", e);
		}
		LocalDateTime responseTime = LocalDateTime.now();
		
		
		InsurerPaymentConfirmLog insurerLog = new InsurerPaymentConfirmLog();
		insurerLog.setCoverageCategoryId(order.getCoverageCategoryId());
		insurerLog.setServiceCode(requestDto.getServiceCode());
		insurerLog.setOrderId(order.getOrderId());
		insurerLog.setOrderTime(order.getOrderTime());
		insurerLog.setUserRefNo(requestDto.getUserRefNo());
		insurerLog.setProviderRequestTime(requestDto.getRequestTime());
		insurerLog.setRequestTime(requestTime);
		insurerLog.setResponseTime(responseTime);
				
		if(result!=null){
			insurerLog.setResponseStatus(result.getHttpStatus());
			if(result.getResponse()!=null){
				insurerLog.setResponseCode(result.getResponse().getResponseCode());		
				insurerLog.setProviderResponseTime(result.getResponse().getResponseTime());
				if(result.getResponse().getResponseParam()!=null){
					insurerLog.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
					insurerLog.setOrderNumber(result.getResponse().getResponseParam().getOrderNumber());
					insurerLog.setPolicyDocument(result.getResponse().getResponseParam().getPolicyDocument());
					insurerLog.setDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());
				}
			}
		}		
				
		insurerPaymentConfirmLogMapper.insertSelective(insurerLog);
		
		logger.debug("Receive aswata response with request: <{}>, entity: <{}> and result <{}>", requestDto, resp == null ? null : resp.toString(),
				result == null ? null : result);	
		
		return result;
	}
	
	public boolean isSuccess(ResponseDto<PaymentConfirmResponseDto> responseResult){
		if(responseResult!=null){
			return responseResult.isSuccess();
		}
//		if(responseResult!=null && responseResult.getResponse()!=null){
//			if(responseResult.getHttpStatus()==AswataResultSuccessCondition.httpStatus
//					&& responseResult.getResponse().getResponseCode()!=null
//					&& responseResult.getResponse().getResponseCode().equals(AswataResultSuccessCondition.responseCode)
//					&& responseResult.getResponse().getResponseParam()!=null
//					){
//				return true;
//			}
//		}
		return false;
	}
	
	@PostConstruct
	private void init() throws IOException {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(config.getInsurance().getAswataConnectionPoolSize());
		cm.setDefaultMaxPerRoute(config.getInsurance().getAswataConnectionPoolSize());
		
		RequestConfig requestConfig = RequestConfig.custom()
	            .setConnectionRequestTimeout(config.getInsurance().getAswataPoolTimeout())
	            .setConnectTimeout(config.getInsurance().getAswataConnectTimeout())
	            .setSocketTimeout(config.getInsurance().getAswataSocketTimeout())
	            .build();
	
		HttpClient defaultHttpClient = HttpClientBuilder
				.create()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(requestConfig)
				.build();

		template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(defaultHttpClient));
		
		providerUrl = config.getInsurance().getAswataUrl();
		clientCode = config.getInsurance().getAswataClientCode();
		clientKey = config.getInsurance().getAswataClientKey();
		
	}
}
