package com.youandi.backend.auth.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="api.url")
@Data
public class AuthorizeRequestUrl {
	private String logIn;
	private String logOut;
	
}
