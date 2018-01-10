package com.ninelives.insurance.insurer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties(NinelivesInsurerConfigProperties.class)
public class NinelivesInsurerConfig extends WebMvcConfigurerAdapter{

}
