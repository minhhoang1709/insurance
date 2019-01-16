package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ninelives.insurance.model.PolicyOrderDocument;

@Mapper
public interface PolicyOrderDocumentMapper {
	int insertList(List<PolicyOrderDocument> records);	
}