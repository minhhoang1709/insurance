package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.InsurerMapper;
import com.ninelives.insurance.core.mybatis.mapper.InsurerPolicyFileMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.InsurerPolicyFile;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.provider.insurance.pti.ref.PtiCoverageCategory;
import com.ninelives.insurance.ref.CoverageCategoryType;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class PtiInsuranceProvider implements InsuranceProvider {
	private static final Logger logger = LoggerFactory.getLogger(PtiInsuranceProvider.class);
	
	private static final String DEFAULT_DOCUMENT_LOCALE = "vi_VN";

	private static final String DEFAULT_TEMPLATE_FONT_APPREANCE = "0.18039 0.20392 0.21176 rg /F8 7.994 Tf";
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired StorageProvider storageProvider;
	@Autowired ProductService productService;
	@Autowired TranslationService translationService;
	@Autowired InsurerMapper insurerMapper;
	@Autowired InsurerPolicyFileMapper insurerFileMapper;
		
	PdfCreator pdfCreator;
	
	int templateCoverageMaxCount;
	String policyFileDir;
	String templateFilePath;
	String templateFontFilePath;
	String templateFontAppearance = DEFAULT_TEMPLATE_FONT_APPREANCE;	
	
	Locale documentLocale;	
	DateTimeFormatter dateFormatter;
	DateTimeFormatter policyFileDirDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	NumberFormat numberFormat;
	
	public PtiInsuranceProvider(){
		this.documentLocale = LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE);
		this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		this.numberFormat = NumberFormat.getInstance( LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE) );
	}
	
	private InsurerPolicyFile generatePolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException{
		InsurerPolicyFile policyFile = policyFile(order);
		
		Map<String, String> fieldMap = new HashMap<>();

		if (order.getPolicyOrderVoucher() != null && order.getPolicyOrderVoucher().getVoucher() != null
				&& VoucherType.B2B.equals(order.getPolicyOrderVoucher().getVoucher().getVoucherType())) {
			fieldMap.put("buyer_name",order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientName());
			fieldMap.put("buyer_contact_no",order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientPhoneNumber());
		}else{
			fieldMap.put("buyer_name",order.getPolicyOrderUsers().getName());
			fieldMap.put("buyer_contact_no",order.getPolicyOrderUsers().getPhone());
		}
				
		fieldMap.put("policy_number",policyFile.getPolicyNumber());		
		fieldMap.put("date_of_issue",policyFile.getIssuedDate().format(dateFormatter));
		fieldMap.put("insured_name",order.getPolicyOrderUsers().getName());
		fieldMap.put("insured_idcard",order.getPolicyOrderUsers().getIdCardNo());
		fieldMap.put("insured_birthdate",order.getPolicyOrderUsers().getBirthDate().format(dateFormatter));
		fieldMap.put("insured_buyer_relation","");
		fieldMap.put("policy_start_date",order.getPolicyStartDate().format(dateFormatter));
		fieldMap.put("policy_end_date",order.getPolicyEndDate().format(dateFormatter));

		int i=1;
		for(PolicyOrderProduct pop : order.getPolicyOrderProducts()){
			Product product = pop.getProduct();
			if(product==null) {
				product = productService.fetchProductByProductId(pop.getProductId());
			}
			fieldMap.put("coverage_"+i, translationService.translate(product.getNameTranslationId(), documentLocale.getLanguage(), product.getName()));
			fieldMap.put("limit_"+i, numberFormat.format(pop.getCoverageMaxLimit()));
			i++;
		}
		for(int j=i;j<=templateCoverageMaxCount;j++){
			fieldMap.put("coverage_"+j, "");
			fieldMap.put("limit_"+j, "");
		}
		
		fieldMap.put("premi", numberFormat.format(order.getTotalPremi()));
		fieldMap.put("period",translationService.translate(order.getPeriod().getNameTranslationId(), documentLocale.getLanguage(), order.getPeriod().getName()));
		
		insurerFileMapper.insert(policyFile);
		
		try {
			Path filepath = storageProvider.prepare(policyFile.getFilePath());						
			pdfCreator.printFieldMap(fieldMap, filepath.toString());
		} catch (IOException|StorageException e) {
			logger.error("Error on creating pdf ", e);
			throw e;
		}
		
		return policyFile;
	}
	
	@Override
	public OrderResult orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		//if has voucher and voucher is free then generate policy-
		OrderResult result = new OrderResult();
		
		if (order.getPolicyOrderVoucher() != null && order.getTotalPremi() == 0
				&& (order.getPolicyOrderVoucher().getVoucher().getVoucherType().equals(VoucherType.INVITE)
						|| order.getPolicyOrderVoucher().getVoucher().getVoucherType()
								.equals(VoucherType.FREE_PROMO_NEW_USER))) {
			InsurerPolicyFile policyFile = generatePolicy(order);
			
			if (policyFile != null) {
				result.setSuccess(true);
				result.setPolicyNumber(policyFile.getPolicyNumber());
				result.setProviderDownloadUrl(policyFile.getFilePath());
			} else {
				result.setSuccess(false);
			}

		}else { //do nothing on order policy since the policy is generated on payment/order confirm for non free
			result.setSuccess(true);			
		}
	
		return result;
	}

	@Override
	public OrderConfirmResult orderConfirm(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		OrderConfirmResult result = new OrderConfirmResult();
		InsurerPolicyFile policyFile = generatePolicy(order);

		if(policyFile!=null) {
			result.setSuccess(true);
			result.setPolicyNumber(policyFile.getPolicyNumber());
			result.setProviderDownloadUrl(policyFile.getFilePath());
		}else {
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public PaymentConfirmResult paymentConfirm(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		PaymentConfirmResult result = new PaymentConfirmResult();
		InsurerPolicyFile policyFile = generatePolicy(order);

		if(policyFile!=null) {
			result.setSuccess(true);
			result.setPolicyNumber(policyFile.getPolicyNumber());
			result.setProviderDownloadUrl(policyFile.getFilePath());
		}else {
			result.setSuccess(false);
		}
		
		return result;
	}
	
	private String policyFilePath(String orderId, String coverageCategoryId, LocalDate issuedDate){
		Path path = Paths.get(policyFileDir, coverageCategoryId, issuedDate.format(policyFileDirDateFormatter));
		return FilenameUtils.concat(path.toString(),orderId+".pdf");		
	}
	
	protected InsurerPolicyFile policyFile(PolicyOrder order) throws InsuranceProviderException{
		LocalDate issuedDate = null;
		if(order.getPayment() != null) {
			issuedDate = order.getPayment().getNotifSuccessTime().toLocalDate();
		}else {
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
		
		if(order.getHasVoucher() && order.getPolicyOrderVoucher()!=null){
			policyFile.setVoucherType(order.getPolicyOrderVoucher().getVoucher().getVoucherType());
			policyFile.setVoucherId(order.getPolicyOrderVoucher().getVoucher().getId());
		}
		
		//policyFile.setCountryId(order.getCoverageCategory());
		policyFile.setFilePath(policyFilePath(order.getOrderId(), order.getCoverageCategoryId(), issuedDate));
		
		
		return policyFile;
	}
	
	/**
	 * Example output: PA0000001/9Lives/2019
	 * @param order
	 * @return
	 * @throws InsuranceProviderException
	 */
	protected String generatePolicyNumber(PolicyOrder order, LocalDate issuedDate) throws InsuranceProviderException{
		String sequenceName = null; 
		String ptiCoverageCategory = null;
		String ptiPolicyYear = null;
		if(order.getCoverageCategory().getType().equals(CoverageCategoryType.PA)){
			ptiCoverageCategory = PtiCoverageCategory.PA;
			ptiPolicyYear = String.valueOf(issuedDate.getYear());
			sequenceName = "pti_"+ptiCoverageCategory+ptiPolicyYear+"_seq";
		}else{
			throw new InsuranceProviderException("System error, fail to generate policy number");
		}		
		
		int sequenceNum = insurerMapper.nextPolicyNumberSequence(sequenceName);
		
		if(sequenceNum==0){
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
		templateCoverageMaxCount = config.getInsurance().getPtiTemplateCoverageMaxCount();
		policyFileDir = config.getInsurance().getPtiPolicyFileDir();
		templateFilePath = config.getInsurance().getPtiTemplateFilePath();
		templateFontFilePath = config.getInsurance().getPtiTemplateFontFilePath();
		if(config.getInsurance().getPtiTemplateFontDefaultAppearance()!=null){
			templateFontAppearance = config.getInsurance().getPtiTemplateFontDefaultAppearance();
		}
		
		pdfCreator = new PdfCreator(templateFilePath, templateFontFilePath, templateFontAppearance);
				
	}
	
}
