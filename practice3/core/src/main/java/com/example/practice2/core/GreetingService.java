package com.example.practice2.core;

public class GreetingService implements Greeter {

    @Override
    public String buildGreeting(String name) {
        return "hello, " + name + "! this is the version from module core.";
    }
}