package com.example.sis2;

import org.springframework.boot.SpringApplication;

public class TestSis2Application {

    public static void main(String[] args) {
        SpringApplication.from(Sis2Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
