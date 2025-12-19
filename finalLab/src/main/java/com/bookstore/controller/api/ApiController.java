package com.bookstore.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "API Info", description = "Информация об API")
public class ApiController {
    
    @GetMapping
    @Operation(summary = "Информация об API", description = "Возвращает информацию о доступных эндпоинтах")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Bookstore Management API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("description", "REST API для управления книжным магазином");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("books", "/api/books");
        endpoints.put("authors", "/api/authors");
        endpoints.put("publishers", "/api/publishers");
        endpoints.put("genres", "/api/genres");
        endpoints.put("orders", "/api/orders");
        endpoints.put("reviews", "/api/reviews");
        endpoints.put("warehouse", "/api/warehouse");
        endpoints.put("swagger-ui", "/swagger-ui.html");
        endpoints.put("api-docs", "/api-docs");
        
        apiInfo.put("endpoints", endpoints);
        
        return ResponseEntity.ok(apiInfo);
    }
}


