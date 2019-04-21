package com.ninelives.insurance.core.provider.insurance;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.service.TranslationService;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class PtiInsuranceProvider implements InsuranceProvider {
	private static final Logger logger = LoggerFactory.getLogger(PtiInsuranceProvider.class);
	
	public static final int DEFAULT_PDF_TEMPLATE_COVERAGE_MAX_COUNT = 6;
	public static final String DEFAULT_DOCUMENT_LOCALE = "vi_VN";
	
	@Autowired TranslationService translationService;
	
	PdfCreator pdfCreator;
	int pdfTemplateCoverageMaxCount = DEFAULT_PDF_TEMPLATE_COVERAGE_MAX_COUNT;
	Locale documentLocale;
	
	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	NumberFormat numberFormat;
	
	public PtiInsuranceProvider(){
		this.numberFormat = NumberFormat.getInstance( LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE) );
		this.documentLocale = LocaleUtils.toLocale(DEFAULT_DOCUMENT_LOCALE);
	}
	
	public void generatePolicy(PolicyOrder order){
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
		for(int j=i;j<=pdfTemplateCoverageMaxCount;j++){
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

	public PdfCreator getPdfCreator() {
		return pdfCreator;
	}

	public void setPdfCreator(PdfCreator pdfCreator) {
		this.pdfCreator = pdfCreator;
	}

	@Override
	public OrderResult orderPolicy(PolicyOrder order) throws InsuranceProviderException, IOException, StorageException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderConfirmResult orderConfirm(PolicyOrder order) throws InsuranceProviderException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
