package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.Period;
@Mapper
public interface PeriodMapper {
	@Select({"select period_id, name, value, unit, start_value, end_value, created_date, update_date ",
		"from ",
		"public.period ",
		"where status='A' ",
		"order by period_id"
		})	
	List<Period> selectByStatusActive();

	@Select({
        "select ",
        "period_id, name, value, unit, created_date, update_date ",
        "from public.period ",
        "where period_id = #{periodId,jdbcType=VARCHAR} "
    })
	Period selectPeriodByPeriodId(@Param("periodId") String periodId);

	@Select({
		"select (SUM(premi), bool_or(b.has_beneficiary)) as productcoverage ",
		"from product a, coverage b where b.coverage_id=a.coverage_id ",
		"and a.product_id in (#{productId1,jdbcType=VARCHAR},#{productId2,jdbcType=VARCHAR},",
		"#{productId3,jdbcType=VARCHAR},#{productId4,jdbcType=VARCHAR},#{productId4,jdbcType=VARCHAR},#{productId6,jdbcType=VARCHAR})"
		
    })
	String selectProductAndCoverage(@Param("productId1") String productId1,
			@Param("productId2") String productId2,@Param("productId3") String productId3,
			@Param("productId4") String productId4,@Param("productId5") String productId5,
			@Param("productId6") String productId6);

	
}
