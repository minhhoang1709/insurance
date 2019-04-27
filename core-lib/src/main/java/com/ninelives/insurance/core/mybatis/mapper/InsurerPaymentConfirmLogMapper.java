package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.InsurerPaymentConfirmLog;

@Mapper
public interface InsurerPaymentConfirmLogMapper {
    int insertSelective(InsurerPaymentConfirmLog record);

}