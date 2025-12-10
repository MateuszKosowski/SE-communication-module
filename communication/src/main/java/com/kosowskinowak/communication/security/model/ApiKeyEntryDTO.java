package com.kosowskinowak.communication.security.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiKeyEntryDTO {
    private String key;
    private String serviceId;
    private List<String> roles;
}