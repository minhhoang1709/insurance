package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.ProviderOrderLog;

@Mapper
public interface ProviderOrderLogMapper {
    int insertSelective(ProviderOrderLog record);
}