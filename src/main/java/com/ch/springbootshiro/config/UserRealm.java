package com.ch.springbootshiro.config;

import com.ch.springbootshiro.po.Permission;
import com.ch.springbootshiro.po.Role;
import com.ch.springbootshiro.po.User;
import com.ch.springbootshiro.po.UserRole;
import com.ch.springbootshiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties("cn.chang.shiro.user.realm")
public class UserRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(UserRealm.class);
    private static final String DEFAULT_PRINCIPAL_PREFIX = "shiro_principal1:";
    private static final String DEFAULT_PERMISSION_PREFIX = "shiro_premission1:";
    private static final String DEFAULT_ROLE_PREFIX = "shiro_role1:";
    private static final Integer DEFAULT_SESSION_TIMEOUT = 1800;
    @Autowired
    RedisTemplate redisTemplate;
    private String principalPrefix = DEFAULT_PRINCIPAL_PREFIX;
    private String permissionPrefix = DEFAULT_PERMISSION_PREFIX;
    private String rolePrefix = DEFAULT_ROLE_PREFIX;

    private Integer sessionTimeOut = DEFAULT_SESSION_TIMEOUT;

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
        User user = null;
        try {
            user = (User) redisTemplate.opsForValue().get(principalPrefix + username);
            logger.info("query redis user");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            try {
                redisTemplate.expire(principalPrefix + username, sessionTimeOut, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            user = userService.findUserByUserNameAndStatus(username, null);
            if (user != null) {
                try {
                    redisTemplate.opsForValue().set(principalPrefix + username, user, sessionTimeOut, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        SimpleAuthenticationInfo simpleAuthorizationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getPasswordSalt()), getName());
        return simpleAuthorizationInfo;
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();

        List<Role> roleList = null;
        try {
            roleList = (List<Role>) redisTemplate.opsForValue().get(rolePrefix + userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (roleList != null) {
            try {
                redisTemplate.expire(rolePrefix+userName,sessionTimeOut,TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            roleList = userService.findRoleListByUserName(userName);
            if (roleList != null) {
                try {
                    redisTemplate.opsForValue().set(rolePrefix + userName, roleList, sessionTimeOut, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roleSet = new HashSet<>();
        roleList.stream().forEach(currentRole -> {
            roleSet.add(currentRole.getRole());
        });
        simpleAuthorizationInfo.setRoles(roleSet);


        List<Permission> permissionList = null;
        try {
            permissionList = (List<Permission>) redisTemplate.opsForValue().get(permissionPrefix + userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (permissionList != null) {
            try {
                redisTemplate.expire(permissionPrefix+userName,sessionTimeOut,TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            permissionList = userService.findPermissionListByUserName(userName);
            if(!CollectionUtils.isEmpty(permissionList)){
                try {
                    redisTemplate.opsForValue().set(permissionPrefix+userName,permissionList,sessionTimeOut,TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Set<String> permissionSet = new HashSet<>();
        permissionList.stream().forEach(currentPermission -> {
            permissionSet.add(currentPermission.getPermission());
        });
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }


    public String getPrincipalPrefix() {
        return principalPrefix;
    }

    public void setPrincipalPrefix(String principalPrefix) {
        this.principalPrefix = principalPrefix;
    }

    public String getPermissionPrefix() {
        return permissionPrefix;
    }

    public void setPermissionPrefix(String permissionPrefix) {
        this.permissionPrefix = permissionPrefix;
    }

    public String getRolePrefix() {
        return rolePrefix;
    }

    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
