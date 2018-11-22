package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
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

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.InsurerOrderConfirmLogMapper;
import com.ninelives.insurance.core.mybatis.mapper.InsurerOrderLogMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.InsurerOrderConfirmLog;
import com.ninelives.insurance.model.InsurerOrderLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderFamily;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmRequestDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderRequestDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.ref.PackageType;
import com.ninelives.insurance.provider.insurance.aswata.ref.ProductCode;
import com.ninelives.insurance.provider.insurance.aswata.ref.ServiceCode;
import com.ninelives.insurance.provider.insurance.aswata.ref.TravelType;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.CoverageOptionId;
import com.ninelives.insurance.ref.VoucherType;
import com.ninelives.insurance.util.ValidationUtil;

@Service
public class AswataInsuranceProvider implements InsuranceProvider{	
	private static final Logger logger = LoggerFactory.getLogger(InsuranceProvider.class);
	
	@Autowired NinelivesConfigProperties config;
	@Autowired StorageProvider storageProvider;
	@Autowired ProductService productService;
	@Autowired FileUploadService fileUploadService;
	
	@Autowired InsurerOrderLogMapper insurerOrderLogMapper;
	@Autowired InsurerOrderConfirmLogMapper insurerOrderConfirmLogMapper;

//	public static final class AswataResultSuccessCondition{
//		public static final String responseCode="000000";
//		public static final int httpStatus=200;
//	}
	
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	private Boolean enableConnection;
	
	private RestTemplate template;
	private String providerUrl;	
	private String clientCode;
	private String clientKey;	
	private final String coverageSeparator = "|";
	
	@Autowired
	public AswataInsuranceProvider(NinelivesConfigProperties config){
		this.config = config;
	}
	
