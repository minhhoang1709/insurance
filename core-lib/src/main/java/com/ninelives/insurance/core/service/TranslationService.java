package com.ninelives.insurance.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.AppTranslationTextMapper;

@Service
public class TranslationService {
	@Autowired AppTranslationTextMapper appTranslationTextMapper;
	
	@Cacheable("AppTranslationText")
	public String translate(Integer translationId, String languageCode, String defaultStr) {
		if(languageCode == null ||languageCode.length()==0 || translationId == null) {
			return defaultStr;
		}
		String result = appTranslationTextMapper.selectTextByTranslationIdAndLanguageCode(translationId, languageCode);
		if(result==null||result.length()==0) {
			result = defaultStr;
		}
		return result;
	}
}
