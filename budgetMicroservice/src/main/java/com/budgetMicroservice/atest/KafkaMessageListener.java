package com.budgetMicroservice.atest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {
    @KafkaListener(topics = "topic", groupId = "budget_microservice_group")
    public void listen(String message) throws JsonProcessingException {
        System.out.println("Received raw message: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
            Fake fake = objectMapper.readValue(message, Fake.class);
            System.out.println("Converted to Fake: " + fake);
    }
}
