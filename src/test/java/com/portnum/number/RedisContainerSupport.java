package com.portnum.number;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class RedisContainerSupport {

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
