package com.kosowskinowak.communication.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    public AuthorizationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            System.out.println("--- [2] AuthorizationFilter: TU KIEDYŚ SPRAWDZĘ UPRAWNIENIA ---");

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}