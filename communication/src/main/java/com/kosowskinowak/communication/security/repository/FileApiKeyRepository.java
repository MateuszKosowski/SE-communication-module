package com.kosowskinowak.communication.security.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosowskinowak.communication.security.model.ApiKeyDetails;
import com.kosowskinowak.communication.security.model.ApiKeyEntryDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileApiKeyRepository implements ApiKeyRepository{

    @Value("classpath:api-keys.json")
    private Resource apiKeysResource;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, ApiKeyDetails> keyStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IOException {

        // 1. Wczytujemy JSON
        List<ApiKeyEntryDTO> entries = objectMapper.readValue(
                apiKeysResource.getInputStream(),
                new TypeReference<List<ApiKeyEntryDTO>>() {}
        );

        // 2. Przepisujemy do Mapy dla szybkiego wyszukiwania
        entries.forEach(entry -> {
            keyStore.put(entry.getKey(), new ApiKeyDetails(
                    entry.getServiceId(),
                    entry.getRoles()
            ));
        });

        System.out.println("--- FileApiKeyRepository: Wczytano " + keyStore.size() + " kluczy z pliku JSON ---");
    }

    @Override
    public Mono<ApiKeyDetails> findByApiKey(String apiKey) {
        return Mono.justOrEmpty(keyStore.get(apiKey));
    }


}
