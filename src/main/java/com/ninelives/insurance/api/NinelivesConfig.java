package com.ninelives.insurance.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ninelives.insurance.api.interceptor.AuthInterceptor;
import com.ninelives.insurance.api.provider.storage.StorageProperties;
import com.ninelives.insurance.api.service.StorageService;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class NinelivesConfig extends WebMvcConfigurerAdapter{
	@Autowired AuthInterceptor authInterceptor;
		
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).excludePathPatterns(
				"/login",
				"/configs",
				"/products",
				"/error");
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
