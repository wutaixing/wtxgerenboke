package com.wtx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WtxBootCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WtxBootCodeApplication.class, args);
    }

}
