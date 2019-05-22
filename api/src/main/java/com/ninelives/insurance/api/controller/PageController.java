package com.ninelives.insurance.api.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/page")
public class PageController {
	
	@RequestMapping(value="/toc",
			method=RequestMethod.GET)	
	public String pageToc() {		
		String locale = LocaleContextHolder.getLocale().toString();
		//System.out.println("forward to " +"forward:/html/toc_"+locale+".html");
		return "forward:/html/toc_"+locale+".html";
	}
	
	@RequestMapping(value="/privacy",
			method=RequestMethod.GET)	
	public String pagePrivacy() {
		String locale = LocaleContextHolder.getLocale().toString();
		//System.out.println("forward to " +"forward:/html/privacy_"+locale+".html");
		return "forward:/html/privacy_"+locale+".html";
	}
	
	@RequestMapping(value="/faq",
			method=RequestMethod.GET)	
	public String pageFaq() {
		String locale = LocaleContextHolder.getLocale().toString();
		//System.out.println("forward to " +"forward:/html/privacy_"+locale+".html");
		return "forward:/html/faq_"+locale+".html";
	}
}
