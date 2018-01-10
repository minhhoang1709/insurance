package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.InsurerOrderLog;

@Mapper
public interface InsurerOrderLogMapper {
    int insertSelective(InsurerOrderLog record);
}