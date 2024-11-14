package com.paymentMicroservice.config;

import org.springframework.beans.factory.annotation.Value;

public class KafkaConsumerConfig {

    @Value("${kafka-bootstrap-servers}")
    private String bootstrapServers;

}
