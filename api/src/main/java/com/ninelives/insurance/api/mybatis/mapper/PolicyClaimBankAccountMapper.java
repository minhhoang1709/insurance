package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyClaimBankAccount;

@Mapper
public interface PolicyClaimBankAccountMapper {
    @Insert({
        "insert into public.policy_claim_bank_account (claim_id, account_name, ",
        "account_bank_name, account_bank_swift_code, ",
        "account_bank_swift, account_number) ",
        "values (#{claimId,jdbcType=VARCHAR}, #{accountName,jdbcType=VARCHAR}, ",
        "#{accountBankName,jdbcType=VARCHAR}, #{accountBankSwiftCode,jdbcType=VARCHAR}, ",
        "#{accountBankSwift,jdbcType=VARCHAR}, #{accountNumber,jdbcType=VARCHAR})"
    })
    int insert(PolicyClaimBankAccount record);

}