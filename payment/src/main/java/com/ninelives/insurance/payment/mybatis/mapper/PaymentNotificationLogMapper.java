package com.ninelives.insurance.payment.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PaymentNotificationLog;

@Mapper
public interface PaymentNotificationLogMapper {
    @Insert({
        "insert into public.payment_notification_log (order_id, payment_seq, ",
        "transaction_time, transaction_status, ",
        "transaction_id, status_message, ",
        "status_code, payment_type, ",
        "payment_code, gross_amount, ",
        "fraud_status, processing_status, ",
        "other_properties)",
        "values (#{orderId,jdbcType=VARCHAR}, #{paymentSeq,jdbcType=INTEGER}, ",
        "#{transactionTime,jdbcType=TIMESTAMP}, #{transactionStatus,jdbcType=VARCHAR}, ",
        "#{transactionId,jdbcType=VARCHAR}, #{statusMessage,jdbcType=VARCHAR}, ",
        "#{statusCode,jdbcType=VARCHAR}, #{paymentType,jdbcType=VARCHAR}, ",
        "#{paymentCode,jdbcType=VARCHAR}, #{grossAmount,jdbcType=VARCHAR}, ",
        "#{fraudStatus,jdbcType=VARCHAR}, #{processingStatus,jdbcType=VARCHAR}, ",
        "#{otherProperties,jdbcType=VARCHAR})"
    })
    int insert(PaymentNotificationLog record);
    
}