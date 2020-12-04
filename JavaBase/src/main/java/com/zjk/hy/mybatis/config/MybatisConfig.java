package com.zjk.hy.mybatis.config;

import com.zjk.hy.mybatis.service.UserService;
import com.zjk.hy.mybatis.service.impl.UserServiceImpl;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@ComponentScan(value = "com.zjk.hy.mybatis")
@Configuration
@MapperScan("com.zjk.hy.mybatis.dao")
public class MybatisConfig {
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/s-mybatis?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        /*org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Log4jImpl.class);
        factoryBean.setConfiguration(configuration);*/
        return factoryBean.getObject();
    }


    public static void main(String[] args) {
        // org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
        // org.apache.ibatis.logging.LogFactory.useSlf4jLogging();
        // org.apache.ibatis.logging.LogFactory.useCommonsLogging();
        // org.apache.ibatis.logging.LogFactory.useJdkLogging();
        // org.apache.ibatis.logging.LogFactory.useLog4JLogging();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MybatisConfig.class);
        UserServiceImpl bean = context.getBean(UserServiceImpl.class);
        System.out.println(bean.findList());
        System.out.println(bean.findList());
        System.out.println(bean.findList());
    }
}
