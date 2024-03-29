package com.demo.config;

import com.demo.beans.BoardInfoBean;
import com.demo.beans.LoginUserBean;
import com.demo.interceptor.CheckLoginInterceptor;
import com.demo.interceptor.CheckWriterInterceptor;
import com.demo.interceptor.MenuInterceptor;
import com.demo.mapper.BoardMapper;
import com.demo.mapper.MenuMapper;
import com.demo.mapper.UserMapper;
import com.demo.service.BoardService;
import com.demo.service.MenuService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

//Spring MVC 관련된 설정을 하는 클래스
@Configuration
//Controller 어노테이션이 셋팅되어 있는 클래스를 Controller로 등록한다.
@EnableWebMvc
//스캔할 패키지를 지정한다.
@PropertySource("/WEB-INF/properties/db.properties")
@ComponentScan("com.demo.controller")
@ComponentScan("com.demo.service")
@ComponentScan("com.demo.beans")
public class ServletAppContext implements WebMvcConfigurer {

	@Value("${db.classname}")
	private String db_classname;

	@Value("${db.url}")
	private String db_url;

	@Value("${db.username}")
	private String db_username;

	@Value("${db.password}")
	private String db_password;





	// 로그인 정보 관리
	@Resource(name = "loginUserBean")
	private LoginUserBean loginUserBean;

	@Autowired
	private  MenuService menuService;

	@Autowired
	private BoardService boardService;


//	private final MenuService menuService;
//
//	private final BoardService boardService;
//
//	@Autowired
//	public ServletAppContext(MenuService menuService, BoardService boardService) {
//		this.menuService = menuService;
//		this.boardService = boardService;
//	}

	// Controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정한다.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	// 정적 파일의 경로를 매핑한다.
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}

	// 데이터베이스 접속 정보 관리
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);

		return source;
	}

	// 쿼리문과 접속 관리하는 객체
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception{
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}




	// 쿼리문 실행을 위한 매퍼 객체 목록
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<MenuMapper> getMenuMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<MenuMapper> factoryBean = new MapperFactoryBean<MenuMapper>(MenuMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}

	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}



	/// intercept 설정 및 추가 .
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		// 1.  메뉴 서비스 목록의 경우 모든 요청에 대해서 필요함..
		MenuInterceptor menuInterceptor = new MenuInterceptor(menuService,loginUserBean);
		InterceptorRegistration reg1 = registry.addInterceptor(menuInterceptor);
		reg1.addPathPatterns("/**"); //모든 요청

		// 2. 로그인 검사 로직 (비로그인사용자 차단수행)
		CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserBean);
		InterceptorRegistration reg2 = registry.addInterceptor(checkLoginInterceptor);
		// 차단하는 url
		reg2.addPathPatterns("/user/modify", "/user/logout", "/board/*");
		// 허용하는 url
		reg2.excludePathPatterns("/board/main","/board/read");


		// 3. 로그인사용자별 수정 및 삭제 권한 추가
		CheckWriterInterceptor checkWriterInterceptor = new CheckWriterInterceptor(loginUserBean, boardService);
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriterInterceptor);

		// 허용
		reg3.addPathPatterns("/board/modify", "/board/delete");

	}

	///  이미지 업로드 관련 BEAN
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

}
