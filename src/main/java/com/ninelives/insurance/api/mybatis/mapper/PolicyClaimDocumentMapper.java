package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.api.model.PolicyClaimDocument;

@Mapper
public interface PolicyClaimDocumentMapper {
	int insertList(List<PolicyClaimDocument> records);
}