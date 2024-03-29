package com.demo.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@ComponentScan(basePackages = {"com.demo.beans","com.demo.db"})
@Configuration
public class BeanConfigClass {

    @Bean
    public BasicDataSource source() {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("oracle.jdbc.OracleDriver");
        source.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        source.setUsername("scott");
        source.setPassword("1234");

        return source;
    }


    // 데이터베이스에 접속해서 쿼릴르 전달하는 빈 등록..
    @Bean
    public JdbcTemplate db(BasicDataSource source) {
        JdbcTemplate db = new JdbcTemplate(source);
        return db;
    }





}
