package org.bbagisix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// csrf disable
		http.csrf().disable();

		// Form 로그인 방식 disable
		http.formLogin().disable();

		// HTTP basic 인증 방식 disable
		http.httpBasic().disable();

		// oauth2
		http.oauth2Login()
			.defaultSuccessUrl("/")  // 로그인 성공 시 리다이렉트 URL
			.failureUrl("/login?error");  // 로그인 실패 시 리다이렉트 URL

		// 경로별 인가 작업
		http.authorizeRequests()
			.antMatchers("/", "/login", "/error", "/resources/**").permitAll()
			.anyRequest().authenticated();

		// 세션 설정 : STATELESS
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
