package com.ninelives.insurance.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.InviteDto;
import com.ninelives.insurance.api.service.InviteService;
import com.ninelives.insurance.api.service.VoucherService;

@Controller
public class InviteController {
	private static final Logger logger = LoggerFactory.getLogger(InviteController.class);
	
	@Autowired InviteService inviteService;
	
	@RequestMapping(value="/invites",
			method={ RequestMethod.GET })
	@ResponseBody
	public InviteDto getInvites(@RequestAttribute ("authUserId") String authUserId){
		return inviteService.fetchInviteDtoByUserId(authUserId);
	}
}
