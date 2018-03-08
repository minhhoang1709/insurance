package com.ninelives.insurance.api;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ninelives.insurance.api.interceptor.AuthInterceptor;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.provider.insurance.AswataInsuranceProvider;
import com.ninelives.insurance.core.provider.insurance.InsuranceProvider;
import com.ninelives.insurance.core.provider.payment.MidtransPaymentProvider;
import com.ninelives.insurance.core.provider.payment.PaymentProvider;
import com.ninelives.insurance.core.provider.storage.FileSystemStorageProvider;
import com.ninelives.insurance.core.provider.storage.StorageProvider;

@Configuration
@EnableConfigurationProperties
@MapperScan("com.ninelives.insurance.core.mybatis.mapper")
@ComponentScan({"com.ninelives.insurance.core.service, com.ninelives.insurance.core.trx"})
public class NinelivesConfig extends WebMvcConfigurerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(NinelivesConfig.class);
			
	@Autowired AuthInterceptor authInterceptor;
	@Autowired DataSource dataSource;
		
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).excludePathPatterns(
				"/api/login",
				"/api/configs",
				"/api/products",
				"/payment/*",
				"/error");
	}
	
	@PostConstruct
	public void configInfo() {
		logger.info("Auto configuration, Datasource is {}", dataSource);
		//logger.info("Ninelives config is {}", config);
		//logger.info("Auto configuration, TransactionManagement is {}", dataSource);
	}
	
	@Bean
	@ConfigurationProperties(prefix="ninelives")
	@Validated
	public NinelivesConfigProperties config(){
		return new NinelivesConfigProperties();
	}
	
	@Bean
	@ConditionalOnProperty(prefix="ninelives", name="storage.location")
	public StorageProvider storageProvider(@Autowired NinelivesConfigProperties config) {
		return new FileSystemStorageProvider(config);
	}
	
	@Bean
	@ConditionalOnProperty(prefix="ninelives", name="payment.midtrans-environment")
	public PaymentProvider paymentProvider(@Autowired NinelivesConfigProperties config) {
		return new MidtransPaymentProvider(config);
	}
	
	@Bean
	@ConditionalOnProperty(prefix="ninelives", name="insurance.aswata-url")
	public InsuranceProvider insuranceProvider(@Autowired NinelivesConfigProperties config) {
		return new AswataInsuranceProvider(config);
	}
	
//	@Autowired RedisConnectionFactory redisConnectionFactory; 
//	
//	@Bean
//	public  RedisTemplate<String, AuthToken> redisAuthTemplate() {
//		RedisTemplate<String, AuthToken> template = new RedisTemplate<String, AuthToken>();
//	    template.setConnectionFactory(redisConnectionFactory);
//	    return template;
//	}
	
//	@Bean
//	public ErrorAttributes errorAttributes() {
//	    return new DefaultErrorAttributes() {
//
//	        @Override
//	        public Map<String, Object> getErrorAttributes(
//	                RequestAttributes requestAttributes,
//	                boolean includeStackTrace) {
//	            Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
//	            Throwable error = getError(requestAttributes);
//	            
//	            Map<String, Object> errorDtoResponse = new HashMap<>();
//	            ErrorDto errorDto = new ErrorDto((Integer)errorAttributes.get("status"), 
//	            		"E111", 
//	            		error.getMessage());
//	            
//	            errorDtoResponse.put("error", errorDto);
//	            
//	            errorAttributes.forEach((k,v)->System.out.println("Entity : " + k + ", Item : " + v));
//	            
//	            return errorDtoResponse;
//	        }
//
//	    };
//	}
}
