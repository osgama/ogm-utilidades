package com.example.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiPassword {

    @GetMapping("/api/{nickname}")
    public String getExito(@PathVariable String parametro) {
        String password = "";
        return password;
    }
    
}
