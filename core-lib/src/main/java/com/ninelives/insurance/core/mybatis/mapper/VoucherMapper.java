package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.Voucher;

@Mapper
public interface VoucherMapper {
	Voucher selectByCode(@Param("code") String code);
	Voucher selectByInviteCode(@Param("code") String code);
	
	@Select({
        "select ",
        "code, title, subtitle, description ",
        "from public.voucher ",
        "where id = #{id,jdbcType=INTEGER}"
    })
    Voucher selectVoucherForInviteById(@Param("id") Integer id);
	
	@Select({
        "select ",
        "uiv.code, v.title, v.subtitle, v.description ",
        "from public.user_invite_voucher uiv, public.voucher v",
        "where uiv.user_id = #{userId,jdbcType=VARCHAR} and v.id=uiv.voucher_id"
    })
    Voucher selectVoucherForInviteByUserId(@Param("userId") String userId);
	
	@Update({
		"update public.user_invite_voucher ",
		"set inviter_reward_count=inviter_reward_count+1 ",
		"where code=#{code,jdbcType=VARCHAR} and user_id=#{userId,jdbcType=VARCHAR}"
	})
	int increamentInviterRewardCount(@Param("code") String code, @Param("userId") String userId);
	
	@Update({
		"update public.voucher ",
		"set approve_cnt=approve_cnt+1 ",
		"where id = #{id,jdbcType=INTEGER}"
	})
	int increamentVoucherApproveCount(@Param("id") Integer id);
}
