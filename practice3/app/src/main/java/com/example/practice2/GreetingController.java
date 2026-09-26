package com.example.practice2;

import com.example.practice2.core.Greeter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final String messageFromValue;
    private final AppGreetingProperties properties;
    private final Greeter greeter;

    public GreetingController(@Value("${app.greeting.message}") String messageFromValue,
                              AppGreetingProperties properties,
                              Greeter greeter) {
        this.messageFromValue = messageFromValue;
        this.properties = properties;
        this.greeter = greeter;
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
        return greeter.buildGreeting("студент");
    }
}