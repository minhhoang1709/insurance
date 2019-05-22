package com.ninelives.insurance.api;

import java.util.TimeZone;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import com.ninelives.insurance.api.i18n.ApiLocaleResolver;
import com.ninelives.insurance.api.interceptor.AuthInterceptor;
import com.ninelives.insurance.api.interceptor.LocaleInterceptor;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;

@Configuration
public class NinelivesWebConfig extends WebMvcConfigurerAdapter{
	@Autowired NinelivesConfigProperties config;
	
	@Autowired AuthInterceptor authInterceptor;
	@Autowired LocaleInterceptor localeInterceptor;
	
	private String defaultTimeZoneStr;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).excludePathPatterns(
				"/api/login",
				"/api/configs",
				"/api/products",
				"/payment/*",
				"/page/*",
				"/api/orders/*/pay",
				"/api/orders/*/payfinish",
				"/error",
				"/email/*",
				"/api/users/passwordReset",
				"/api/resultPayment/2c2p");
		registry.addInterceptor(localeInterceptor);
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		ApiLocaleResolver localeResolver = new ApiLocaleResolver();
		localeResolver.setDefaultLocale(LocaleUtils.toLocale(config.getAppLocale().getDefaultLocale()));
		if(!StringUtils.isEmpty(defaultTimeZoneStr)) {
			localeResolver.setDefaultTimeZone(TimeZone.getTimeZone(defaultTimeZoneStr));			
		}
		return localeResolver;
	}
	
	/*
	 * For debugging on development
	 */
	@Bean
	@Profile("dev")
	public CommonsRequestLoggingFilter requestLoggingFilter() {
	    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
	    loggingFilter.setIncludeHeaders(true);
	    loggingFilter.setIncludeClientInfo(true);
	    loggingFilter.setIncludeQueryString(true);
	    loggingFilter.setIncludePayload(true);
	    loggingFilter.setMaxPayloadLength(10000);
	    return loggingFilter;
	}
	
}
