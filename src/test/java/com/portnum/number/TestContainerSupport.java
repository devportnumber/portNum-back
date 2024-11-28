package com.portnum.number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class TestContainerSupport {

    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;

    private static final GenericContainer REDIS;

    static {
        REDIS = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);

        REDIS.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", () -> String.valueOf(REDIS.getMappedPort(REDIS_PORT)));
    }
}
