package com.devsco.moongch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MoongchApplication {
  public static void main(String[] args) {
    SpringApplication.run(MoongchApplication.class, args);
    System.out.println("hello");
  }
}
