package com.ninelives.insurance.notif.provider.notification;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.ninelives.insurance.notif.NinelivesNotifConfigProperties;
import com.ninelives.insurance.provider.notification.message.FcmNotifMessageDto;

@Component
public class FcmProvider {
	private static final Logger logger = LoggerFactory.getLogger(FcmProvider.class);
	
	@Autowired NinelivesNotifConfigProperties config;

	@Value("${ninelives-notif.fcm.private-key-location}")
	String privateKeyLocation;
	
	@Value("${ninelives-notif.fcm.project-id}")
	String projectId;

	private GoogleCredential googleCredential;
	private RestTemplate template;
	private String fcmUrl;
	
	public void sendNotification(FcmNotifMessageDto messageDto){
		logger.debug("Sending fcm notification to <{}> with data <{}> and access <{}>", fcmUrl, messageDto, getAccessToken());
		
		HttpHeaders restHeader = new HttpHeaders();
		restHeader.setContentType(MediaType.APPLICATION_JSON);
		restHeader.set("Authorization", "Bearer " + getAccessToken());
		
		HttpEntity<FcmNotifMessageDto> entity = new HttpEntity<>(messageDto, restHeader);
		
		logger.debug("To be send <{}>", entity);
		
		ResponseEntity<String> resp = null;
		try{
			resp = template.exchange(fcmUrl, HttpMethod.POST, entity, String.class);
		}catch(HttpClientErrorException e){
			String errorResponseBody = e.getResponseBodyAsString();
			logger.error("Error on sending fcm <{}> with response <{}> and exception <{}>", messageDto, errorResponseBody, e.getMessage());
		}	
		
		logger.debug("Receive charge response with entity {}", resp);
		
	}
	
	//test, return to private
	public String getAccessToken(){
		String token = googleCredential.getAccessToken();
		if(StringUtils.isEmpty(token)){
			try {
				googleCredential.refreshToken();
				token = googleCredential.getAccessToken();
			} catch (IOException e) {
				logger.error("Error on refresh token with message: {}", e.getMessage());
				logger.error("Error on refresh token", e);
			}
		}
		return token;
	}

	@PostConstruct
	private void init() throws IOException {
		googleCredential = GoogleCredential.fromStream(new FileInputStream(privateKeyLocation))
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
		googleCredential.refreshToken();
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(config.getFcm().getConnectionPoolSize());
		cm.setDefaultMaxPerRoute(config.getFcm().getConnectionPoolSize());
		
		RequestConfig requestConfig = RequestConfig.custom()
	            .setConnectionRequestTimeout(config.getFcm().getPoolTimeout())
	            .setConnectTimeout(config.getFcm().getConnectTimeout())
	            .setSocketTimeout(config.getFcm().getSocketTimeout())
	            .build();
	
		HttpClient defaultHttpClient = HttpClientBuilder
				.create()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(requestConfig)
				.build();

		template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(defaultHttpClient));
		
		fcmUrl = "https://fcm.googleapis.com/v1/projects/"+projectId+"/messages:send";
	}
	
	
}
