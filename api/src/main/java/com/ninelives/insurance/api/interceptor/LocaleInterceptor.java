package com.ninelives.insurance.api.interceptor;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class LocaleInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LocaleInterceptor.class);
			
	public static final String DEFAULT_HEADER_NAME = "x-locale";
	
	private String headerName = DEFAULT_HEADER_NAME;

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {

		String newLocaleStr = request.getHeader(getHeaderName());
		logger.debug("hore, dapet string "+newLocaleStr);
		if (newLocaleStr != null) {
			LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
			if (localeResolver == null) {
				throw new IllegalStateException(
						"No LocaleResolver found: not in a DispatcherServlet request?");
			}
			try {
				localeResolver.setLocale(request, response, parseLocaleValue(newLocaleStr));
			}
			catch (IllegalArgumentException ex) {
				logger.debug("Ignoring invalid locale value [" + newLocaleStr + "]: " + ex.getMessage());				
			}
		}
		//test
		logger.debug("hore, locale-resolver is: "+RequestContextUtils.getLocaleResolver(request).toString());
		logger.debug("hore, locale is: "+RequestContextUtils.getLocale(request));
		//--
		return true;
	}

	protected Locale parseLocaleValue(String localeValue) {
		return StringUtils.parseLocaleString(localeValue);
	}
}