	@Override
	public ResponseDto<OrderResponseDto> orderPolicy(PolicyOrder order) throws IOException, StorageException, InsuranceProviderException{
		
		if(!enableConnection){
			logger.error("Error on orderPolicy with exception <connection is not enabled>");
			throw new InsuranceProviderConnectDisabledException("Connection is not enabled");
		}

		ResponseDto<OrderResponseDto> result = new ResponseDto<>();
		
		OrderRequestDto requestDto = new OrderRequestDto();
		requestDto.setServiceCode(ServiceCode.POLICY_ORDER);
		requestDto.setUserRefNo(order.getUserId());
		requestDto.setClientCode(clientCode);
		requestDto.setRequestTime(order.getOrderTime().format(timeFormatter));
		
		requestDto.setRequestParam(new OrderRequestDto.RequestParam());
		
		
		
		requestDto.getRequestParam().setInsuredName(StringUtils.abbreviate(order.getPolicyOrderUsers().getName(), 50));
		requestDto.getRequestParam().setDateOfBirth(order.getPolicyOrderUsers().getBirthDate().format(dateFormatter));
		requestDto.getRequestParam().setGender(order.getPolicyOrderUsers().getGender().toStr());		
		requestDto.getRequestParam().setInsuranceStartDate(order.getPolicyStartDate().format(dateFormatter));
		requestDto.getRequestParam().setInsuranceEndDate(order.getPolicyEndDate().format(dateFormatter));
		StringJoiner joiner = new StringJoiner(coverageSeparator);
		for(PolicyOrderProduct p: order.getPolicyOrderProducts()){
			joiner.add(productService.fetchCoverageByCoverageId(p.getCoverageId()).getProviderCode());
		}
		requestDto.getRequestParam().setCoverages(joiner.toString());			
		requestDto.getRequestParam().setPremium(order.getTotalPremi());
		requestDto.getRequestParam().setMobileNumber(ValidationUtil.toAswataPhoneNumber(order.getPolicyOrderUsers().getPhone()));
		requestDto.getRequestParam().setEmailAddress(order.getPolicyOrderUsers().getEmail());
		
		/* Insured address is hardcoded into empty string */
		requestDto.getRequestParam().setInsuredAddress("-");				
		
		
		/*
		 * Travel insurance specific
		 */
		if(order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)
				|| order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_DOMESTIC)){
			requestDto.getRequestParam().setProductCode(ProductCode.TRAVEL);
			requestDto.getRequestParam().setPackageType(PackageType.TYPE_TRAVEL);
			requestDto.getRequestParam().setTravelType(getProviderTravelType(order));
			requestDto.getRequestParam().setIsFamily((order.getIsFamily()?"Y":"N"));
			if(order.getIsFamily() && !CollectionUtils.isEmpty(order.getPolicyOrderFamilies())){
				List<OrderRequestDto.RequestParamFamily> familyList = new ArrayList<>();  
				for(PolicyOrderFamily pof: order.getPolicyOrderFamilies()){
					OrderRequestDto.RequestParamFamily family = new OrderRequestDto.RequestParamFamily();
					family.setFamilyName(pof.getName());
					family.setDateOfBirth(pof.getBirthDate().format(dateFormatter));
					family.setRelation(pof.getRelationship().toStr());
					family.setIdCardNumber("-");
					family.setBeneficiary("-");
					familyList.add(family);
				}
				requestDto.getRequestParam().setFamilyList(familyList);
			}
		}else{
			requestDto.getRequestParam().setProductCode(ProductCode.PA);
			if (order.getPolicyOrderVoucher() != null && order.getPolicyOrderVoucher().getVoucher() != null
					&& VoucherType.B2B.equals(order.getPolicyOrderVoucher().getVoucher().getVoucherType())) {
				requestDto.getRequestParam().setPackageType(PackageType.TYPE_PA_B2B);
				if (order.getPolicyOrderVoucher().getVoucher().getCorporateClient() != null 
						&& !StringUtils.isEmpty(
						order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientProviderId())) {
					requestDto.getRequestParam().setClientId(
							order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientProviderId());
				}			
			}else{				
				requestDto.getRequestParam().setPackageType(PackageType.TYPE_PA_NORMAL);
			}			
		}
		
		String authCode=requestDto.getServiceCode()+requestDto.getUserRefNo()+requestDto.getRequestTime()+requestDto.getClientCode()+clientKey;		
		requestDto.setAuthCode(DigestUtils.sha256Hex(authCode));
		
		/* ID card, replace with passport incase of international travel */
		Long idCardFileId = order.getPolicyOrderUsers().getIdCardFileId();
		if(order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)){
			idCardFileId = order.getPolicyOrderUsers().getPassportFileId();
		}
		
		if(idCardFileId!=null){
			UserFile userFile = fileUploadService.fetchUserFileById(idCardFileId);
			
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
		}else{
			/* if there is no image, use the ktp number instead
			 * no support for passport card number yet
			 */
			if(!order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)){
				requestDto.getRequestParam().setIdCardNumber(order.getPolicyOrderUsers().getIdCardNo());
			}
			
		}
		
		logger.debug("Send to aswata with request <{}>", requestDto);
		
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
		
		InsurerOrderLog orderLog = new InsurerOrderLog();
		orderLog.setCoverageCategoryId(order.getCoverageCategoryId());
		orderLog.setServiceCode(requestDto.getServiceCode());
		orderLog.setUserRefNo(requestDto.getUserRefNo());
		orderLog.setProviderCoverage(requestDto.getRequestParam().getCoverages());
		orderLog.setPremi(requestDto.getRequestParam().getPremium());
		orderLog.setProviderRequestTime(requestDto.getRequestTime());
		orderLog.setRequestTime(requestTime);
		orderLog.setResponseTime(responseTime);
		orderLog.setOrderTime(order.getOrderTime());
		orderLog.setPackageType(requestDto.getRequestParam().getPackageType());
		orderLog.setProductCode(requestDto.getRequestParam().getProductCode());
		orderLog.setTravelType(requestDto.getRequestParam().getTravelType());
		
		if(order.getIsFamily()!=null && order.getIsFamily()){
			orderLog.setIsFamily(requestDto.getRequestParam().getIsFamily());
			orderLog.setOtherProperties("{ familyList="+ requestDto.getRequestParam().getFamilyList().toString()+"}");
		}
		
		if(result!=null){
			orderLog.setResponseStatus(result.getHttpStatus());
			if(result.getResponse()!=null){
				orderLog.setResponseCode(result.getResponse().getResponseCode());		
				orderLog.setProviderResponseTime(result.getResponse().getResponseTime());
				if(result.getResponse().getResponseParam()!=null){
					orderLog.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
					orderLog.setOrderNumber(result.getResponse().getResponseParam().getOrderNumber());
					orderLog.setDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());			
				}
			}
		}
		
		if(result!=null && result.isSuccess()){
			//only set order id on success case since we will not create order if request to aswata fail
			orderLog.setOrderId(order.getOrderId());
		}
		
		insurerOrderLogMapper.insertSelective(orderLog);
		
		logger.debug("Receive aswata response with request: <{}>, entity: <{}> and result <{}>", requestDto, resp == null ? null : resp.toString(),
				result == null ? null : result);		

		return result;
	}
	
	public ResponseDto<OrderConfirmResponseDto> orderConfirm(PolicyOrder order) throws InsuranceProviderException{
		if(!enableConnection){
			logger.error("Error on orderPolicy with exception <connection is not enabled>");
			throw new InsuranceProviderException ("Connection is not enabled");
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		ResponseDto<OrderConfirmResponseDto> result = new ResponseDto<>();
		
		OrderConfirmRequestDto requestDto = new OrderConfirmRequestDto();
		requestDto.setServiceCode(ServiceCode.ORDER_CONFIRM);
		requestDto.setUserRefNo(order.getUserId());
		requestDto.setClientCode(clientCode);
		requestDto.setRequestTime(now.format(timeFormatter));
		
		requestDto.setRequestParam(new OrderConfirmRequestDto.RequestParam());
		requestDto.getRequestParam().setOrderNumber(order.getProviderOrderNumber());
		requestDto.getRequestParam().setPaymentToken("0");
		requestDto.getRequestParam().setPaymentAmount("0");
		
		String authCode=requestDto.getServiceCode()+requestDto.getUserRefNo()+requestDto.getRequestTime()+requestDto.getClientCode()+clientKey;		
		requestDto.setAuthCode(DigestUtils.sha256Hex(authCode));
				
		logger.debug("Sending to aswata with request <{}>", requestDto);
		
		HttpHeaders restHeader = new HttpHeaders();
		restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restHeader.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<OrderConfirmRequestDto> entity = new HttpEntity<>(requestDto, restHeader);
		ResponseEntity<OrderConfirmResponseDto> resp = null;
		LocalDateTime requestTime = LocalDateTime.now();
		try{
			resp  = template.exchange(providerUrl, HttpMethod.POST, entity, OrderConfirmResponseDto.class);
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
		
		InsurerOrderConfirmLog insurerLog = new InsurerOrderConfirmLog();
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
				
		insurerOrderConfirmLogMapper.insertSelective(insurerLog);
		
		logger.debug("Receive aswata response with request: <{}>, entity: <{}> and result <{}>", requestDto, resp == null ? null : resp.toString(),
				result == null ? null : result);	
		
		return result;
	}
	
//	@Override
//	public boolean isSuccess(ResponseDto<OrderResponseDto> responseResult){
//		if(responseResult!=null && responseResult.getResponse()!=null){
//			if(responseResult.getHttpStatus()==AswataResultSuccessCondition.httpStatus
//					&& responseResult.getResponse().getResponseCode()!=null
//					&& responseResult.getResponse().getResponseCode().equals(AswataResultSuccessCondition.responseCode)
//					&& responseResult.getResponse().getResponseParam()!=null
//					){
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean isSuccess(ResponseDto<IAswataResponsePayload> responseResult){
//		if(responseResult!=null && responseResult.getResponse()!=null){
//			if(responseResult.getHttpStatus()==AswataResultSuccessCondition.httpStatus
//					&& responseResult.getResponse().getResponseCode()!=null
//					&& responseResult.getResponse().getResponseCode().equals(AswataResultSuccessCondition.responseCode)
//					&& responseResult.getResponse().getResponseParam()!=null
//					){
//				return true;
//			}
//		}
//		return false;
//	}

	
	public Integer getProviderTravelType(PolicyOrder order){
		Integer result = null;
		if(order!=null){
			if(CoverageCategoryId.TRAVEL_DOMESTIC.equals(order.getCoverageCategoryId())){
				result = TravelType.TYPE_DOMESTIC;
			}else if(CoverageCategoryId.TRAVEL_INTERNATIONAL.equals(order.getCoverageCategoryId())){
				//check if any coverage has option, if they do, use that option, otherwise use international
				for(PolicyOrderProduct p: order.getPolicyOrderProducts()){
					Coverage coverage = productService.fetchCoverageByCoverageId(p.getCoverageId());
					if (coverage.getCoverageOptionId()!=null){
						if(CoverageOptionId.OPTION_INTERNATIONAL.equals(coverage.getCoverageOptionId())){
							result = TravelType.TYPE_WORLDWIDE;
						}else if(CoverageOptionId.OPTION_ASEAN.equals(coverage.getCoverageOptionId())){
							result = TravelType.TYPE_ASIA;
						}else if(CoverageOptionId.OPTION_EU_AU_NZ.equals(coverage.getCoverageOptionId())){
							result = TravelType.TYPE_EU_AUS_NZ;
						}
						break;
					}
				}
				
				if(result == null){
					//for international coverage without specific destination option, use worldwide
					result =  TravelType.TYPE_WORLDWIDE;
				}
			}
		}
				
		return result;
	}
	
	@PostConstruct
	private void init() throws IOException {
		enableConnection = config.getInsurance().getEnableConnection();
		
		if(enableConnection){
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
		}
				
		providerUrl = config.getInsurance().getAswataUrl();
		clientCode = config.getInsurance().getAswataClientCode();
		clientKey = config.getInsurance().getAswataClientKey();					
	}
		
}
