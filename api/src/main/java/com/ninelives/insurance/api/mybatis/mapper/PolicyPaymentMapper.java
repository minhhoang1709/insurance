package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.PolicyPayment;

@Mapper
public interface PolicyPaymentMapper {
    @Update({
        "update public.policy_payment",
        "set status = #{status,jdbcType=VARCHAR},",
          "charge_time = #{chargeTime,jdbcType=TIMESTAMP},",
          "charge_expiry_time = #{chargeExpiryTime,jdbcType=TIMESTAMP},",
          "payment_seq = #{paymentSeq,jdbcType=INTEGER},",
          "provider_transaction_id = #{providerTransactionId,jdbcType=VARCHAR},",
          "provider_transaction_time = NULL,",
          "provider_status_code = NULL,",
          "provider_transaction_status = NULL,",
          "payment_type = NULL,",
          "update_date = now()",
        "where id = #{id,jdbcType=BIGINT}"
    })
    void updateChargeResponseById(PolicyPayment payment);
    
    @Insert({
        "insert into public.policy_payment (id, order_id, ",
        "user_id, total_amount, payment_seq, ",
        "start_time, charge_time, charge_expiry_time, provider_transaction_id, status) ",
        "values (#{id,jdbcType=VARCHAR}, #{orderId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{totalAmount,jdbcType=INTEGER}, #{paymentSeq,jdbcType=INTEGER}, ",
        "#{startTime,jdbcType=TIMESTAMP}, #{chargeTime,jdbcType=TIMESTAMP}, #{chargeExpiryTime,jdbcType=TIMESTAMP}, ",
        "#{providerTransactionId,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})"
    })
    int insertForStatusCharge(PolicyPayment record);
    
}