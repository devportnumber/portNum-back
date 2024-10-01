package com.portnum.number.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

    // serializer 설정으로 redis-cli를 통해 직접 데이터를 조회할 수 있도록 함
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

    @Bean("popupCacheManager")
    public CacheManager popupCacheManager(){
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .cacheDefaults(defaultConfiguration())
                .withInitialCacheConfigurations(configureMap())
                .build();
    }

    private Map<String, RedisCacheConfiguration> configureMap(){
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();
        cacheConfigurationMap.put("getRedisWithCacheManager", defaultConfiguration().entryTtl(Duration.ofHours(1)));
        return cacheConfigurationMap;
    }

    private RedisCacheConfiguration defaultConfiguration(){
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(customGenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(10));
    }

    private GenericJackson2JsonRedisSerializer customGenericJackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule to handle LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure dates are serialized as ISO-8601 strings
        objectMapper.activateDefaultTyping(BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build(), ObjectMapper.DefaultTyping.EVERYTHING);

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}

