package com.demo.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;



public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer {

	// DispatcherServlet에 매핑할 요청 주소를 셋팅한다.
	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	// Spring MVC 프로젝트 설정을 위한 클래스를 지정한다.
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] {ServletAppContext.class};
	}
	
	// 프로젝트에서 사용할 Bean들을 정의기 위한 클래스를 지정한다.
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootAppContext.class};
	}
	
	// 파라미터 인코딩 필터 설정
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		return new Filter[] {encodingFilter};
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		super.customizeRegistration(registration);

		//50MB = 5242880;
		/**location - 파일 업로드 시 임시로 저장하는 절대 경로이다
		 	디폴트 값 : null 일때 톰캣의 지정된 경로
		 maxFileSize - 파일당 최대 파일 크기이다
		 	디폴트 값 : 제한없음
		 maxRequestSize - 파일 한 개의 용량이 아니라 multipart/form-data 요청당 최대 파일 크기이다 (여러 파일 업로드 시 총 크기로 보면 된다)
		 	디폴트 값: 제한없음
		 fileSizeThreshold - 업로드하는 파일이 임시로 파일로 저장되지 않고 메모리에서 바로 스트림으로 전달되는 크기의 한계를 나타낸다
		 	디폴트 값: 0
		 * */
		MultipartConfigElement config1 = new MultipartConfigElement(null,5242880, 52428800, 0);
		registration.setMultipartConfig(config1);
	}
}


