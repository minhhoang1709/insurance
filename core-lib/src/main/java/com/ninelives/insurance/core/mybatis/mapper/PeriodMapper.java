package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.Period;
@Mapper
public interface PeriodMapper {
	@Select({"select period_id, name, value, unit, created_date, update_date ",
		"from ",
		"public.period ",
		"where status='A' ",
		"order by period_id"
		})	
	List<Period> selectByStatusActive();
}
