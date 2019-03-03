package com.ch.springbootshiro.description;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationDescription {
    public static Logger logger = LoggerFactory.getLogger(AuthenticationDescription.class);

    public static void main(String[] args) {
        /*获取构造器*/
        Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
        /*获取SecurityManager 实例*/
        SecurityManager securityManager = securityManagerFactory.getInstance();
        /*设置SecurityManager*/
        SecurityUtils.setSecurityManager(securityManager);
        /*
         *       获取当前线程的subject
         *         Thread t = Thread.currentThread();
         *         ThreadLocalMap map = getMap(t);
         *         使用的是ThreadLocalMap 保持同一线程是一个subject
         * */
        Subject subject = SecurityUtils.getSubject();
        /*
         * 创建用户信息
         *  (implements)HostAuthenticationToken ->(extends)AuthenticationToken
         *   返回主机Ip address
         *
         * Returns the host name of the client from where the
         * authentication attempt originates or if the Shiro environment cannot or
         * chooses not to resolve the hostname to improve performance, this method
         * returns the String representation of the client's IP address.
         * <p/>
         * When used in web environments, this value is usually the same as the
         * {@code ServletRequest.getRemoteHost()} value.
         *
         * @return the fully qualified name of the client from where the
         *         authentication attempt originates or the String representation
         *         of the client's IP address is hostname resolution is not
         *         available or disabled.
         *
         *  String getHost ();
         *
         *
         *(implements)RememberMeAuthenticationToken -> (extends)AuthenticationToken
         *  返回是否主=subject 选择接住我
         *  boolean isRememberMe();
         *
         * UsernamePasswordToken
         *   返回身份信息就是用户名
         *   Object getPrincipal() {return getUsername();}
         *   返回凭证信息就是password
         *  Object getCredentials(){return getPassword();}
         *   密码使用的char[]数组存放
         *   private char[] password;
         * */
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("zhangsan", "123");
        /*登陆
         *
         * DelegatingSubject implements Subject
         * login(AuthenticationToken authenticationToken)
         *   securityManager.login(this, token);
         *     默认实现类DefaultSecurityManager
         *       Subject login(Subject subject, AuthenticationToken token)
         *           info = authenticate(token);
         *               this.authenticator.authenticate(token);
         *               Authenticator 默认实现类
         *               ModularRealmAuthenticator extends AbstractAuthenticator
         *                       AuthenticationInfo authenticate(AuthenticationToken token)
         *                           info = doAuthenticate(token); 抽象接口有子实现类实现
         *                           AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken)
         *                                if (realms.size() == 1) {
         *                                      return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
         *                                                  AuthenticationInfo info = realm.getAuthenticationInfo(token);
         *                                 } else {
         *                                  return doMultiRealmAuthentication(realms, authenticationToken)
         *                                          AuthenticationStrategy strategy = getAuthenticationStrategy();
         *                                          通过调用策略来完成real的调用 最终实现登陆
         *                                  };
         *
         *
         *
         *
         * */

        subject.login(usernamePasswordToken);
        /*
         *查看是否登陆成功
         * 如果验证失败会抛出异常
         * AuthenticationException>(extends)ShiroException>(extends)RunTimeException
         *
         *   CredentialsException>(extends)AuthenticationException
         *       ExpiredCredentialsException   凭证过期
         *       IncorrectCredentialsException 凭证不正确
         *
         *  AccountException>(extends)AuthenticationException
         *       DisabledAccountException 不可用的账户
         *       ExcessiveAttemptsException    超过尝试次数
         *       ConcurrentAccessException 已经登陆的用户去验证
         *       LockedAccountException    账户被锁定
         *       UnknownAccountException   不存在的用户名字
         * AuthenticationException
         *       UnsupportedTokenException 不支持的Token
         * */
        if (subject.isAuthenticated()) {
            logger.info("登陆成功！");
        } else {
            logger.debug("登陆失败！");
        }


    }
}
