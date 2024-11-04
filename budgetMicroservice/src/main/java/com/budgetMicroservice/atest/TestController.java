package com.budgetMicroservice.atest;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final KafkaTemplate<String, Fake> kafkaMovementTemplate;

    @GetMapping("/send")
    public String sendTestMessage() {
        Fake fake = new Fake();
        fake.setName("Kafka");
        kafkaMovementTemplate.send("topic", fake);
        return "Message sent!";
    }
}
