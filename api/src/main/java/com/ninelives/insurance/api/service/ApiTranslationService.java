package com.ninelives.insurance.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.service.TranslationService;

@Service
public class ApiTranslationService {
	@Autowired TranslationService translationService;
	
	public String translate(int translationId, String defaultStr) {	
		return translationService.translate(translationId, LocaleContextHolder.getLocale().getLanguage(), defaultStr);
	}
}
