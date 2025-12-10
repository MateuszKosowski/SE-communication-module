package com.kosowskinowak.communication.filter;

import com.kosowskinowak.communication.security.strategy.AuthenticationStrategy;
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



            // Sprawdzamy czy ten obiekt exchange już przeszedł przez autentykację
            if (exchange.getAttributes().containsKey("auth_principal")) {
                System.out.println("--- [1] AuthFilter: Już zautentykowane - przepuszczam ---");
                return chain.filter(exchange).doFinally(signalType -> {
                    System.out.println("--- [1] AuthFilter: Zakończono przetwarzanie (cached auth) - Signal: " + signalType);
                });
            }

            return Flux.fromIterable(strategies) // Reaktywny strumień - kolejkuje strategie z listy do wykonania
                    .concatMap(strategy -> strategy.authenticate(exchange)) // Dla każdej strategii wykonaj metode z przekazanym obiektem żądania HTTP
                    .next() // Jeśli będzie pozytywna odpowiedź (inna niż Mono.empty) to zatrzymaj resztę LUB czekaj aż coś wreszcie będzie OK
                    .switchIfEmpty(Mono.defer(() -> { // Blok porażki - MUSI być przed flatMap
                        System.out.println("--- [1] AuthFilter: Brak pasującej strategii lub błąd danych -> 401 ---");
                        return Mono.error(new RuntimeException("Authentication failed"));
                    }))
                    .flatMap(principal -> { // Blok sukcesu wykonywany jak next() coś znalazło - to znalezione to principal
                        System.out.println("--- [1] AuthFilter: Zalogowano (" + principal.getType() + "): " + principal.getId());

                        exchange.getAttributes().put("auth_principal", principal);
                        return chain.filter(exchange).doFinally(signalType -> {
                            System.out.println("--- [1] AuthFilter: Zakończono przetwarzanie (new auth) - Signal: " + signalType + ", Exchange: " + exchange.getRequest().getId());
                        });
                    })
                    .onErrorResume(error -> {
                        System.out.println("--- [1] AuthFilter: Obsługa błędu: " + error.getMessage());
                        return onError(exchange, HttpStatus.UNAUTHORIZED);
                    });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}