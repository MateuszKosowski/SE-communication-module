package com.kosowskinowak.communication.filter.authentication;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface AuthenticationStrategy {
    // Exchange to obiekt reprezentujący żądanie HTTP
    Mono<AuthenticationPrincipal> authenticate(ServerWebExchange exchange);
}
