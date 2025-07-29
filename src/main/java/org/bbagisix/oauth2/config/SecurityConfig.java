package org.bbagisix.oauth2.config;

import lombok.RequiredArgsConstructor;
import org.bbagisix.oauth2.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;

	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
		this.customOAuth2UserService = customOAuth2UserService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// csrf disable
		http.csrf().disable();

		// Form 로그인 방식 disable
		http.formLogin().disable();

		// HTTP basic 인증 방식 disable
		http.httpBasic().disable();

		// oauth2
		http.oauth2Login((oauth2) -> oauth2
			.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
				.userService(customOAuth2UserService)));

		// 경로별 인가 작업
		http.authorizeRequests()
			.antMatchers("/", "/login", "/error", "/resources/**", "/oauth2/**", "/login/oauth2/**").permitAll()
			.anyRequest().authenticated();

		// 세션 설정 : STATELESS
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
