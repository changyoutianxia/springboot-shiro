package com.ch.springbootshiro.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {
    @ConfigurationProperties("spring.datasource.druid")
    @Bean
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean  = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        /**
         * 白名单
         * 下面属性来自于
         * com.alibaba.druid.support.http.ResourceServlet
        * */
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        /**
         * 拒绝访问
         *
        * */
        //servletRegistrationBean.addInitParameter("deny","127.0.0.2");
        servletRegistrationBean.addInitParameter("loginUsername","root");
        servletRegistrationBean.addInitParameter("loginPassword","suifeng");
        return servletRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/**");
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,*.eot,*.svg,*.ttf,*.woff,*.woff2,/druid/**");
        return filterRegistrationBean;
    }
}
