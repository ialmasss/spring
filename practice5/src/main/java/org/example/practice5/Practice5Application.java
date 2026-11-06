package org.example.practice5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Practice5Application {

    public static void main(String[] args) {
        SpringApplication.run(Practice5Application.class, args);
    }

}