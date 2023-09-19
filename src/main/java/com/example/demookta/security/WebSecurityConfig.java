package com.example.demookta.security;

import com.okta.commons.lang.Assert;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.net.URI;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

//	@Autowired
//	ClientRegistrationRepository clientRegistrationRepository;
	private final ClientRegistrationRepository clientRegistrationRepository;

	public WebSecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
		Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler successHandler =
				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri("http://localhost:8080/"); //(URI.create("http://localhost:8080/"));
		return successHandler;
	}


	@Bean
	protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatchers(security -> security.requestMatchers("/**"))
			.authorizeHttpRequests((authorize) ->
				authorize
					.requestMatchers("/").permitAll()
					.anyRequest().authenticated())
//			.exceptionHandling(exception ->
//				exception
//  				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN)))
			.logout(logout ->
				logout
					.logoutSuccessUrl("/")
					.logoutSuccessHandler(oidcLogoutSuccessHandler()))
			.oauth2Login(Customizer.withDefaults())
		 	.oauth2ResourceServer((oauth2) -> oauth2
				.jwt(Customizer.withDefaults()));
		return http.build();
	}

}

//@Bean
//        protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
//            http
//                    .authorizeHttpRequests(auth ->
//                            auth
//                                // allow anonymous access to the root page
//                                .requestMatchers("/").permitAll()
//                                // all other requests
//                                .anyRequest().authenticated())
//                                // After we logout, redirect to root page, by default Spring will send you to /login?logout
//                    .logout(logout ->
//                            logout
//                                .logoutSuccessUrl("/")
//                                // RP-initiated logout
//                                .logoutSuccessHandler(oidcLogoutSuccessHandler()))
//
//                    // enable OAuth2/OIDC
//                    .oauth2Login(Customizer.withDefaults());
//            return http.build();
//        }