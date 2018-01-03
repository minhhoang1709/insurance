package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderVoucher;

@Mapper
public interface PolicyOrderVoucherMapper {
    @Insert({
        "insert into public.policy_order_voucher (order_id, ",
        "voucher_id, code, voucher_type, ",
        "inviter_user_id)",
        "values (#{orderId,jdbcType=VARCHAR}, ",
        "#{voucher.id,jdbcType=INTEGER}, #{voucher.code,jdbcType=VARCHAR}, #{voucher.voucherType,jdbcType=VARCHAR}, ",
        "#{voucher.inviterUserId,jdbcType=VARCHAR})"
    })
    int insert(PolicyOrderVoucher record);

}