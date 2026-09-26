package com.example.practice2;

import com.example.practice2.core.FancyGreetingService;
import com.example.practice2.core.GreetingService;
import com.example.practice2.core.Greeter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GreetingConfig {

    @Bean
    @ConditionalOnProperty(name = "app.greeting.fancy", havingValue = "true")
    public Greeter fancyGreeter() {
        return new FancyGreetingService();
    }

    @Bean
    @ConditionalOnProperty(name = "app.greeting.fancy", havingValue = "false", matchIfMissing = true)
    public Greeter simpleGreeter() {
        return new GreetingService();
    }
}