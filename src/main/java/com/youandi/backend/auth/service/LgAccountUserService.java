package com.youandi.backend.auth.service;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.youandi.backend.domain.ApiToken;
import com.youandi.backend.domain.AuthenticationTokenImpl;
import com.youandi.backend.domain.LgAccountUser;
import com.youandi.backend.redis.RedisService;

public class LgAccountUserService {


	@Autowired
	private RedisService service;

	private LgAccountUser call(String sid) {
		LgAccountUser user = new LgAccountUser();
		return user;
	}

	public Authentication authenticate(String sid) {

		ApiToken user = (ApiToken) service.getValue(sid, ApiToken.class);
		if(user == null) {
			user = new ApiToken();
			user.setUsername(sid);
			user.setCreated(new Date());
			service.setValue(user.getUsername(), user, TimeUnit.SECONDS, 3600L, true);
		}
		
		AuthenticationTokenImpl auth = new AuthenticationTokenImpl(sid, Collections.emptyList());
		auth.setAuthenticated(true);
		auth.setDetails(user);

		return auth;
	}


}
