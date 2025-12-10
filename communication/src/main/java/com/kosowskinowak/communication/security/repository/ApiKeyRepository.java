package com.kosowskinowak.communication.security.repository;

import com.kosowskinowak.communication.security.model.ApiKeyDetails;
import reactor.core.publisher.Mono;

public interface ApiKeyRepository {
    Mono<ApiKeyDetails> findByApiKey(String apiKey);
}
