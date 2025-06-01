package org.yiqixue.kbbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Knowledge Base API");
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(status);
    }

    @GetMapping("/elasticsearch")
    public ResponseEntity<Map<String, String>> elasticsearchHealth() {
        // TODO: Implement actual Elasticsearch health check
        Map<String, String> status = new HashMap<>();
        status.put("elasticsearch", "UP");
        return ResponseEntity.ok(status);
    }
}
