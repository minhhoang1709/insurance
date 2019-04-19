package com.ninelives.insurance.api.interceptor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.config.NinelivesConfigProperties.AppLocale;

@Component
public class LocaleInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LocaleInterceptor.class);
			
	@Autowired NinelivesConfigProperties config;
	
	public static final String DEFAULT_HEADER_NAME = "x-locale";
	
	private String headerName = DEFAULT_HEADER_NAME;
	
	private AppLocale localeConfig;
	private Map<String, Locale> supportedLocales;
	private Map<String, Locale> localeByCountries;

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		String localeStr = request.getHeader(getHeaderName());
		//logger.debug("hore, dapet string "+newLocaleStr);
		if (localeStr != null) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException(
						"No LocaleResolver found: not in a DispatcherServlet request?");
			}
			//Locale locale = supportedLocale(localeStr);
			//Locale locale = parseLocaleValue(newLocaleStr);
			try {
				
				if(!isDefaultLocale(localeStr)){
					Locale locale = supportedLocale(localeStr);
					if(locale != null){
						localeResolver.setLocale(request, response, locale);
					}					
				}
			}
			catch (IllegalArgumentException ex) {
				logger.debug("Ignoring invalid locale value [" + localeStr + "]: " + ex.getMessage());				
			}
		}
		//test
		//logger.debug("hore, locale-resolver is: "+RequestContextUtils.getLocaleResolver(request).toString());
		//logger.debug("hore, locale is: "+RequestContextUtils.getLocale(request));
		//--
		return true;
	}
	
	
	public Locale supportedLocale(String localeStr){
		Locale locale = supportedLocales.get(localeStr);
		if(locale == null){
			locale = localeByCountries.get(parseLocaleValue(localeStr).getCountry());
			//logger.debug("hore, locale is null, jadi ambil by country {} get {}",parseLocaleValue(localeStr).getCountry(),locale);
//			if(localeConfig.getDefaultLocaleByCountry().containsKey(locale.getCountry())){
//				locale = supportedLocales.get(localeConfig.getDefaultLocaleByCountry());
//			}			
		}
		return locale;
	}
	
	public boolean isDefaultLocale(String localeStr){
		return localeConfig.getDefaultLocale().equals(localeStr);
	}

	protected Locale parseLocaleValue(String localeValue) {
		return StringUtils.parseLocaleString(localeValue);
	}
	
	@PostConstruct
	protected void init(){
		localeConfig = config.getAppLocale();
		
		supportedLocales = new HashMap<>();
		for(String localeStr: localeConfig.getSupportedLocales()){
			supportedLocales.put(localeStr, LocaleUtils.toLocale(localeStr));
			//logger.debug("hore, daftar supported locale is: "+localeStr);
		}
		
		localeByCountries = new HashMap<>();
		localeConfig.getDefaultLocaleByCountry().forEach((k,v)->{
			localeByCountries.put(k, supportedLocales.get(v));
			//logger.debug("hore, country {} has local {}",k,v);
		});
	}
}
