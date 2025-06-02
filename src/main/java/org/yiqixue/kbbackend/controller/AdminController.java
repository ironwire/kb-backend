package org.yiqixue.kbbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> adminCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "You have admin access");
        return ResponseEntity.ok(response);
    }
}