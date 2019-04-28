package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.InsurerMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.provider.insurance.pti.ref.PtiCoverageCategory;
import com.ninelives.insurance.ref.CoverageCategoryType;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class PtiInsuranceProvider implements InsuranceProvider {
	private static final Logger logger = LoggerFactory.getLogger(PtiInsuranceProvider.class);
	
	private static final String DEFAULT_DOCUMENT_LOCALE = "vi_VN";

	private static final String DEFAULT_TEMPLATE_FONT_APPREANCE = "0.18039 0.20392 0.21176 rg /F8 7.994 Tf";
	
	@Autowired NinelivesConfigProperties config;
	@Autowired TranslationService translationService;
	@Autowired InsurerMapper insurerMapper;
		
	PdfCreator pdfCreator;
	
	int templateCoverageMaxCount;
	String policyFileDir;
	String templateFilePath;
	String templateFontFilePath;
	String templateFontAppearance = DEFAULT_TEMPLATE_FONT_APPREANCE;
	
	
	Locale documentLocale;	
	DateTimeFormatter dateFormatter;
	NumberFormat numberFormat;
	
	public PtiInsuranceProvider(){
		this.documentLocale = LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE);
		this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		this.numberFormat = NumberFormat.getInstance( LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE) );
	}
	
	private void generatePolicy(PolicyOrder order){
		Map<String, String> fieldMap = new HashMap<>();

		if (order.getPolicyOrderVoucher() != null && order.getPolicyOrderVoucher().getVoucher() != null
				&& VoucherType.B2B.equals(order.getPolicyOrderVoucher().getVoucher().getVoucherType())) {
			fieldMap.put("buyer_name",order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientName());
			fieldMap.put("buyer_contact_no",order.getPolicyOrderVoucher().getVoucher().getCorporateClient().getCorporateClientPhoneNumber());
		}else{
			fieldMap.put("buyer_name",order.getPolicyOrderUsers().getName());
			fieldMap.put("buyer_contact_no",order.getPolicyOrderUsers().getPhone());
		}
				
		fieldMap.put("policy_number","PA0000021/9Lives/2019");		
		fieldMap.put("date_of_issue",order.getOrderDate().format(dateFormatter));
		fieldMap.put("insured_name",order.getPolicyOrderUsers().getName());
		fieldMap.put("insured_idcard",order.getPolicyOrderUsers().getIdCardNo());
		fieldMap.put("insured_birthdate",order.getPolicyOrderUsers().getBirthDate().format(dateFormatter));
		fieldMap.put("insured_buyer_relation","");
		fieldMap.put("policy_start_date",order.getPolicyStartDate().format(dateFormatter));
		fieldMap.put("policy_end_date",order.getPolicyEndDate().format(dateFormatter));

		int i=1;
		for(PolicyOrderProduct pop : order.getPolicyOrderProducts()){
			fieldMap.put("coverage_"+i, translationService.translate(pop.getProduct().getNameTranslationId(), documentLocale.getLanguage(), pop.getProductName()));
			fieldMap.put("limit_"+i, numberFormat.format(pop.getCoverageMaxLimit()));
			i++;
		}
		for(int j=i;j<=templateCoverageMaxCount;j++){
			fieldMap.put("coverage_"+j, "");
			fieldMap.put("limit_"+j, "");
		}
		
		fieldMap.put("premi", numberFormat.format(order.getTotalPremi()));
		fieldMap.put("period",translationService.translate(order.getPeriod().getNameTranslationId(), documentLocale.getLanguage(), order.getPeriod().getName()));
		
		//test --
		try {
			pdfCreator.printFieldMap(fieldMap, "D:\\local\\sts\\9lives\\test\\pdfTest-3.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//end --
	}
	
	@Override
	public OrderResult orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		//if has voucher and voucher is free then generate policy
		return null;
	}

	@Override
	public OrderConfirmResult orderConfirm(PolicyOrder order) throws InsuranceProviderException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentConfirmResult paymentConfirm(PolicyOrder order) throws InsuranceProviderException {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected String generatePolicyNumber(PolicyOrder order) throws InsuranceProviderException{
		//PA0000001/9Lives/2019
		String sequenceName = null; 
		String ptiCoverageCategory = null;
		String ptiPolicyYear = null;
		if(order.getCoverageCategory().getType().equals(CoverageCategoryType.PA)){
			ptiCoverageCategory = PtiCoverageCategory.PA;
			ptiPolicyYear = String.valueOf(order.getOrderDate().getYear());
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
