package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.Product;

@Mapper
public interface ProductMapper {
		
	Product selectByProductId(@Param("productId") String productId);
	List<Product> select();
	List<Product> selectByTypeNormalAndStatusActive();	
	List<Product> selectByTypeNormalAndStatusActiveAndLocale(@Param("countryCode") String countryCode);
	//List<Product> selectByProductIds(@Param("productIds") Set<String> productIds);	
	
	@Select({
        "select ",
        "product_id, coverage_id,period_id,name, premi, status, product_type ",
        "from public.product ",
        "where coverage_id = #{coverageId,jdbcType=VARCHAR} ",
        "and period_id=#{periodId,jdbcType=VARCHAR}"
    })
	Product selectProductByCoverageIdAndPeriodId(@Param("coverageId") String coverageId, @Param("periodId") String periodId);
	
	@Select({
        "select ",
        "product_id, coverage_id,period_id,name, premi, status, product_type ",
        "from public.product ",
        "where product_type = #{productType,jdbcType=VARCHAR} ",
        "and status='A' "
    })
	List<Product> selectByProductTypeAndStatusActive(@Param("productType") String productType);


}
