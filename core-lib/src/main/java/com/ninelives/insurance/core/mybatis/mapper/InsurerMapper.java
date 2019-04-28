package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InsurerMapper {
	@Select({
		"select next_policy_number(#{sequenceName, jdbcType=VARCHAR})"
	})
	public int nextPolicyNumberSequence(String sequenceName);
}
