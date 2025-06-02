package org.yiqixue.kbbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> testAuth(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Authentication successful");
        response.put("username", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        
        return ResponseEntity.ok(response);
    }
}