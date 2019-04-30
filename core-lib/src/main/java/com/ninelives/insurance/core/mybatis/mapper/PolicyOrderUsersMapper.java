package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.PolicyOrderUsers;

@Mapper
public interface PolicyOrderUsersMapper {
    @Insert({
        "insert into public.policy_order_users (order_id, ",
        "email, name, gender, ",
        "birth_date, birth_place, ",
        "phone, address, ",
        "id_card_file_id, passport_file_id, id_card_no) ",
        "values (#{orderId,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, #{gender,jdbcType=VARCHAR}, ",
        "#{birthDate,jdbcType=DATE}, #{birthPlace,jdbcType=VARCHAR}, ",
        "#{phone,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{idCardFileId,jdbcType=BIGINT}, #{passportFileId,jdbcType=BIGINT}, #{idCardNo,jdbcType=VARCHAR})"
    })
    int insert(PolicyOrderUsers record);
    
    @Select({
        "select",
        "order_id, email, name, gender, birth_date, birth_place, ",
        "phone, address, id_card_file_id, created_date, update_date, id_card_no, passport_file_id",
        "from public.policy_order_users",
        "where order_id = #{orderId,jdbcType=BIGINT}"
    })
    @ResultMap("com.ninelives.insurance.core.mybatis.mapper.PolicyOrderUsersMapper.BaseResultMap")
    PolicyOrderUsers selectByOrderId(String orderId);

}