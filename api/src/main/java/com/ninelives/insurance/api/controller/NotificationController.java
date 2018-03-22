package com.ninelives.insurance.api.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.UserNotificationDto;
import com.ninelives.insurance.api.service.ApiNotificationService;
import com.ninelives.insurance.core.util.GsonUtil;

@Controller
@RequestMapping("/api")
public class NotificationController {
	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	
	@Autowired ApiNotificationService apiNotificationService;
	
	@RequestMapping(value="/notifications",
			method=RequestMethod.GET)	
	@ResponseBody
	public List<UserNotificationDto> getNotifications(@RequestAttribute("authUserId") String authUserId,
			@RequestParam(value="filter",required=false) String filter){
		
		FilterDto filterDto = null;
		if(!StringUtils.isEmpty(filter)){
			filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
		}		
		
		return apiNotificationService.fetchDtoForInbox(authUserId, filterDto);
	}
}
