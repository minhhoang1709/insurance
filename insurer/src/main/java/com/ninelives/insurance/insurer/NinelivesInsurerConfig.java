package com.ninelives.insurance.insurer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;

@Configuration
@EnableConfigurationProperties
@MapperScan("com.ninelives.insurance.core.mybatis.mapper")
@ComponentScan({"com.ninelives.insurance.core.service, com.ninelives.insurance.core.trx, com.ninelives.insurance.core.provider, com.ninelives.insurance.util"})
public class NinelivesInsurerConfig extends WebMvcConfigurerAdapter{
	@Bean
	@ConfigurationProperties(prefix="ninelives")
	@Validated
	public NinelivesConfigProperties config(){
		return new NinelivesConfigProperties();
	}

}
