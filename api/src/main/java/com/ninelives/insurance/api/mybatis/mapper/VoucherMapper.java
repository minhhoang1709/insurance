package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
