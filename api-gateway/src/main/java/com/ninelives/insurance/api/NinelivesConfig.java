package com.ninelives.insurance.api;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@ComponentScan({"com.ninelives.insurance.core.service, "
		+ "com.ninelives.insurance.core.trx, com.ninelives.insurance.core.provider"})
public class NinelivesConfig extends WebMvcConfigurerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(NinelivesConfig.class);
			
	@Autowired DataSource dataSource;

	@PostConstruct
	public void configInfo() {
		logger.info("Auto configuration, Datasource is <{}>", dataSource);
	}
	
	@Bean
	@ConfigurationProperties(prefix="ninelives")
	@Validated
	public NinelivesConfigProperties config(){
		return new NinelivesConfigProperties();
	}
	
}
