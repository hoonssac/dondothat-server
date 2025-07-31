package org.bbagisix.user.config;

import org.bbagisix.user.handler.CustomSuccessHandler;
import org.bbagisix.user.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final Environment environment;
	private final CustomSuccessHandler customSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// csrf disable
		http.csrf().disable();

		// Form 로그인 방식 disable
		http.formLogin().disable();

		// HTTP basic 인증 방식 disable
		http.httpBasic().disable();

		// 세션 설정 (OAuth2에서는 세션 필요)
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

		// 경로별 인가 작업 (OAuth2 경로 허용)
		http.authorizeRequests()
			.antMatchers("/", "/oauth2-login", "/oauth2-success", "/error", "/resources/**", 
						"/oauth2/**", "/login/oauth2/**", "/debug/**").permitAll()
			.anyRequest().authenticated();

		// OAuth2 로그인 설정 - 이것이 필터를 자동 등록해야 함
		http.oauth2Login()
			.clientRegistrationRepository(clientRegistrationRepository(environment))
			.userInfoEndpoint()
				.userService(customOAuth2UserService)
			.and()
			.successHandler(customSuccessHandler)
			.failureUrl("/oauth2-login?error");
	}

	@Bean
	public static ClientRegistrationRepository clientRegistrationRepository(Environment environment) {
		return new InMemoryClientRegistrationRepository(
			googleClientRegistration(environment),
			naverClientRegistration(environment)
		);
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	private static ClientRegistration googleClientRegistration(Environment environment) {
		return ClientRegistration.withRegistrationId("google")
			.clientId(environment.getProperty("GOOGLE_CLIENT_ID"))
			.clientSecret(environment.getProperty("GOOGLE_CLIENT_SECRET"))
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.scope("profile", "email")
			.authorizationUri("https://accounts.google.com/o/oauth2/auth")
			.tokenUri("https://oauth2.googleapis.com/token")
			.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
			.userNameAttributeName("sub")
			.clientName("Google")
			.build();
	}

	private static ClientRegistration naverClientRegistration(Environment environment) {
		return ClientRegistration.withRegistrationId("naver")
			.clientId(environment.getProperty("NAVER_CLIENT_ID"))
			.clientSecret(environment.getProperty("NAVER_CLIENT_SECRET"))
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
			.scope("name", "email")
			.authorizationUri("https://nid.naver.com/oauth2.0/authorize")
			.tokenUri("https://nid.naver.com/oauth2.0/token")
			.userInfoUri("https://openapi.naver.com/v1/nid/me")
			.userNameAttributeName("response")
			.clientName("Naver")
			.build();
	}
}
