package com.kosowskinowak.communication.filter.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiKeyDetails {
    private String serviceId;
    List<String> roles;
}
