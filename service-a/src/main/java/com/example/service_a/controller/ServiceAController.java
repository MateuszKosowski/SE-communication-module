package com.example.service_a.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class ServiceAController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/status")
    public String getStatus() {
        return "Service A działa poprawnie!";
    }

    // http://localhost:3000/api/a/ask-b
    @GetMapping("/ask-b")
    public String askServiceB() {
        // Gateway (3000) -> /api/b  -> /inventory/items
        String gatewayUrl = "http://localhost:3000/api/b/inventory/items";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", "tajny_klucz_A_123");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                gatewayUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        return "Jestem Serwis A. Zapytałem B przez Gateway i dostałem: [" + response.getBody() + "]";
    }
}
