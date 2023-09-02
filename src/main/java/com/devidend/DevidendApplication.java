package com.devidend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevidendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevidendApplication.class, args);
    }

}
