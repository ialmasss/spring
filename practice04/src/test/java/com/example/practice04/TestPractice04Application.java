package com.example.practice04;

import org.springframework.boot.SpringApplication;

public class TestPractice04Application {

    public static void main(String[] args) {
        SpringApplication.from(Practice04Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
