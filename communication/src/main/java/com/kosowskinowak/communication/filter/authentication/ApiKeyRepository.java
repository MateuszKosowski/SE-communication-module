package com.kosowskinowak.communication.filter.authentication;

import reactor.core.publisher.Mono;

public interface ApiKeyRepository {
    Mono<ApiKeyDetails> findByApiKey(String apiKey);
}
