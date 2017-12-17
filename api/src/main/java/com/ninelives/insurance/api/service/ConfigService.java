package com.ninelives.insurance.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.mybatis.mapper.ClientAppConfigMapper;
import com.ninelives.insurance.model.ClientAppConfig;

@Service
public class ConfigService {
	@Autowired ClientAppConfigMapper clientAppConfigMapper;
	
	@Cacheable("ClientAppConfig")
	public Map<String, String> fetchClientAppConfigMapWithStatusActive(){
		List<ClientAppConfig> configs = clientAppConfigMapper.selectByStatusActive();
		
		Map<String, String> configMap = new HashMap<String, String>();
		for (ClientAppConfig i : configs){
			configMap.put(i.getName(),i.getValue());
		}
		
		return configMap; 
	}
	
}
