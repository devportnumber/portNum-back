package com.portnum.number.global.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    public void setValues(String key, String value, Duration duration){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (values.get(key) == null) ? "false" : (String) values.get(key);
    }

    public void deleteValues(String key){
        redisTemplate.delete(key);
    }


    public boolean checkExistsValue(String value){
        return !value.equals("false");
    }

}
