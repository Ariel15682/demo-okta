package com.example.demookta.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class WebSecurityConfig {

	@Bean
	protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatchers(security ->
				security
					.requestMatchers("/**"))
			.authorizeHttpRequests((authorize) ->
				authorize
					.requestMatchers("/", "/login**").permitAll()
					.anyRequest().authenticated())
			.oauth2Login(Customizer.withDefaults());
		return http.build();
	}

}
