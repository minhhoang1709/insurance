package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import javax.annotation.PostConstruct;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.jackson.transformer.UserFileToBase64JsonSerializer;
import com.ninelives.insurance.core.mybatis.mapper.InsurerMapper;
import com.ninelives.insurance.core.mybatis.mapper.InsurerOrderLogMapper;
import com.ninelives.insurance.core.mybatis.mapper.InsurerPolicyFileMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.InsurerOrderLog;
import com.ninelives.insurance.model.InsurerPolicyFile;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.provider.insurance.aswata.ref.PackageType;
import com.ninelives.insurance.provider.insurance.aswata.ref.ProductCode;
import com.ninelives.insurance.provider.insurance.aswata.ref.ServiceCode;
import com.ninelives.insurance.provider.insurance.pti.dto.PtiOrderRequestDto;
import com.ninelives.insurance.provider.insurance.pti.dto.PtiRequestInfo;
import com.ninelives.insurance.provider.insurance.pti.dto.PtiResponeResultDto;
import com.ninelives.insurance.provider.insurance.pti.dto.PtiResponseDto;
import com.ninelives.insurance.provider.insurance.pti.ref.PtiCoverageCategory;
import com.ninelives.insurance.ref.CoverageCategoryType;
import com.ninelives.insurance.ref.VoucherType;
import com.ninelives.insurance.util.PtiObjectMapper;
import com.ninelives.insurance.util.Sha1EncryptionUtil;

@Service
public class PtiInsuranceProvider implements InsuranceProvider {
	private static final Logger logger = LoggerFactory.getLogger(PtiInsuranceProvider.class);

	private static final String DEFAULT_DOCUMENT_LOCALE = "vi_VN";

	private static final String DEFAULT_TEMPLATE_FONT_APPREANCE = "0.18039 0.20392 0.21176 rg /F8 7.994 Tf";

	@Autowired
	NinelivesConfigProperties config;

	@Autowired
	StorageProvider storageProvider;
	@Autowired
	ProductService productService;
	@Autowired
	TranslationService translationService;
	@Autowired
	InsurerMapper insurerMapper;
	@Autowired
	InsurerPolicyFileMapper insurerFileMapper;
	@Autowired
	Environment env;
	@Autowired
	PtiObjectMapper ptiObjectMapper;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	Sha1EncryptionUtil sha1EncryptionUtil;
	@Autowired
	InsurerOrderLogMapper insurerOrderLogMapper;

//	private boolean isPtiEnabled = true;
	private RestTemplate template;
	private UserFileToBase64JsonSerializer userFileToBase64JsonSerializer;
	private Boolean enableConnection;
	private String providerUrl;
	private String imageDownloadUrl;
	private String secretCode;
	private int serviceCode;
	private final String coverageSeparator = "|";

	PdfCreator pdfCreator;

	int templateCoverageMaxCount;
	String policyFileDir;
	String templateFilePath;
	String templateFontFilePath;
	String templateFontAppearance = DEFAULT_TEMPLATE_FONT_APPREANCE;

	Locale documentLocale;
	DateTimeFormatter dateFormatter;
	DateTimeFormatter policyFileDirDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	NumberFormat numberFormat;

