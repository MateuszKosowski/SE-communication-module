package com.kosowskinowak.communication.security.strategy;

import com.kosowskinowak.communication.security.model.AuthenticationPrincipal;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthenticationStrategy {
    // Exchange to obiekt reprezentujący żądanie HTTP
    Mono<AuthenticationPrincipal> authenticate(ServerWebExchange exchange);
}
