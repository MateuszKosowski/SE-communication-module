package com.example.service_a.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/ask-b")
    public String askServiceB() {
        // Gateway (3000) -> /api/b  -> /inventory/items
        String gatewayUrl = "http://localhost:3000/api/b/inventory/items";

        String responseFromB = restTemplate.getForObject(gatewayUrl, String.class);

        return "Jestem Serwis A. Zapytałem B przez Gateway i dostałem: [" + responseFromB + "]";
    }
}
