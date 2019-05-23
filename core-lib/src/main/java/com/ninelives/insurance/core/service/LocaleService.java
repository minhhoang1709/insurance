package com.ninelives.insurance.core.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.config.NinelivesConfigProperties.AppLocale;

@Service
public class LocaleService {
	@Autowired NinelivesConfigProperties config;
	
	private AppLocale localeConfig;
	private Map<String, Locale> supportedLocales;
	private Map<String, Locale> localeByCountries;
	private Locale defaultLocale;
	
	public Locale defaultLocaleByCountry(String country) {
		return localeByCountries.get(country);
	}
	
	public Locale getDefaultLocale(){
		return defaultLocale;
	}
	
	/**
	 * Return null if supported locale is not found
	 *  
	 * @param localeStr
	 * @return
	 */
	public Locale supportedLocale(String localeStr){
		Locale locale = supportedLocales.get(localeStr);
		if(locale == null){ //if supported language is not found, use the default language based on the country
			locale = defaultLocaleByCountry(parseLocaleValue(localeStr).getCountry());
		}
		return locale;
	}
	
	/**
	 * Return default locale if supported locale is not found
	 *  
	 * @param localeStr
	 * @return
	 */
	public Locale supportedLocale(String localeStr, Locale defaultIfNotFound){
		Locale locale = supportedLocales.get(localeStr);
		if(locale == null){ //if supported language is not found, use the default language based on the country
			locale = defaultLocaleByCountry(parseLocaleValue(localeStr).getCountry());
			if(locale == null){
				locale = defaultIfNotFound;
			}
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
		
		defaultLocale = LocaleUtils.toLocale(config.getAppLocale().getDefaultLocale());
	}
}
