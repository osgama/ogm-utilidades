package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private Environment environment;

    @GetMapping("/url")
    public String getApiUrl() {
        String apiUrl = environment.getProperty("api.url");
        return apiUrl;
    }
}