	public PtiInsuranceProvider(NinelivesConfigProperties config) {
		this.config = config;
		this.documentLocale = LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE);
		this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		this.numberFormat = NumberFormat.getInstance(LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE));
	}

	public PtiInsuranceProvider() {
		// TODO Auto-generated constructor stub
	}

	private InsurerPolicyFile generatePolicy(PolicyOrder order)
			throws InsuranceProviderException, IOException, StorageException {
		InsurerPolicyFile policyFile = policyFile(order);

		Map<String, String> fieldMap = new HashMap<>();

		if (order.getPolicyOrderVoucher() != null && order.getPolicyOrderVoucher().getVoucher() != null
				&& VoucherType.B2B.equals(order.getPolicyOrderVoucher().getVoucher().getVoucherType())) {
			fieldMap.put("buyer_name",
					order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientName());
			fieldMap.put("buyer_contact_no",
					order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientPhoneNumber());
		} else {
			fieldMap.put("buyer_name", order.getPolicyOrderUsers().getName());
			fieldMap.put("buyer_contact_no", order.getPolicyOrderUsers().getPhone());
		}

		fieldMap.put("policy_number", policyFile.getPolicyNumber());
		fieldMap.put("date_of_issue", policyFile.getIssuedDate().format(dateFormatter));
		fieldMap.put("insured_name", order.getPolicyOrderUsers().getName());
		fieldMap.put("insured_idcard", order.getPolicyOrderUsers().getIdCardNo());
		fieldMap.put("insured_birthdate", order.getPolicyOrderUsers().getBirthDate().format(dateFormatter));
		fieldMap.put("insured_buyer_relation", "");
		fieldMap.put("policy_start_date", order.getPolicyStartDate().format(dateFormatter));
		fieldMap.put("policy_end_date", order.getPolicyEndDate().format(dateFormatter));

		int i = 1;
		for (PolicyOrderProduct pop : order.getPolicyOrderProducts()) {
			Product product = pop.getProduct();
			if (product == null) {
				product = productService.fetchProductByProductId(pop.getProductId());
			}
			fieldMap.put("coverage_" + i,
					translationService.translateDefaultIfEmpty(product, documentLocale.getLanguage()).getName());
			fieldMap.put("limit_" + i, numberFormat.format(pop.getCoverageMaxLimit()));
			i++;
		}
		for (int j = i; j <= templateCoverageMaxCount; j++) {
			fieldMap.put("coverage_" + j, "");
			fieldMap.put("limit_" + j, "");
		}

		fieldMap.put("premi", numberFormat.format(order.getTotalPremi()));
		// fieldMap.put("period",
		// translationService.translateDefaultIfEmpty(order.getPeriod(),
		// documentLocale.getLanguage()).getName());

		insurerFileMapper.insert(policyFile);

		try {
			Path filepath = storageProvider.prepare(policyFile.getFilePath());
			pdfCreator.printFieldMap(fieldMap, filepath.toString());
		} catch (IOException | StorageException e) {
			logger.error("Error on creating pdf ", e);
			throw e;
		}

		return policyFile;
	}

	@Override
	public OrderResult orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		// if has voucher and voucher is free then generate policy-
		OrderResult result = new OrderResult();

		if (order.getPolicyOrderVoucher() != null && order.getTotalPremi() == 0
				&& (order.getPolicyOrderVoucher().getVoucher().getVoucherType().equals(VoucherType.INVITE)
						|| order.getPolicyOrderVoucher().getVoucher().getVoucherType()
								.equals(VoucherType.FREE_PROMO_NEW_USER)
						|| order.getPolicyOrderVoucher().getVoucher().getVoucherType().equals(VoucherType.B2B2C))) {
			InsurerPolicyFile policyFile = generatePolicy(order);

			if (policyFile != null) {
				result.setSuccess(true);
				result.setPolicyNumber(policyFile.getPolicyNumber());
				result.setProviderDownloadUrl(policyFile.getFilePath());
				orderPolicyInternal(order, policyFile.getPolicyNumber());
			} else {
				result.setSuccess(false);
			}

		} else { // do nothing on order policy since the policy is generated on payment/order
					// confirm for non free
			result.setSuccess(true);
		}

		return result;
	}

	protected PtiResponseDto orderPolicyInternal(PolicyOrder order, String policyNumber)
			throws IOException, StorageException, InsuranceProviderException {

		if (!enableConnection) {
			logger.error("Error on orderPolicy with exception <connection is not enabled>");
			throw new InsuranceProviderConnectDisabledException("Connection is not enabled");
		}

		PtiResponseDto result = new PtiResponseDto();
		PtiOrderRequestDto requestDto = new PtiOrderRequestDto();

		UserFile userFile = fileUploadService.fetchUserFileById(order.getPolicyOrderUsers().getIdCardFileId());
		String base64IdCard = null;
		if(userFile!=null) {
			
			base64IdCard = imageDownloadUrl + Base64.getEncoder().encodeToString(order.getOrderId().getBytes());
		}
		
		PtiRequestInfo requestInfo = ptiObjectMapper.toDto(order, base64IdCard, policyNumber);
		requestDto.setServiceId(serviceCode);
		String productCode = null;
		String packageType = null;

		if (order.getCoverageCategoryId().equals("201")) {
			requestDto.setInsuranceType(4);
			String goiSanPham = "itemPA(" + requestInfo.getGoiSamPham() + ")";
			String info = "{\"refId\":\"" + requestInfo.getRefId() + "\",\"name\":\"" + requestInfo.getName()
					+ "\",\"personId\":\"" + requestInfo.getPersonId() + "\"," + "\"phone\":\"" + requestInfo.getPhone()
					+ "\",\"mail\":\"" + requestInfo.getMail() + "\",\"fromTime\":\"" + requestInfo.getFromTime()
					+ "\",\"toTime\":\"" + requestInfo.getToTime() + "\"," + "\"value\":\"" + requestInfo.getValue()
					+ "\",\"bonus\":0,\"fee\":0,\"ngaySinh\":\"" + requestInfo.getNgaySinh() + "\",\"viaPartner\":\""
					+ requestInfo.getViaPartner() + "\",\"maHopDong\":\"" + requestInfo.getMaHopDong()
					+ "\",\"gioiTinh\":\"" + requestInfo.getGioiTinh() + "\"," + "\"goiSanPham\":\"" + goiSanPham
					+ "\",\"trangThaiHopDong\":\"" + requestInfo.getTrangThaiHopDong()
					+ "\",\"chuKyThanhToan\":\"\",\"loaiThueBao\":\"\"}";
			requestDto.setRequestInfo(info);
			System.out.println(info);
			requestDto.setSha1Hash(sha1EncryptionUtil.sha1Encrypt(Integer.toString(serviceCode), secretCode, "4", info));

			productCode = ProductCode.PA;
			packageType = PackageType.TYPE_PA_NORMAL;
		}

		HttpHeaders restHeader = new HttpHeaders();
		// restHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		restHeader.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<PtiOrderRequestDto> entity = new HttpEntity<>(requestDto, restHeader);
		ResponseEntity<PtiResponeResultDto> resp = null;
		LocalDateTime requestTime = LocalDateTime.now();
		try {
			resp = template.exchange(providerUrl, HttpMethod.POST, entity, PtiResponeResultDto.class);
			result.setHttpStatus(resp.getStatusCodeValue());
			result.setResponseResult(resp.getBody());
			System.out.println(resp.getBody().toString());
		} catch (HttpClientErrorException e) {
			result.setHttpStatus(e.getStatusCode().value());
			result.setErrorMessage(e.getMessage());
			logger.error("Error on send order request to pti", e);
		} catch (Exception e) {
			result.setHttpStatus(-1);
			logger.error("Error on send order request to pti", e);
		}
		LocalDateTime responseTime = LocalDateTime.now();
		InsurerOrderLog orderLog = new InsurerOrderLog();
		orderLog.setCoverageCategoryId(order.getCoverageCategoryId());
		orderLog.setServiceCode(ServiceCode.POLICY_ORDER);
		orderLog.setUserRefNo(order.getUserId());
		orderLog.setOrderId(order.getOrderId());
		StringJoiner joiner = new StringJoiner(coverageSeparator);
		for (PolicyOrderProduct p : order.getPolicyOrderProducts()) {
			joiner.add(productService.fetchCoverageByCoverageId(p.getCoverageId()).getProviderCode());
		}
		orderLog.setProviderCoverage(joiner.toString());
		orderLog.setPremi(order.getTotalPremi());
		orderLog.setProviderRequestTime(order.getOrderTime().format(timeFormatter));
		orderLog.setRequestTime(requestTime);
		orderLog.setResponseTime(responseTime);
		orderLog.setOrderTime(order.getOrderTime());
		orderLog.setPackageType(packageType);
		orderLog.setProductCode(productCode);

		if (result != null) {
			orderLog.setResponseStatus(result.getHttpStatus());
			if (result.getResponseResult() != null) {
				orderLog.setResponseCode(Integer.toString(result.getResponseResult().getResultCode()));
				orderLog.setProviderResponseTime(responseTime.toString());
			}
		}

		if (result != null && result.isSuccess()) {
			// only set order id on success case since we will not create order if request
			// to aswata fail
			orderLog.setOrderId(order.getOrderId());
		}

		insurerOrderLogMapper.insertSelective(orderLog);

		logger.debug("Receive pti response with request: <{}>, entity: <{}> and result <{}>", requestDto,
				resp == null ? null : resp.toString(), result == null ? null : result);
		return result;
	}

	@Override
	public OrderConfirmResult orderConfirm(PolicyOrder order)
			throws InsuranceProviderException, IOException, StorageException {
		OrderConfirmResult result = new OrderConfirmResult();
		InsurerPolicyFile policyFile = generatePolicy(order);

		if (policyFile != null) {
			result.setSuccess(true);
			result.setPolicyNumber(policyFile.getPolicyNumber());
			result.setProviderDownloadUrl(policyFile.getFilePath());
			orderPolicyInternal(order, policyFile.getPolicyNumber());
		} else {
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public PaymentConfirmResult paymentConfirm(PolicyOrder order)
			throws InsuranceProviderException, IOException, StorageException {

		PaymentConfirmResult result = new PaymentConfirmResult();
		InsurerPolicyFile policyFile = generatePolicy(order);
		if (policyFile != null) {
			result.setSuccess(true);
			result.setPolicyNumber(policyFile.getPolicyNumber());
			result.setProviderDownloadUrl(policyFile.getFilePath());
			orderPolicyInternal(order, policyFile.getPolicyNumber());
		} else {
			result.setSuccess(false);
		}

		return result;
	}

	private String policyFilePath(String orderId, String coverageCategoryId, LocalDate issuedDate) {
		Path path = Paths.get(policyFileDir, coverageCategoryId, issuedDate.format(policyFileDirDateFormatter));
		return FilenameUtils.concat(path.toString(), orderId + ".pdf");
	}

	protected InsurerPolicyFile policyFile(PolicyOrder order) throws InsuranceProviderException {
		LocalDate issuedDate = null;
		if (order.getPayment() != null) {
			issuedDate = order.getPayment().getNotifSuccessTime().toLocalDate();
		} else {
			issuedDate = order.getOrderDate();
		}

		InsurerPolicyFile policyFile = new InsurerPolicyFile();
		policyFile.setPremi(order.getTotalPremi());
		policyFile.setPolicyNumber(generatePolicyNumber(order, issuedDate));
		policyFile.setIssuedDate(issuedDate);
		policyFile.setInsurerId(order.getCoverageCategory().getInsurer().getId());
		policyFile.setUserId(order.getUserId());
		policyFile.setOrderId(order.getOrderId());
		policyFile.setCoverageCategoryId(order.getCoverageCategoryId());

		if (order.getHasVoucher() && order.getPolicyOrderVoucher() != null) {
			policyFile.setVoucherType(order.getPolicyOrderVoucher().getVoucher().getVoucherType());
			policyFile.setVoucherId(order.getPolicyOrderVoucher().getVoucher().getId());
		}

		// policyFile.setCountryId(order.getCoverageCategory());
		policyFile.setFilePath(policyFilePath(order.getOrderId(), order.getCoverageCategoryId(), issuedDate));

		return policyFile;
	}

	/**
	 * Example output: PA0000001/9Lives/2019
	 * 
	 * @param order
	 * @return
	 * @throws InsuranceProviderException
	 */
	protected String generatePolicyNumber(PolicyOrder order, LocalDate issuedDate) throws InsuranceProviderException {
		String sequenceName = null;
		String ptiCoverageCategory = null;
		String ptiPolicyYear = null;
		if (order.getCoverageCategory().getType().equals(CoverageCategoryType.PA)) {
			ptiCoverageCategory = PtiCoverageCategory.PA;
			ptiPolicyYear = String.valueOf(issuedDate.getYear());
			sequenceName = "pti_" + ptiCoverageCategory + ptiPolicyYear + "_seq";
		} else {
			throw new InsuranceProviderException("System error, fail to generate policy number");
		}

		int sequenceNum = insurerMapper.nextPolicyNumberSequence(sequenceName);

		if (sequenceNum == 0) {
			throw new InsuranceProviderException("System error, fail to generate policy number");
		}

		return String.format("%s%07d/%s/%s", ptiCoverageCategory, sequenceNum, "9Lives", ptiPolicyYear);
	}

	public PdfCreator getPdfCreator() {
		return pdfCreator;
	}

	public void setPdfCreator(PdfCreator pdfCreator) {
		this.pdfCreator = pdfCreator;
	}

	@PostConstruct
	private void init() throws IOException {

//		isPtiEnabled = isPtiEnabled();
		enableConnection = config.getInsurance().getPtiEnableConnection();
		if (enableConnection) {
			userFileToBase64JsonSerializer = new UserFileToBase64JsonSerializer(storageProvider);

			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(config.getInsurance().getAswataConnectionPoolSize());
			cm.setDefaultMaxPerRoute(config.getInsurance().getAswataConnectionPoolSize());

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(config.getInsurance().getAswataPoolTimeout())
					.setConnectTimeout(config.getInsurance().getAswataConnectTimeout())
					.setSocketTimeout(config.getInsurance().getAswataSocketTimeout()).build();

			HttpClient defaultHttpClient = HttpClientBuilder.create().setConnectionManager(cm)
					.setDefaultRequestConfig(requestConfig).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					defaultHttpClient);
			requestFactory.setBufferRequestBody(false);

			template = new RestTemplate(requestFactory);

			// custom object mapper for userfile's handler
			SimpleModule module = new SimpleModule();
			module.addSerializer(UserFile.class, userFileToBase64JsonSerializer);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(module);
			MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
			messageConverter.setObjectMapper(objectMapper);

			template.getMessageConverters().removeIf(m -> m instanceof MappingJackson2HttpMessageConverter);
			template.getMessageConverters().add(messageConverter);
		}

		templateCoverageMaxCount = config.getInsurance().getPtiTemplateCoverageMaxCount();
		policyFileDir = config.getInsurance().getPtiPolicyFileDir();
		templateFilePath = config.getInsurance().getPtiTemplateFilePath();
		templateFontFilePath = config.getInsurance().getPtiTemplateFontFilePath();
		if (config.getInsurance().getPtiTemplateFontDefaultAppearance() != null) {
			templateFontAppearance = config.getInsurance().getPtiTemplateFontDefaultAppearance();
		}

		pdfCreator = new PdfCreator(templateFilePath, templateFontFilePath, templateFontAppearance);

		providerUrl = config.getInsurance().getPtiUrl();
		imageDownloadUrl = config.getInsurance().getPtiInsurerUrl();
		secretCode = config.getInsurance().getPtiSecretCode();
		serviceCode = config.getInsurance().getPtiServiceCode();
	}

	public void setEnableConnection(Boolean enableConnection) {
		this.enableConnection = enableConnection;
	}

//	private boolean isPtiEnabled(){
//		if(env.acceptsProfiles("noaswata")){
//			return false;
//		}
//		return true;
//	}

}
