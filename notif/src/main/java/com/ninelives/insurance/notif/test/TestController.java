package com.ninelives.insurance.notif.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.notif.provider.notification.FcmProvider;

@Controller
public class TestController {
	@Autowired FcmProvider fcmprovider;
	
	@GetMapping("/test/token")
	@ResponseBody
	public String googleToken(){
		return fcmprovider.testAccessToken();
	}
}
