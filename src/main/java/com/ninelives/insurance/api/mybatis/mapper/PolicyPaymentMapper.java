package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.PolicyPayment;

@Mapper
public interface PolicyPaymentMapper {
    @Insert({
        "insert into public.policy_payment (charge_date, ",
        "order_id, user_id, ",
        "total_amount, status) ",
        "values (#{chargeDate,jdbcType=DATE}, ",
        "#{orderId,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
        "#{totalAmount,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "paymentId")
    int insertCharge(PolicyPayment record);

    @Update({
        "update public.policy_payment",
        "set status = #{status,jdbcType=VARCHAR},",
          "provider_payment_token = #{providerPaymentToken,jdbcType=VARCHAR},",
          "charge_response_date = #{chargeResponseDate,jdbcType=TIMESTAMP},",
          "update_date = now()",
        "where payment_id = #{paymentId,jdbcType=BIGINT}"
    })
    void updateChargeByPaymentId(PolicyPayment payment);
    
//    @Update({
//        "update public.policy_payment",
//        "set charge_date = #{chargeDate,jdbcType=DATE},",
//          "order_id = #{orderId,jdbcType=VARCHAR},",
//          "user_id = #{userId,jdbcType=VARCHAR},",
//          "total_amount = #{totalAmount,jdbcType=INTEGER},",
//          "status = #{status,jdbcType=VARCHAR},",
//          "payment_type = #{paymentType,jdbcType=VARCHAR},",
//          "provider_payment_token = #{providerPaymentToken,jdbcType=VARCHAR},",
//          "provider_transaction_status = #{providerTransactionStatus,jdbcType=VARCHAR},",
//          "provider_status_code = #{providerStatusCode,jdbcType=INTEGER},",
//          "charge_response_date = #{chargeResponseDate,jdbcType=TIMESTAMP},",
//          "settlement_date = #{settlementDate,jdbcType=TIMESTAMP},",
//          "created_date = #{createdDate,jdbcType=TIMESTAMP},",
//          "update_date = #{updateDate,jdbcType=TIMESTAMP}",
//        "where payment_id = #{paymentId,jdbcType=BIGINT}"
//    })
//    int updateByPrimaryKey(PolicyPayment record);
}