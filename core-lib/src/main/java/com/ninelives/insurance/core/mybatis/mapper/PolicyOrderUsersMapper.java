package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderUsers;

@Mapper
public interface PolicyOrderUsersMapper {
    @Insert({
        "insert into public.policy_order_users (order_id, ",
        "email, name, gender, ",
        "birth_date, birth_place, ",
        "phone, address, ",
        "id_card_file_id) ",
        "values (#{orderId,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{gender,jdbcType=VARCHAR}, ",
        "#{birthDate,jdbcType=DATE}, #{birthPlace,jdbcType=VARCHAR}, ",
        "#{phone,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{idCardFileId,jdbcType=BIGINT})",
    })
    int insert(PolicyOrderUsers record);

}