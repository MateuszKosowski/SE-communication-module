package com.kosowskinowak.communication.filter.authentication;

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
            System.out.println("ApiKey jest null lub pusty - zwracam Mono.empty()");
            return Mono.empty();
        }

        return apiKeyRepository.findByApiKey(apiKey)
                .map(details -> new AuthenticationPrincipal(
                        details.getServiceId(),
                        PrincipalType.SERVICE,
                        details.getRoles()
                ));
    }
}
