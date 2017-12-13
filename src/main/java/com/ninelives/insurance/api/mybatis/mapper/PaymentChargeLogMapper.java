package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.PaymentChargeLog;

@Mapper
public interface PaymentChargeLogMapper {
    @Insert({
        "insert into public.payment_charge_log (charge_date, ",
        "policy_payment_id, order_id, ",
        "user_id, payment_seq, ",
        "total_amount )",
        "values (#{chargeDate,jdbcType=DATE}, ",
        "#{policyPaymentId,jdbcType=VARCHAR}, #{orderId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{paymentSeq,jdbcType=INTEGER}, ",
        "#{totalAmount,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PaymentChargeLog record);
    
    @Update({
        "update public.payment_charge_log",
        "set provider_transaction_id = #{providerTransactionId,jdbcType=VARCHAR},",
          "response_http_status = #{responseHttpStatus,jdbcType=VARCHAR},",
          "response_error_message = #{responseErrorMessage,jdbcType=VARCHAR},",
          "response_date = #{responseDate,jdbcType=TIMESTAMP},",
          "status = #{status,jdbcType=VARCHAR},",
          "update_date = now()",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateChargeResponseById(PaymentChargeLog record);
    
}