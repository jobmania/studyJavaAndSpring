package com.demo.main;

import com.demo.beans.TestBean1;
import com.demo.beans.TestBean2;
import com.demo.beans.TestBean3;
import com.demo.config.BeanConfigClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {

	// 로그 객체 선언(사용시 사용하는 클래스의 이름을 괄호안에 입력)
	private static final Logger log = LoggerFactory.getLogger(MainClass.class);

	public static void main(String[] args) {
		log.info("시작");
		// beans.xml 파일을 로딩한다.
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(BeanConfigClass.class);

		System.out.println("=====시작========");

		TestBean1 java1 = ctx.getBean("java1", TestBean1.class);
		log.info(java1.toString());

		System.out.println("==============================================");

		TestBean1 java600 = ctx.getBean("java600", TestBean1.class);
		log.info(java600.toString());

		System.out.println("==============================================");

		TestBean2 java2 = ctx.getBean("java2", TestBean2.class);
		log.info(java2.toString());

		System.out.println("==============================================");


		TestBean3 java3 = ctx.getBean("java3", TestBean3.class);
		log.info(java3.toString());
		System.out.println("==============================================");
		TestBean3 java3_ver2 = ctx.getBean("java3", TestBean3.class);
		log.info(java3_ver2.toString());


		ctx.close();
		log.info("끝");
	}

}
