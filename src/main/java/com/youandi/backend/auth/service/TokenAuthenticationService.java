package com.youandi.backend.auth.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Sha512DigestUtils;

import com.youandi.backend.auth.common.ApiTokenConfig;
import com.youandi.backend.domain.ApiToken;
import com.youandi.backend.domain.AuthenticationTokenImpl;
import com.youandi.backend.redis.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationService.class);
	
    private RedisService service;

    private String secret;

    private ApiTokenConfig apiTokenConfig;
    
    public TokenAuthenticationService(RedisService service, ApiTokenConfig apiTokenConfig) {
        
    	this.apiTokenConfig = apiTokenConfig;
    	
    	logger.error("apiTokenConfig : " + apiTokenConfig.getExpired());
    	
    	this.service = service;
        secret = Sha512DigestUtils.shaHex(apiTokenConfig.getSigningKey());
    }

    public void addAuthentication(HttpServletResponse response, AuthenticationTokenImpl auth) {
        // We generate a token now.
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", auth.getPrincipal());
        claims.put("hash", auth.getHash());
        String JWT = Jwts.builder()
                .setSubject(auth.getPrincipal().toString())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + apiTokenConfig.getExpired()))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        response.addHeader(ApiTokenConfig.AUTHORIZATION_HEADER_NAME, ApiTokenConfig.AUTHORIZATION_HEADER_PREFIX + " " + JWT);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(ApiTokenConfig.AUTHORIZATION_HEADER_NAME);
        if (token == null) {
            return null;
        }
        //remove "Bearer" text
        token = token.replace(ApiTokenConfig.AUTHORIZATION_HEADER_PREFIX, "").trim();

        //Validating the token
        if (token != null && !token.isEmpty()) {
            // parsing the token.`
            Claims claims = null;
            try {
                claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token).getBody();

            } catch ( Exception e) {
                return null;
            }
            
            //Valid token and now checking to see if the token is actally expired or alive by quering in redis.
            if (claims != null && claims.containsKey("username")) {
                String username = claims.get("username").toString();
                String hash = claims.get("hash").toString();
                ApiToken user = (ApiToken) service.getValue(String.format("%s:%s", username,hash), ApiToken.class);
                if (user != null) {
                    AuthenticationTokenImpl auth = new AuthenticationTokenImpl(user.getUsername(), Collections.emptyList());
                    auth.setDetails(user);
                    auth.authenticate();
                    return auth;
                } else {
                    return new UsernamePasswordAuthenticationToken(null, null);
                }

            }
        }
        return null;
    }
}
