package com.example.practice2.core;

public class FancyGreetingService implements Greeter {

    @Override
    public String buildGreeting(String name) {
        return "welcome, " + name + "! this is the fancy version!";
    }
}