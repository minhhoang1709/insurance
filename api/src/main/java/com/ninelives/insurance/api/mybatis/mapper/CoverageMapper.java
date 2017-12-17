package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.Coverage;

@Mapper
public interface CoverageMapper {
	
//
//	@Select({"select coverage_id, coverage_category_id, name, recommendation, description, ",
//		"has_beneficiary, max_limit, is_recommended, created_date, update_date ",
//		"from ",
//		"public.coverage ",
//		"where status='A' ",
//		"order by coverage_id"
//		})
	List<Coverage> selectByStatusActive();
	
	Coverage selectByCoverageId(@Param("coverageId") String coverageId);
}
