package com.budgetMicroservice.atest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.data.domain.Pageable;

public class PageableSerializer implements Serializer<Pageable> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Pageable pageable) {
        try {
            return objectMapper.writeValueAsBytes(pageable);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Pageable object", e);
        }
    }
}