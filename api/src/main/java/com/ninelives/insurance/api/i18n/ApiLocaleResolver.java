package com.ninelives.insurance.api.i18n;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

public class ApiLocaleResolver extends AbstractLocaleContextResolver {
	
	public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = ApiLocaleResolver.class.getName() + ".LOCALE";
	
	public static final String TIME_ZONE_REQUEST_ATTRIBUTE_NAME = ApiLocaleResolver.class.getName() + ".TIME_ZONE";
	
	private String localeAttributeName = LOCALE_REQUEST_ATTRIBUTE_NAME;
	private String timeZoneAttributeName = TIME_ZONE_REQUEST_ATTRIBUTE_NAME;
	/**
	 * Create a default FixedLocaleResolver, exposing a configured default
	 * locale (or the JVM's default locale as fallback).
	 * @see #setDefaultLocale
	 * @see #setDefaultTimeZone
	 */
	public ApiLocaleResolver() {
		setDefaultLocale(Locale.getDefault());
	}

	/**
	 * Create a FixedLocaleResolver that exposes the given locale.
	 * @param locale the locale to expose
	 */
	public ApiLocaleResolver(Locale locale) {
		setDefaultLocale(locale);
	}

	/**
	 * Create a FixedLocaleResolver that exposes the given locale and time zone.
	 * @param locale the locale to expose
	 * @param timeZone the time zone to expose
	 */
	public ApiLocaleResolver(Locale locale, TimeZone timeZone) {
		setDefaultLocale(locale);
		setDefaultTimeZone(timeZone);
	}


	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale) request.getAttribute(localeAttributeName);
		if (locale == null) {
			locale = getDefaultLocale();
		}
		return locale;
	}

	@Override
	public LocaleContext resolveLocaleContext(HttpServletRequest request) {
		return new TimeZoneAwareLocaleContext() {
			@Override
			@Nullable
			public Locale getLocale() {
				Locale locale = (Locale) request.getAttribute(localeAttributeName);
				if (locale == null) {
					locale = getDefaultLocale();
				}
				return locale;
			}
			@Override
			public TimeZone getTimeZone() {
				TimeZone timeZone = (TimeZone) request.getAttribute(timeZoneAttributeName);
				if (timeZone == null) {
					timeZone = getDefaultTimeZone();
				}
				return timeZone;
			}
		};
	}

	@Override
	public void setLocaleContext(HttpServletRequest request, HttpServletResponse response,
			LocaleContext localeContext) {
		Locale locale = null;
		TimeZone timeZone = null;
		if (localeContext != null) {
			locale = localeContext.getLocale();
			if (localeContext instanceof TimeZoneAwareLocaleContext) {
				timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone();
			}
		}
		request.setAttribute(localeAttributeName, locale);
		request.setAttribute(timeZoneAttributeName, timeZone);
	}

}
