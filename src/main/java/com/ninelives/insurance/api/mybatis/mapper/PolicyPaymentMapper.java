package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.PolicyPayment;

@Mapper
public interface PolicyPaymentMapper {
//    @Insert({
//        "insert into public.policy_payment (payment_id, charge_date, ",
//        "order_id, user_id, ",
//        "total_amount, status) ",
//        "values (#{paymentId,jdbcType=VARCHAR}, #{chargeDate,jdbcType=DATE}, ",
//        "#{orderId,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
//        "#{totalAmount,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR})"
//    })
//    @Options(useGeneratedKeys = true, keyProperty = "paymentId")
//    int insertCharge(PolicyPayment record);

    @Update({
        "update public.policy_payment",
        "set status = #{status,jdbcType=VARCHAR},",
          "charge_date=#{chargeDate,jdbcType=TIMESTAMP},",
          "cnt=#{cnt,jdbcType=INTEGER},",
          "provider_transaction_id = #{providerTransactionId,jdbcType=VARCHAR},",
          "update_date = now()",
        "where id = #{id,jdbcType=BIGINT}"
    })
    void updateChargeResponseById(PolicyPayment payment);
    
    @Insert({
        "insert into public.policy_payment (id, payment_start_date, ",
        "order_id, user_id, ",
        "total_amount, status, ",        
        "charge_date, cnt) ",
        "values (#{id,jdbcType=VARCHAR}, #{paymentStartDate,jdbcType=DATE}, ",
        "#{orderId,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
        "#{totalAmount,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR}, ",
        "#{chargeDate,jdbcType=TIMESTAMP}, #{cnt,jdbcType=INTEGER})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertForStatusCharge(PolicyPayment record);
}