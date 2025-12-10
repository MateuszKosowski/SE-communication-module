package com.kosowskinowak.communication.filter.authentication;

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
