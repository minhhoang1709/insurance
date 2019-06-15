package com.ninelives.insurance.insurer.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.InsurerPaymentConfirmLog;

//@Mapper
@Deprecated
public interface InsurerPaymentConfirmLogMapper {
    int insertSelective(InsurerPaymentConfirmLog record);

}