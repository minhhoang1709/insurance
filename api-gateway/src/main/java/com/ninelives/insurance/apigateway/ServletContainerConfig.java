package com.ninelives.insurance.apigateway;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class ServletContainerConfig {
	@Value("${server.http.port:0}")
	private int httpPort;

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (httpPort!=0 && container instanceof TomcatEmbeddedServletContainerFactory) {
	                TomcatEmbeddedServletContainerFactory containerFactory =
	                        (TomcatEmbeddedServletContainerFactory) container;

	                Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
	                connector.setPort(httpPort);
	                containerFactory.addAdditionalTomcatConnectors(connector);
	            }
			}
	    };
	}
}
