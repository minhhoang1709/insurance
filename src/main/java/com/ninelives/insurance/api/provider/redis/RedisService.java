package com.ninelives.insurance.api.provider.redis;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.util.GsonUtil;

@Service
public class RedisService {
	private static final String AUTH_TOKEN_KEY = "T:";
	
	private static final String AUTH_TOKEN_OBJ_FIELD = "O:";
	private static final String AUTH_TOKEN_UPDATE_MILLIS_FIELD = "M:";
	
	//private RedisTemplate<String, AuthToken> redisAuthTokenTemplate;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	private HashOperations<String, String, String> hashOps;
	
	@PostConstruct
    private void init() {
		hashOps = redisTemplate.opsForHash();
    }
	
	public void saveAuthToken(AuthToken authToken) {
		hashOps.put(AUTH_TOKEN_KEY + authToken.getTokenId(), AUTH_TOKEN_OBJ_FIELD, GsonUtil.gson.toJson(authToken, AuthToken.class));		
	}
	public void touchAuthToken(String tokenId){
		hashOps.put(AUTH_TOKEN_KEY + tokenId, AUTH_TOKEN_UPDATE_MILLIS_FIELD, String.valueOf(System.currentTimeMillis()));
	}
	
}
