package com.portnum.number;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NumberApplication {

  public static void main(String[] args) {
    SpringApplication.run(NumberApplication.class, args);
  }

}
