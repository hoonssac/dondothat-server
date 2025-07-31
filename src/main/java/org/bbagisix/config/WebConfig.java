package org.bbagisix.config;

import org.bbagisix.oauth2.config.SecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
// web.xml을 대체하는 설정
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {

	final String LOCATION = "c:/upload";
	final long MAX_FILE_SIZE = 1024 * 1024 * 10L;
	final long MAX_REQUEST_SIZE = 1024 * 1024 * 20L;
	final int FILE_SIZE_THRESHOLD = 1024 * 1024 * 5;

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {ServletConfig.class};
	}

	// 스프링의 FrontController인 DispatcherServlet이 담당할 url 매핑 패턴, / : 모든 요청에 대한 매핑
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}

	// POST body 문자 인코딩 필터 설정 - UTF-8 설정
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		// Spring Security Filter
		org.springframework.web.filter.DelegatingFilterProxy securityFilter = 
			new org.springframework.web.filter.DelegatingFilterProxy("springSecurityFilterChain");
		
		return new Filter[] {characterEncodingFilter, securityFilter};
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		MultipartConfigElement multipartConfig =
			new MultipartConfigElement(
				LOCATION, // 업로드 처리 디렉토리 경로
				MAX_FILE_SIZE, // 업로드 가능한 파일 하나의 최대 크기
				MAX_REQUEST_SIZE, // 업로드 가능한 전체 최대 크기(여러 파일 업로드 하는 경우)
				FILE_SIZE_THRESHOLD // 메모리 파일의 최대 크기(이보다 작으면 실제 메모리에서만 작업)
			);
		registration.setMultipartConfig(multipartConfig);
	}
}
