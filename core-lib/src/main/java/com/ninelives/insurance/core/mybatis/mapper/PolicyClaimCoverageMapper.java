package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyClaimCoverage;

@Mapper
public interface PolicyClaimCoverageMapper {
    int insertList(List<PolicyClaimCoverage> records);
}