package com.example.service_b.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String sender;
    private String content;
}