package com.apiinabox.helloworld.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1")
public interface HelloWorldApi {
    @GetMapping("/helloworld")
    ResponseEntity<String> getHelloWorld();
} 