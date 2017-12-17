package com.ninelives.insurance.payment.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyPayment;

@Mapper
public interface PolicyPaymentMapper {

    int updateSelectiveByOrderIdAndPaymentId(PolicyPayment record);
}