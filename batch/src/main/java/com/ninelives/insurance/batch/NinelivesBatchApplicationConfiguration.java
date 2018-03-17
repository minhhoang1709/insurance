package com.ninelives.insurance.batch;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.annotation.Validated;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;

@Configuration
@MapperScan({"com.ninelives.insurance.core.mybatis.mapper", "com.ninelives.insurance.batch.mybatis.mapper"})
@ComponentScan({"com.ninelives.insurance.core.service", "com.ninelives.insurance.core.trx", "com.ninelives.insurance.core.provider"})
@EnableConfigurationProperties(NinelivesBatchConfigProperties.class)
public class NinelivesBatchApplicationConfiguration {
	
	@Bean(name="batchDataSource")
	@ConfigurationProperties(prefix = "ninelives-batch-app.datasource")
	public DataSource batchDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name="primaryDataSource")
	@ConfigurationProperties(prefix = "ninelives.datasource")
	@Primary
	public DataSource primaryDataSource(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix="ninelives")
	@Validated
	public NinelivesConfigProperties config(){
		return new NinelivesConfigProperties();
	}
	
}
