package com.ch.springbootshiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ShiroConfig {

    /**
     * 貌似用于bean 的初始化和销毁
     * */
    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /*
    * 下面两个用于开启注解
    * */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor= new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /**
         * 使用cglib 进行代理
         * */
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    /**
     * 创建拦截器
     * @param defaultWebSecurityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        Map urlMap = new ConcurrentHashMap();
        /**
         *  需要认证
         *  anon    匿名
         *  authc   需要登陆
         *  user    如果使用了rememberMe的功能可以直接访问
         *  perms   该资源必须得到权限才能访问
         *  role   该资源必须得到角色权限
         * */
        urlMap.put("/toLogin","anon");
        urlMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(urlMap);
        return shiroFilterFactoryBean;
    }
    /**
     *创建WebSecurityManager
     * */
    @Bean
    public DefaultWebSecurityManager securityManager(UserRealm userRealm, HashedCredentialsMatcher hashedCredentialsMatcher, ShiroSessionRedisDao shiroSessionRedisDao) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();;
        sessionManager.setSessionDAO(shiroSessionRedisDao);

        securityManager.setSessionManager(sessionManager);
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return securityManager;
    }

    /**
     * 创建 加密算法
     * @return
     */
    @Bean
    public HashedCredentialsMatcher getHashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        /**
         * 设置加密的算法
        * */
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-1");
        /**
         * 迭代次数
         * */
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }
}
