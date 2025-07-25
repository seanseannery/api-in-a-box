package com.apiinabox.helloworld.controller;

import com.apiinabox.helloworld.api.HelloWorldApi;
import com.apiinabox.helloworld.service.HelloWorldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class HelloWorldController implements HelloWorldApi {
    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @Override
    public ResponseEntity<String> getHelloWorld() {
        return ResponseEntity.ok(helloWorldService.getHelloWorld());
    }
} 