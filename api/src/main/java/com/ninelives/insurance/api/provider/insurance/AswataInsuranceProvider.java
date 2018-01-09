package com.ninelives.insurance.api.provider.insurance;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.api.NinelivesConfigProperties;
import com.ninelives.insurance.api.mybatis.mapper.ProviderOrderLogMapper;
import com.ninelives.insurance.api.provider.storage.StorageException;
import com.ninelives.insurance.api.provider.storage.StorageProvider;
import com.ninelives.insurance.api.service.FileUploadService;
import com.ninelives.insurance.api.service.ProductService;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.ProviderOrderLog;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderRequestDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;

@Service
public class AswataInsuranceProvider implements InsuranceProvider{	
	private static final Logger logger = LoggerFactory.getLogger(InsuranceProvider.class);
	
	@Autowired NinelivesConfigProperties config;
	@Autowired StorageProvider storageProvider;
	@Autowired ProductService productService;
	@Autowired FileUploadService fileUploadService;
	
	@Autowired ProviderOrderLogMapper providerOrderLogMapper; 
	
	public static class ServiceCode{
		public static final String POLICY_ORDER = "POLICY_ORDER"; 
	}
	
	public static final class AswataResultSuccessCondition{
		public static final String responseCode="000000";
		public static final int httpStatus=200;
	}
	
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	private RestTemplate template;
	private String providerUrl;	
	private String clientCode;
	private String clientKey;
	private String packageType;
	private String productCode;
	private final String coverageSeparator = "|";
	
	@Override
	public ResponseDto<OrderResponseDto> orderPolicy(PolicyOrder order) throws IOException, StorageException{
		
		ResponseDto<OrderResponseDto> result = new ResponseDto<>();
		
		OrderRequestDto requestDto = new OrderRequestDto();
		requestDto.setServiceCode(ServiceCode.POLICY_ORDER);
		requestDto.setUserRefNo(order.getUserId());
		//requestDto.setUserRefNo("test1234");
		requestDto.setClientCode(clientCode);
		requestDto.setRequestTime(order.getOrderTime().format(timeFormatter));
		
		requestDto.setRequestParam(new OrderRequestDto.RequestParam());
		requestDto.getRequestParam().setProductCode(productCode);
		requestDto.getRequestParam().setPackageType(packageType);
		requestDto.getRequestParam().setInsuredName(StringUtils.abbreviate(order.getPolicyOrderUsers().getName(), 50));
		requestDto.getRequestParam().setDateOfBirth(order.getPolicyOrderUsers().getBirthDate().format(dateFormatter));
		requestDto.getRequestParam().setGender(order.getPolicyOrderUsers().getGender().toStr());
		requestDto.getRequestParam().setInsuredAddress(StringUtils.abbreviate(order.getPolicyOrderUsers().getAddress(), 250));
		requestDto.getRequestParam().setInsuranceStartDate(order.getPolicyStartDate().format(dateFormatter));
		requestDto.getRequestParam().setInsuranceEndDate(order.getPolicyEndDate().format(dateFormatter));
		StringJoiner joiner = new StringJoiner(coverageSeparator);
		for(PolicyOrderProduct p: order.getPolicyOrderProducts()){
			joiner.add(productService.fetchCoverageByCoverageId(p.getCoverageId()).getProviderCode());
		}
		requestDto.getRequestParam().setCoverages(joiner.toString());			
		requestDto.getRequestParam().setPremium(order.getTotalPremi());
		requestDto.getRequestParam().setMobileNumber(order.getPolicyOrderUsers().getPhone());
		requestDto.getRequestParam().setEmailAddress(order.getPolicyOrderUsers().getEmail());

		//no beneficiary since its optional and done after payment
		//requestDto.getRequestParam().setBeneficiary("beneficiary");
		//requestDto.getRequestParam().setBeneficiaryRelation("ayah");
		//requestDto.getRequestParam().setIndustry("");		
		
		logger.debug("Sending to aswata with request <{}>", requestDto);
		
		UserFile userFile = fileUploadService.fetchUserFileById(order.getPolicyOrderUsers().getIdCardFileId());
		
		//Path filepath = storageProvider.load("test/scan-ktp.jpg");
		String base64IdCard = null;
		try {
			Resource resource = storageProvider.loadAsResource(userFile);
			base64IdCard = Base64.getEncoder().encodeToString((FileUtils.readFileToByteArray(resource.getFile())));
		} catch (IOException e) {
			logger.error("Exception on fetching id card file", e);
			throw e;
		} catch (StorageException e) {
			logger.error("Exception on fetching id card file", e);
			throw e;
		}
		if(base64IdCard!=null){
			requestDto.getRequestParam().setIdCard(base64IdCard);
		}
		
		String authCode=requestDto.getServiceCode()+requestDto.getUserRefNo()+requestDto.getRequestTime()+requestDto.getClientCode()+clientKey;		
		requestDto.setAuthCode(DigestUtils.sha256Hex(authCode));
				
		HttpHeaders restHeader = new HttpHeaders();
		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restHeader.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<OrderRequestDto> entity = new HttpEntity<>(requestDto, restHeader);
		ResponseEntity<OrderResponseDto> resp = null;
		LocalDateTime requestTime = LocalDateTime.now();
		try{
			resp  = template.exchange(providerUrl, HttpMethod.POST, entity, OrderResponseDto.class);
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
		
		ProviderOrderLog orderLog = new ProviderOrderLog();
		orderLog.setCoverageCategoryId(order.getCoverageCategoryId());
		orderLog.setServiceCode(requestDto.getServiceCode());
		orderLog.setUserRefNo(requestDto.getUserRefNo());
		orderLog.setProviderCoverage(requestDto.getRequestParam().getCoverages());
		orderLog.setPremi(requestDto.getRequestParam().getPremium());
		orderLog.setProviderRequestTime(requestDto.getRequestTime());
		orderLog.setRequestTime(requestTime);
		orderLog.setResponseTime(responseTime);
		orderLog.setOrderTime(order.getOrderTime());
		
		if(result!=null){
			orderLog.setResponseStatus(result.getHttpStatus());
			if(result.getResponse()!=null){
				orderLog.setResponseCode(result.getResponse().getResponseCode());		
				orderLog.setProviderResponseTime(result.getResponse().getResponseTime());
				if(result.getResponse().getResponseParam()!=null){
					orderLog.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
					orderLog.setOrderNumber(result.getResponse().getResponseParam().getOderNumber());
					orderLog.setDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());			
				}
			}
		}
		
		if(isSuccess(result)){
			//only set order id on success case since we will not create order if request to aswata fail
			orderLog.setOrderId(order.getOrderId());
		}
		
		providerOrderLogMapper.insertSelective(orderLog);
		
		logger.debug("Receive aswata response with request: <{}>, entity: <{}> and result <{}>", requestDto, resp == null ? null : resp.toString(),
				result == null ? null : result);		

		return result;
	}	
	@Override
	public boolean isSuccess(ResponseDto<OrderResponseDto> responseResult){
		if(responseResult!=null && responseResult.getResponse()!=null){
			if(responseResult.getHttpStatus()==AswataResultSuccessCondition.httpStatus
					&& responseResult.getResponse().getResponseCode()!=null
					&& responseResult.getResponse().getResponseCode().equals(AswataResultSuccessCondition.responseCode)
					){
				return true;
			}
		}
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
		productCode = config.getInsurance().getAswataProductCode();
		packageType = config.getInsurance().getAswataPackageType();
		
	}
}
