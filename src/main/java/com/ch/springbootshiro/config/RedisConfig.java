package com.ch.springbootshiro.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setClusterNodes(getNodes(redisProperties.getCluster().getNodes()));
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            redisClusterConfiguration.setPassword(redisProperties.getPassword());
        }
        if (!StringUtils.isEmpty(redisProperties.getCluster().getMaxRedirects())) {
            redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        }
        return redisClusterConfiguration;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public LettuceClientConfiguration lettucePoolingClientConfiguration() {
        LettucePoolingClientConfiguration build = LettucePoolingClientConfiguration.builder().build();

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());

        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());

        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());

        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(15))
                .poolConfig(poolConfig)
                .shutdownTimeout(Duration.ZERO)
                .build();
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration(), lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        return lettuceClientConfiguration;
    }

    private List<RedisNode> getNodes(List<String> nodes) {
        List<RedisNode> redisNodes = new ArrayList<>();
        for (String address : nodes) {
            RedisNode redisNode = new RedisNode(address.split(":")[0], Integer.valueOf(address.split(":")[1]));
            redisNodes.add(redisNode);
        }
        return redisNodes;
    }

}
