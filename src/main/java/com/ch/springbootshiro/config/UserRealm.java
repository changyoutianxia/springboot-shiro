package com.ch.springbootshiro.config;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.ch.springbootshiro.po.Permission;
import com.ch.springbootshiro.po.Role;
import com.ch.springbootshiro.po.User;
import com.ch.springbootshiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class UserRealm extends AuthorizingRealm {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public String getName() {
        return "customJdbcRealm";
    }

    @Autowired
    UserService userService;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findUserByUserNameAndStatus(username, null);
        SimpleAuthenticationInfo simpleAuthorizationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getPasswordSalt()), getName());
        return simpleAuthorizationInfo;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        List<Role> roleList = userService.findRoleListByUserName(userName);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<>();
        roleList.stream().forEach(currentRole -> {
            roleSet.add(currentRole.getRole());
        });
        simpleAuthorizationInfo.setRoles(roleSet);
        List<Permission> permissionList = userService.findPermissionListByUserName(userName);
        Set<String> permissionSet= new HashSet<>();
        permissionList.stream().forEach(currentPermission->{permissionSet.add(currentPermission.getPermission());});
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }


}
