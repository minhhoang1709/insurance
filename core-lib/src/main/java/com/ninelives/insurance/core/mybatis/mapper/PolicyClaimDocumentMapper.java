package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyClaimDocument;

@Mapper
public interface PolicyClaimDocumentMapper {
	int insertList(List<PolicyClaimDocument> records);
}