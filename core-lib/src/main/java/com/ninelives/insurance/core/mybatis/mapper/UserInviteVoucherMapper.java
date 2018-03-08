package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.UserInviteVoucher;

@Mapper
public interface UserInviteVoucherMapper {
    @Insert({
        "insert into public.user_invite_voucher (user_id, code, ",
        "voucher_id, status, ",
        "inviter_reward_count)",
        "values (#{userId,jdbcType=VARCHAR}, #{code,jdbcType=VARCHAR}, ",
        "#{voucherId,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR}, ",
        "#{inviterRewardCount,jdbcType=INTEGER})"
    })
    int insert(UserInviteVoucher record);
}