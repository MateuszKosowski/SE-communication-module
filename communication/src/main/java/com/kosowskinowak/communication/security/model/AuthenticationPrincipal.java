package com.kosowskinowak.communication.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticationPrincipal {
    private String id;
    private PrincipalType type;
    private List<String> roles;
}
