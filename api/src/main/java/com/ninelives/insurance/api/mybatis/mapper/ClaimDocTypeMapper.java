package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.ClaimDocType;

@Mapper
public interface ClaimDocTypeMapper {
    @Select({
        "select",
        "claim_doc_type_id, name, description, created_date, update_date",
        "from public.claim_doc_type",
        "where claim_doc_type_id = #{claimDocTypeId,jdbcType=VARCHAR}"
    })
    ClaimDocType selectByClaimDocTypeId(String claimDocTypeId);  
}