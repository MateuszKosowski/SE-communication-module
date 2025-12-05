package com.example.service_b.controller;


import com.example.service_b.dto.MessageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class ServiceBController {

    @GetMapping("/items")
    public String getItems() {
        return "Service B: Lista towarów [Item1, Item2, Item3]";
    }
}