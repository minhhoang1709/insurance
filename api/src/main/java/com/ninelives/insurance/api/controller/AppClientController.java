package com.ninelives.insurance.api.controller;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.exception.AppUpgradeRequiredException;
import com.ninelives.insurance.api.model.Version;
import com.ninelives.insurance.api.service.ConfigService;
import com.ninelives.insurance.api.util.AppVersionUtil;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class AppClientController {
	
	@Autowired ConfigService configService;
	
	@Value("${ninelives-api.minimum-client-app-version}")
	String minimumClientVersionStr;
	
	Version minimumClientVersion;
	
	@RequestMapping(value="/configs",
			method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getConfigs(@RequestParam("appVersion") String appVersion)
			throws AppUpgradeRequiredException {
		if(AppVersionUtil.isUpgradeRequired(minimumClientVersion, appVersion)){
			throw new AppUpgradeRequiredException(ErrorCode.ERR1003_UPGRADE_REQUIRED, 
					"Aplikasi 9Lives anda butuh pembaharuan untuk pengalaman yang lebih baik.");
		}else{
			return configService.fetchClientAppConfigMapWithStatusActive();
		}
	}
	
	@PostConstruct
	private void init(){
		if(!StringUtils.isEmpty(minimumClientVersionStr)){
			minimumClientVersion = AppVersionUtil.parse(minimumClientVersionStr);
		}
	}
}
