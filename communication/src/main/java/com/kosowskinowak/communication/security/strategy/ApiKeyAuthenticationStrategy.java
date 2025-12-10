package com.kosowskinowak.communication.security.strategy;

import com.kosowskinowak.communication.security.model.AuthenticationPrincipal;
import com.kosowskinowak.communication.security.model.PrincipalType;
import com.kosowskinowak.communication.security.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationStrategy implements AuthenticationStrategy{

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final ApiKeyRepository apiKeyRepository;

    @Override
    public Mono<AuthenticationPrincipal> authenticate(ServerWebExchange exchange) {

        String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);

        if (apiKey == null || apiKey.isEmpty()) {
            System.out.println("    [ApiKeyStrategy] ApiKey jest null lub pusty - zwracamy Mono.empty()");
            return Mono.empty();
        }

        return apiKeyRepository.findByApiKey(apiKey)
                .doOnNext(details -> System.out.println("    [ApiKeyStrategy] Znaleziono klucz dla: " + details.getServiceId()))
                .map(details -> new AuthenticationPrincipal(
                        details.getServiceId(),
                        PrincipalType.SERVICE,
                        details.getRoles()
                ))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("    [ApiKeyStrategy] Klucz NIE ZNALEZIONY w bazie: " + apiKey);
                    return Mono.empty();
                }));
    }
}
