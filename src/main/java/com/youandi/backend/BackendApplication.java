package com.youandi.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youandi.backend.auth.common.ApiTokenConfig;
import com.youandi.backend.auth.service.TokenAuthenticationService;
import com.youandi.backend.redis.RedisService;

@SpringBootApplication
@EnableAutoConfiguration
public class BackendApplication  extends SpringBootServletInitializer {

	@Autowired
	private RedisService redisService;
	
	@Bean
	public ApiTokenConfig getApiTokenConfig() {
		return new ApiTokenConfig();
	}

	@Bean
	public TokenAuthenticationService tokenAuthService() {
		return new TokenAuthenticationService(redisService, getApiTokenConfig());
	}
	
	

	@Bean
	public ObjectMapper mapper(){
		return new ObjectMapper();
	}
	
	
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BackendApplication.class);
    }


	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
