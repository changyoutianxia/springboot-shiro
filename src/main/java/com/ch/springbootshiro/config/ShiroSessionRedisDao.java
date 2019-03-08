package com.ch.springbootshiro.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

@Configuration
@ConfigurationProperties("cn.chang.shiro.session.redis")
public class ShiroSessionRedisDao extends EnterpriseCacheSessionDAO {
    private static Logger logger = LoggerFactory.getLogger(ShiroSessionRedisDao.class);
    private static final String DEFAULT_PREFIX = "shiro_session:";
    private String shiroSessionPrefix = DEFAULT_PREFIX;
    @Autowired
    private RedisTemplate redisTemplate;

    public ShiroSessionRedisDao() {
        super();
        System.out.println(shiroSessionPrefix);
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable serializable = super.doCreate(session);
        String redisSessionId = shiroSessionPrefix + session.getId();
        try {
            redisTemplate.opsForValue().set(redisSessionId, session,session.getTimeout());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis set seesion error");
            return null;
        }
        return redisSessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = null;
        try {
            session = (Session) redisTemplate.opsForValue().get(sessionId);
            if(session !=null){
                redisTemplate.opsForValue().set(session.getId(),session,session.getTimeout());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return session;
        }
        return session;

    }
    @Override
    protected void doUpdate(Session session) {
        try {
            redisTemplate.opsForValue().set(shiroSessionPrefix+session.getId(), session,session.getTimeout());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(Session session) {
        try {
            redisTemplate.delete(shiroSessionPrefix+session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getShiroSessionPrefix() {
        return shiroSessionPrefix;
    }

    public void setShiroSessionPrefix(String shiroSessionPrefix) {
        this.shiroSessionPrefix = shiroSessionPrefix;
    }


}
