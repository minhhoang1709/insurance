package com.ninelives.insurance.core.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppTranslationTextMapper {
	
	@Select({
        "select text",
        "from ",
        "public.app_translation_text a inner join app_language b on a.language_id=b.id",
        "where a.translation_id = #{translationId,jdbcType=INTEGER} and b.code= #{languageCode,jdbcType=VARCHAR}"
    })
	String selectTextByTranslationIdAndLanguageCode(@Param("translationId") int translationId,
			@Param("languageCode") String languageCode);
}