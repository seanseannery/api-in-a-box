package com.apiinabox.helloworld.service;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String getHelloWorld() {
        return "Hello, World!";
    }
} 