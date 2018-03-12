package com.ninelives.insurance.core.provider.payment;

import java.util.Base64;
import java.util.Collections;

import javax.annotation.PostConstruct;

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
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.util.GsonUtil;
import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeDto;
import com.ninelives.insurance.provider.payment.midtrans.dto.ChargeResponseDto;

@Service
public class MidtransPaymentProvider implements PaymentProvider{	
	private static final Logger logger = LoggerFactory.getLogger(MidtransPaymentProvider.class);
	
	private static final class Environment{
		static final String SANDBOX = "sandbox";
		static final String PRODUCTION = "production";
	}
	
	NinelivesConfigProperties.Payment config;
	
	private RestTemplate template;
	private String midtransEnvironment;
	private String midtransUrl;
	private String midtransServerAuthorization;
	
	@Autowired
	public MidtransPaymentProvider(NinelivesConfigProperties config){
		this.config = config.getPayment();
	}
	
	public ChargeResponseDto charge(ChargeDto chargeDto){
		ChargeResponseDto chargeResponseDto = null;
		ResponseEntity<String> resp = null;
		try {
			logger.debug("Sending charge to <{}> with data <{}>", midtransUrl, GsonUtil.gsonUnderscore.toJson(chargeDto, ChargeDto.class));
			
			HttpHeaders restHeader = new HttpHeaders();
			restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			restHeader.setContentType(MediaType.APPLICATION_JSON);
			restHeader.set("Authorization", midtransServerAuthorization);
			
			HttpEntity<ChargeDto> entity = new HttpEntity<>(chargeDto, restHeader);
			
			resp = template.exchange(midtransUrl, HttpMethod.POST, entity, String.class);
			
			logger.debug("Receive charge response with entity {} and data <{}>", resp==null?null:resp.toString(), resp==null?null:resp.getBody());			
			
			if(resp != null){
				chargeResponseDto = GsonUtil.gsonUnderscore.fromJson(resp.getBody(), ChargeResponseDto.class);
				chargeResponseDto.setHttpStatus(resp.getStatusCode());
			}
		}catch(HttpClientErrorException e){
			try {
				String errorResponseBody = e.getResponseBodyAsString();
				
				logger.error("Error on payment provider charge <{}> with response <{}> and exception <{}>", chargeDto, errorResponseBody, e.getMessage());
				
				if(!StringUtils.isEmpty(errorResponseBody) && e.getStatusCode() != null){
					chargeResponseDto = GsonUtil.gsonUnderscore.fromJson(errorResponseBody, ChargeResponseDto.class);
					chargeResponseDto.setHttpStatus(e.getStatusCode());
				}
			} catch (Exception e1) {
				logger.error("Error on payment provider charge <{}> with exception <{}>", chargeDto, e1.getMessage());
				logger.error("Error on payment provider charge", e1);
			}						
		}catch (Exception e){
			logger.error("Error on payment provider charge <{}> with exception <{}>", chargeDto, e.getMessage());
			logger.error("Error on payment provider charge", e);
		}
		
		return chargeResponseDto;
	}

//private ObjectMapper jacksonMapper = new ObjectMapper();	
//	try {
//		logger.debug("Sending charge to <{}> with data <{}>", midtransUrl, jacksonMapper.writeValueAsString(chargeDto));
//	} catch (JsonProcessingException e) {
//		logger.error("Error on convert chargeDto to string", e);
//		throw new ApiInternalServerErrorException(ErrorCode.ERR1001_GENERIC_ERROR,"Generic error");
//	}

	
	@PostConstruct
	public void init(){
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(config.getMidtransConnectionPoolSize());
		cm.setDefaultMaxPerRoute(config.getMidtransConnectionPoolSize());
		
		RequestConfig requestConfig = RequestConfig.custom()
	            .setConnectionRequestTimeout(config.getMidtransPoolTimeout())
	            .setConnectTimeout(config.getMidtransConnectTimeout())
	            .setSocketTimeout(config.getMidtransSocketTimeout())
	            .build();
	
		HttpClient defaultHttpClient = HttpClientBuilder
				.create()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(requestConfig)
				.build();

		template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(defaultHttpClient));
		
		midtransEnvironment = config.getMidtransEnvironment();
		
		if(midtransEnvironment.equals(Environment.PRODUCTION)){			
			midtransUrl = config.getMidtransProductionUrl();
			midtransServerAuthorization = "Basic "
					+ Base64.getEncoder().encodeToString((config.getMidtransProductionServerKey() + ":").getBytes());
		}else if (midtransEnvironment.equals(Environment.SANDBOX)){
			midtransUrl = config.getMidtransSandboxUrl();
			midtransServerAuthorization = "Basic "
					+ Base64.getEncoder().encodeToString((config.getMidtransSandboxServerKey() + ":").getBytes());
		}
		logger.info("Init PaymentProvider with environment <{}> and url <{}>", midtransEnvironment, midtransUrl);
	}
}
