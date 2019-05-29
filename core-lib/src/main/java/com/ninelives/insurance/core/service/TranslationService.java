package com.ninelives.insurance.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.TranslateMapper;
import com.ninelives.insurance.model.ClaimDocType;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.CoverageOption;
import com.ninelives.insurance.model.OrderDocType;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.tlt.ClaimDocTypeTlt;
import com.ninelives.insurance.model.tlt.CoverageCategoryTlt;
import com.ninelives.insurance.model.tlt.CoverageOptionTlt;
import com.ninelives.insurance.model.tlt.CoverageTlt;
import com.ninelives.insurance.model.tlt.OrderDocTypeTlt;
import com.ninelives.insurance.model.tlt.PeriodTlt;
import com.ninelives.insurance.model.tlt.ProductTlt;
import com.ninelives.insurance.model.tlt.VoucherTlt;

@Service
public class TranslationService {
	//@Autowired AppTranslationTextMapper appTranslationTextMapper;
	
	@Autowired TranslateMapper translateMapper;
	
//	@Cacheable("AppTranslationText")
//	public String translate(Integer translationId, String languageCode, String defaultStr) {
//		if(languageCode == null ||languageCode.length()==0 || translationId == null) {
//			return defaultStr;
//		}
//		String result = appTranslationTextMapper.selectTextByTranslationIdAndLanguageCode(translationId, languageCode);
//		if(result==null||result.length()==0) {
//			result = defaultStr;
//		}
//		return result;
//	}
	
	@Cacheable(value="CoverageCategoryTlt", key="{#obj.coverageCategoryId, #languageCode}")
	public CoverageCategoryTlt translateDefaultIfEmpty(CoverageCategory obj, String languageCode) {
		CoverageCategoryTlt objTranslate = translateMapper
				.selectCoverageCategoryTltByIdAndLanguageCode(obj.getCoverageCategoryId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new CoverageCategoryTlt();
			objTranslate.setCoverageCategoryId(obj.getCoverageCategoryId());
			objTranslate.setName(obj.getName());
			objTranslate.setRecommendation(obj.getRecommendation());
		}
		return objTranslate;
	}
	
	@Cacheable(value="CoverageTlt", key="{#obj.coverageId, #languageCode}")
	public CoverageTlt translateDefaultIfEmpty(Coverage obj, String languageCode) {
		CoverageTlt objTranslate = translateMapper
				.selectCoverageTltByIdAndLanguageCode(obj.getCoverageId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new CoverageTlt();
			objTranslate.setCoverageId(obj.getCoverageId());
			objTranslate.setName(obj.getName());
			objTranslate.setRecommendation(obj.getRecommendation());
		}
		return objTranslate;
	}
	
	@Cacheable(value="PeriodTlt", key="{#obj.periodId, #languageCode}")
	public PeriodTlt translateDefaultIfEmpty(Period obj, String languageCode) {
		PeriodTlt objTranslate = translateMapper
				.selectPeriodTltByIdAndLanguageCode(obj.getPeriodId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new PeriodTlt();
			objTranslate.setPeriodId(obj.getPeriodId());
			objTranslate.setName(obj.getName());
		}
		return objTranslate;
	}
	
	@Cacheable(value="ProductTlt", key="{#obj.productId, #languageCode}")
	public ProductTlt translateDefaultIfEmpty(Product obj, String languageCode) {
		ProductTlt objTranslate = translateMapper
				.selectProductTltByIdAndLanguageCode(obj.getProductId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new ProductTlt();
			objTranslate.setProductId(obj.getProductId());
			objTranslate.setName(obj.getName());			
		}
		return objTranslate;
	}
	
	@Cacheable(value="VoucherTlt", key="{#obj.id, #languageCode}")
	public VoucherTlt translateDefaultIfEmpty(Voucher obj, String languageCode) {
		VoucherTlt objTranslate = translateMapper
				.selectVoucherTltByIdAndLanguageCode(obj.getId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new VoucherTlt();
			objTranslate.setId(obj.getId());
			objTranslate.setTitle(obj.getTitle());
			objTranslate.setSubtitle(obj.getSubtitle());
			objTranslate.setDescription(obj.getDescription());
		}
		return objTranslate;
	}
	
	@Cacheable(value="ClaimDocTypeTlt", key="{#obj.claimDocTypeId, #languageCode}")
	public ClaimDocTypeTlt translateDefaultIfEmpty(ClaimDocType obj, String languageCode) {
		ClaimDocTypeTlt objTranslate = translateMapper
				.selectClaimDocTypeTltByIdAndLanguageCode(obj.getClaimDocTypeId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new ClaimDocTypeTlt();
			objTranslate.setClaimDocTypeId(obj.getClaimDocTypeId());
			objTranslate.setName(obj.getName());
		}
		return objTranslate;
	}
	
	@Cacheable(value="OrderDocTypeTlt", key="{#obj.orderDocTypeId, #languageCode}")
	public OrderDocTypeTlt translateDefaultIfEmpty(OrderDocType obj, String languageCode) {
		OrderDocTypeTlt objTranslate = translateMapper
				.selectOrderDocTypeTltByIdAndLanguageCode(obj.getOrderDocTypeId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new OrderDocTypeTlt();
			objTranslate.setOrderDocTypeId(obj.getOrderDocTypeId());
			objTranslate.setName(obj.getName());
		}
		return objTranslate;
	}
	
	@Cacheable(value="CoverageOptionTlt", key="{#obj.id, #languageCode}")
	public CoverageOptionTlt translateDefaultIfEmpty(CoverageOption obj, String languageCode) {
		CoverageOptionTlt objTranslate = translateMapper
				.selectCoverageOptionTltByIdAndLanguageCode(obj.getId(), languageCode);
		if(objTranslate==null) {
			objTranslate = new CoverageOptionTlt();
			objTranslate.setId(obj.getId());
			objTranslate.setCoverageOptionName(obj.getCoverageOptionName());
		}
		return objTranslate;
	}
}
