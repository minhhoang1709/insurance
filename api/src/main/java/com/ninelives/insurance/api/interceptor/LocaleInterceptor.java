package com.ninelives.insurance.api.interceptor;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.ninelives.insurance.core.service.LocaleService;

@Component
public class LocaleInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LocaleInterceptor.class);
			
	@Autowired LocaleService localeService;
	
	public static final String DEFAULT_HEADER_NAME = "x-locale";
	public static final String DEFAULT_PARAMETER_NAME = "locale";
	
	private String headerName = DEFAULT_HEADER_NAME;
	private String parameterName = DEFAULT_PARAMETER_NAME;
	
	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		String localeStr = request.getHeader(getHeaderName());
		if(StringUtils.isEmpty(localeStr)) {
			localeStr = request.getParameter(getParameterName());
		}
		//logger.debug("hore, dapet string "+localeStr);
		if (localeStr != null) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException(
						"No LocaleResolver found: not in a DispatcherServlet request?");
			}
			try {
				if(localeStr.equals("id_ID")){
					localeStr = "in_ID";
				}
				if(!localeService.isDefaultLocale(localeStr)){
					Locale locale = localeService.supportedLocale(localeStr);
					if(locale != null){
						localeResolver.setLocale(request, response, locale);
					}					
				}
			}
			catch (IllegalArgumentException ex) {
				logger.debug("Ignoring invalid locale value [" + localeStr + "]: " + ex.getMessage());				
			}
		}
		//
		//logger.debug("hore, locale-resolver is: "+RequestContextUtils.getLocaleResolver(request).toString());
		//logger.debug("hore, locale is: "+RequestContextUtils.getLocale(request));
		//--
		return true;
	}

}
