package com.ninelives.insurance.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ninelives.insurance.api.interceptor.AuthInterceptor;

@Configuration
public class NinelivesWebConfig extends WebMvcConfigurerAdapter{
	@Autowired AuthInterceptor authInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).excludePathPatterns(
				"/api/login",
				"/api/configs",
				"/api/products",
				"/payment/*",
				"/error",
				"/email/*",
				"/api/users/passwordReset");
	}
}
