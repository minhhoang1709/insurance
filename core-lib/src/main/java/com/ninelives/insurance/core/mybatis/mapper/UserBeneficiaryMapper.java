package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.UserBeneficiary;

@Mapper
public interface UserBeneficiaryMapper {
    @Insert({
        "insert into public.user_beneficiary (user_id, ",
        "name, phone, email, ",
        "relationship)",
        "values (#{userId,jdbcType=VARCHAR}, ",
        "#{name,jdbcType=VARCHAR}, #{phone,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
        "#{relationship,jdbcType=VARCHAR})"
    })
    int insert(UserBeneficiary record);

    @Update({
        "update public.user_beneficiary",
        "set name = #{name,jdbcType=VARCHAR},",
          "phone = #{phone,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "relationship = #{relationship,jdbcType=VARCHAR},",
          "update_date = now() ",
        "where user_beneficiary_id = #{userBeneficiaryId,jdbcType=BIGINT}"
    })
    int updateByUserBeneficiaryId(UserBeneficiary record);
    
    @Select({
        "select",
        "user_beneficiary_id, user_id, name, phone, email, relationship, created_date, ",
        "update_date",
        "from public.user_beneficiary",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    UserBeneficiary selectByUserId(@Param("userId") String userId);
}