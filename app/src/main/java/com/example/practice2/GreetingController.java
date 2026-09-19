package com.example.practice2;

import com.example.practice2.core.GreetingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Value("${app.greeting.message}")
    private String messageFromValue;

    private final AppGreetingProperties properties;
    private final GreetingService greetingService;

    public GreetingController(AppGreetingProperties properties) {
        this.properties = properties;
        this.greetingService = new GreetingService();
    }

    @GetMapping("/greeting-value")
    public String greetingViaValue() {
        return messageFromValue;
    }

    @GetMapping("/greeting-properties")
    public String greetingViaProperties() {
        return properties.getMessage();
    }

    @GetMapping("/greeting-core")
    public String greetingViaCore() {
        return greetingService.buildGreeting("student");
    }
}