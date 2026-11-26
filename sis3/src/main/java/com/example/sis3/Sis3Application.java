package com.example.sis3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableCaching
@EnableRetry
public class Sis3Application {

    public static void main(String[] args) {
        SpringApplication.run(Sis3Application.class, args);
    }
}