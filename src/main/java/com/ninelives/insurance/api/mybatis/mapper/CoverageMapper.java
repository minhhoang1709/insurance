package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.api.model.Coverage;

@Mapper
public interface CoverageMapper {
	
	@Select({"select coverage_id, coverage_group, name, recommendation, description, ",
		"has_beneficiary, max_limit, created_date, update_date ",
		"from ",
		"public.coverage"
		})
	List<Coverage> selectAll();
}
