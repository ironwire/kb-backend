package org.yiqixue.kbbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> publicTest() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "This is a public endpoint");
        
        return ResponseEntity.ok(response);
    }
}