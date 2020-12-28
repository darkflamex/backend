
package com.youandi.backend.auth.config;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.youandi.backend.domain.ApiToken;
import com.youandi.backend.domain.AuthenticationTokenImpl;
import com.youandi.backend.domain.LgAccountUser;
import com.youandi.backend.redis.RedisService;


public class AuthenticationProviderImpl implements org.springframework.security.authentication.AuthenticationProvider {

    private RedisService service;

    public AuthenticationProviderImpl(RedisService service) {
        this.service = service;
    }

    private LgAccountUser call(String sid) {
    	LgAccountUser user = new LgAccountUser();
    	return user;
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getPrincipal() + "";
//        String password = authentication.getCredentials() + "";
//
//        if (username == null || username.length() < 5) {
//            throw new BadCredentialsException("Username not found.");
//        }
//        if (password.length() < 5) {
//            throw new BadCredentialsException("Wrong password.");
//        }
//
//        //Right now just authenticate on the basis of the user=pass
//        if (username.equalsIgnoreCase(password)) {
//            ApiToken u = new ApiToken();
//            u.setUsername(username);
//            u.setCreated(new Date());
//            AuthenticationTokenImpl auth = new AuthenticationTokenImpl(u.getUsername(), Collections.emptyList());
//            auth.setAuthenticated(true);
//            auth.setDetails(u);
//            service.setValue(String.format("%s:%s", u.getUsername().toLowerCase(), auth.getHash()), u, TimeUnit.SECONDS, 3600L, true);
//            return auth;
//        } else {
//
//        }
//        return null;
    	
    	String sid = StringUtils.trimToEmpty((String)authentication.getPrincipal());
    	
    	if(StringUtils.isEmpty(sid)) {
    		
    	} 
    	
    	
    	//
    	ApiToken u = new ApiToken();
    	u.setUsername(sid);
    	u.setCreated(new Date());
    	
    	AuthenticationTokenImpl auth = new AuthenticationTokenImpl(sid, Collections.emptyList());
    	auth.setAuthenticated(true);
    	auth.setDetails(u);
    	service.setValue(String.format("%s:%s", u.getUsername().toLowerCase(), auth.getHash()), u, TimeUnit.SECONDS, 3600L, true);
    	return auth;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    

}
