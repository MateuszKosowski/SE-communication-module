package com.kosowskinowak.communication.filter;

import com.kosowskinowak.communication.filter.authentication.AuthenticationStrategy;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final List<AuthenticationStrategy> strategies;

    public AuthenticationFilter(List<AuthenticationStrategy> strategies) {
        super(Config.class);
        this.strategies = new ArrayList<>(strategies);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            System.out.println("--- [1] AuthenticationFilter: JWT lub ApiKey ---");

            return Flux.fromIterable(strategies) // Reaktywny strumień - kolejkuje strategie z listy do wykonania
                    .concatMap(strategy -> strategy.authenticate(exchange)) // Dla każdej strategii wykonaj metode z przekazanym obiektem żądania HTTP
                    .next() // Jeśli będzie pozytywna odpowiedź (inna niż Mono.empty) to zatrzymaj resztę LUB czekaj aż coś wreszcie będzie OK
                    .flatMap(principal -> { // Blok sukcesu wykonywany jak next() coś znalazło - to znalezione to principal
                        System.out.println("--- [1] AuthFilter: Zalogowano (" + principal.getType() + "): " + principal.getId());

                        exchange.getAttributes().put("auth_principal", principal);
                        return chain.filter(exchange);
                    })
                    .switchIfEmpty(Mono.defer(() -> { // Blok porażki
                        System.out.println("--- [1] AuthFilter: Brak pasującej strategii lub błąd danych -> 401 ---");
                        return onError(exchange, HttpStatus.UNAUTHORIZED);
                    }));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}