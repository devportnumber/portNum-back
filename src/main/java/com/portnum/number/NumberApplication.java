package com.portnum.number;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
public class NumberApplication {

  static{
    System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true"); // 설정 추가
  }
  public static void main(String[] args) {
    SpringApplication.run(NumberApplication.class, args);
  }

}
