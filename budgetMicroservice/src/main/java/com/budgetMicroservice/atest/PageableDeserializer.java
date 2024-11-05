package com.budgetMicroservice.atest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.data.domain.Pageable;

public class PageableDeserializer implements Deserializer<Pageable> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Pageable deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Pageable.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing Pageable object", e);
        }
    }
}
