package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.tlt.ClaimDocTypeTlt;
import com.ninelives.insurance.model.tlt.CoverageCategoryTlt;
import com.ninelives.insurance.model.tlt.CoverageOptionTlt;
import com.ninelives.insurance.model.tlt.CoverageTlt;
import com.ninelives.insurance.model.tlt.OrderDocTypeTlt;
import com.ninelives.insurance.model.tlt.PeriodTlt;
import com.ninelives.insurance.model.tlt.ProductTlt;
import com.ninelives.insurance.model.tlt.VoucherTlt;

@Mapper
public interface TranslateMapper {
    @Select({
        "select",
        "a.coverage_category_id, a.language_code, a.name, a.recommendation",
        "from public.coverage_category_tlt a",
        "where a.coverage_category_id = #{coverageCategoryId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })
    CoverageCategoryTlt selectCoverageCategoryTltByIdAndLanguageCode(@Param("coverageCategoryId") String coverageCategoryId, 
    		@Param("languageCode") String languageCode);
    
    @Select({
        "select",
        "a.period_id, a.language_code, a.name",
        "from public.period_tlt a",
        "where a.period_id = #{periodId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })    
	PeriodTlt selectPeriodTltByIdAndLanguageCode(@Param("periodId") String periodId,
			@Param("languageCode") String languageCode);
	
	@Select({
        "select",
        "a.coverage_id, a.language_code, a.name, a.recommendation",
        "from public.coverage_tlt a",
        "where a.coverage_id = #{coverageId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
	})
	CoverageTlt selectCoverageTltByIdAndLanguageCode(@Param("coverageId") String coverageId,
			@Param("languageCode") String languageCode);
	 
	@Select({
	    "select",
	    "a.product_id, a.language_code, a.name",
	    "from public.product_tlt a",
	    "where a.product_id = #{productId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
	})	    
	ProductTlt selectProductTltByIdAndLanguageCode(@Param("productId") String productId,
			@Param("languageCode") String languageCode);

    @Select({
        "select",
        "a.id, a.language_code, a.title, a.subtitle, a.description",
        "from public.voucher_tlt a",
        "where a.id = #{id,jdbcType=INTEGER}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })
    VoucherTlt selectVoucherTltByIdAndLanguageCode(@Param("id") Integer id, 
			@Param("languageCode") String languageCode);
    
    
    @Select({
        "select",
        "a.order_doc_type_id, a.language_code, a.name",
        "from public.order_doc_type_tlt a",
        "where a.order_doc_type_id = #{orderDocTypeId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })
    OrderDocTypeTlt selectOrderDocTypeTltByIdAndLanguageCode(@Param("orderDocTypeId") String orderDocTypeId, 
			@Param("languageCode") String languageCode);

    @Select({
        "select",
        "a.claim_doc_type_id, a.language_code, a.name",
        "from public.claim_doc_type_tlt a",
        "where a.claim_doc_type_id = #{claimDocTypeId,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })    
    ClaimDocTypeTlt selectClaimDocTypeTltByIdAndLanguageCode(@Param("claimDocTypeId") String claimDocTypeId, 
    		@Param("languageCode") String languageCode);

    @Select({
        "select",
        "a.id, a.language_code, a.coverage_option_name",
        "from public.coverage_option_tlt a",
        "where a.id = #{id,jdbcType=VARCHAR}",
          " and a.language_code= #{languageCode,jdbcType=VARCHAR}"
    })
    CoverageOptionTlt selectCoverageOptionTltByIdAndLanguageCode(@Param("id") String id, 
    		@Param("languageCode") String languageCode);

}
