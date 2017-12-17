package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.ClientAppConfig;

@Mapper
public interface ClientAppConfigMapper {
	
	@Select("select id, name, value, created_date, update_date from public.client_app_config where status='A'") 
	List<ClientAppConfig> selectByStatusActive();
	
}
