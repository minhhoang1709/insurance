package com.ninelives.insurance.api.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.UserNotificationDto;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.model.UserNotification;

@Service
public class ApiNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(ApiNotificationService.class);
	
	@Autowired NotificationService notificationService;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	int defaultOffsetForInbox = 0;
	int defaultLimitForInbox = 15;
	int maxLimitForInbox = 15;
	
	public List<UserNotificationDto> fetchDtoForInbox(String userId){
		return fetchDtoForInbox(userId, null);
	}
	
	public List<UserNotificationDto> fetchDtoForInbox(String userId, FilterDto filter){		
		int offset = defaultOffsetForInbox;
		int limit = defaultLimitForInbox;
		if(filter!=null){
			offset = filter.getOffset();
			if(filter.getLimit()<maxLimitForInbox){
				limit = filter.getLimit();
			}
		}
		
		ArrayList<UserNotificationDto> dtos = new ArrayList<>();
		List<UserNotification> notifications = notificationService.fetchNotification(userId, limit, offset);
		if(notifications!=null){
			for(UserNotification n: notifications){
				dtos.add(modelMapperAdapter.toDto(n));
			}
		}
		return dtos;
	}
	
}
