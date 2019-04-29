package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.InsurerPolicyFile;

@Mapper
public interface InsurerPolicyFileMapper {
    @Insert({
        "insert into public.insurer_policy_file (insurer_id, ",
        "country_id, issued_date, ",
        "policy_number, user_id, ",
        "order_id, voucher_id, ",
        "voucher_type, coverage_category_id, ",
        "premi, status, file_path ",
        ")",
        "values (#{insurerId,jdbcType=INTEGER}, ",
        "#{countryId,jdbcType=INTEGER}, #{issuedDate,jdbcType=DATE}, ",
        "#{policyNumber,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
        "#{orderId,jdbcType=VARCHAR}, #{voucherId,jdbcType=INTEGER}, ",
        "#{voucherType,jdbcType=VARCHAR}, #{coverageCategoryId,jdbcType=VARCHAR}, ",
        "#{premi,jdbcType=INTEGER}, #{status,jdbcType=VARCHAR}, #{filePath,jdbcType=VARCHAR} ",
        ")"
    })
    int insert(InsurerPolicyFile record);

}