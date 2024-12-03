package com.portnum.number.global.service;


import com.portnum.number.RedisContainerSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml",
    properties = {
        "jasypt.encryptor.password=portNumber"
    }
)
public class RedisServiceRedis extends RedisContainerSupport {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        // 테스트 전에 Redis를 초기화
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @AfterEach
    void tearDown() {
        // 테스트 후 Redis 데이터를 정리
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void setValuesAndGetValues_정상_동작() {
        // given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofMinutes(10);

        // when
        redisService.setValues(key, value, duration);

        // then
        String result = redisService.getValues(key);
        assertThat(result).isEqualTo(value);
    }

    @Test
    void getValues_존재하지_않는_키() {
        // given
        String key = "nonExistingKey";

        // when
        String result = redisService.getValues(key);

        // then
        assertThat(result).isEqualTo("false");
    }

    @Test
    void deleteValues_정상_동작() {
        // given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofMinutes(10);

        redisService.setValues(key, value, duration);

        // when
        redisService.deleteValues(key);

        // then
        String result = redisService.getValues(key);
        assertThat(result).isEqualTo("false");
    }

    @Test
    void checkExistsValue_정상_동작() {
        // given
        String key = "testKey";
        String value = "testValue";
        Duration duration = Duration.ofMinutes(10);

        redisService.setValues(key, value, duration);

        // when
        boolean exists = redisService.checkExistsValue(redisService.getValues(key));

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void checkExistsValue_존재하지_않는_값() {
        // when
        boolean exists = redisService.checkExistsValue(redisService.getValues("nonExistingKey"));

        // then
        assertThat(exists).isFalse();
    }
}
