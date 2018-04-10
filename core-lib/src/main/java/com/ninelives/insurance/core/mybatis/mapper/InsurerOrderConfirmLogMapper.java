package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.InsurerOrderConfirmLog;

@Mapper
public interface InsurerOrderConfirmLogMapper {
	int insertSelective(InsurerOrderConfirmLog record);
}
