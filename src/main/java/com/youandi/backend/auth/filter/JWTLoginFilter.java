package com.youandi.backend.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.youandi.backend.auth.common.ApiTokenConfig;
import com.youandi.backend.auth.service.TokenAuthenticationService;
import com.youandi.backend.domain.AuthenticationTokenImpl;


public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager, TokenAuthenticationService service) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
        tokenAuthenticationService = service;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse hsr1) throws AuthenticationException, IOException, ServletException {
//        ApiToken credentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(), ApiToken.class);
//		  UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
//        return getAuthenticationManager().authenticate(token);
        
    	String sid = httpServletRequest.getHeader(ApiTokenConfig.SID_HEADER_NAME);
    	
    	System.out.println("value : " + sid);
    	
    	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(sid, sid);
    
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        
        AuthenticationTokenImpl auth = (AuthenticationTokenImpl) authentication;
        tokenAuthenticationService.addAuthentication(response, auth); 
    }
    
//    private String extract(String header) {
//        if (StringUtils.isBlank(header)) {
//            throw new AuthenticationServiceException("Authorization header cannot be blank!");
//        }
//
//        if (header.length() <= ApiTokenConfig.SID_HEADER_NAME.length()) {
//            throw new AuthenticationServiceException("Invalid authorization header size.");
//        }
//        return header.substring(ApiTokenConfig.SID_HEADER_NAME.length(), header.length());
//    }
    
   
    
}
