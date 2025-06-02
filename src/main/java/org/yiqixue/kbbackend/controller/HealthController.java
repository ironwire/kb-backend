package org.yiqixue.kbbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yiqixue.kbbackend.service.ElasticsearchService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    private final ElasticsearchService elasticsearchService;

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
        Map<String, String> status = new HashMap<>();
        try {
            boolean isElasticsearchUp = elasticsearchService.isElasticsearchUp();
            status.put("elasticsearch", isElasticsearchUp ? "UP" : "DOWN");
            status.put("timestamp", String.valueOf(System.currentTimeMillis()));
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            status.put("elasticsearch", "DOWN");
            status.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(status);
        }
    }
}