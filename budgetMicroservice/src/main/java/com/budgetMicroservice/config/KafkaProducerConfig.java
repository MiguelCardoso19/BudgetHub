//package com.budgetMicroservice.config;
//
//import com.budgetMicroservice.dto.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@RequiredArgsConstructor
//public class KafkaProducerConfig {
//    private final KafkaProperties kafkaProperties;
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    private Map<String, Object> producerConfigProps() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        return configProps;
//    }
//
//    @Bean
//    public ProducerFactory<String, String> stringProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaStringTemplate() {
//        return new KafkaTemplate<>(stringProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, MovementDTO> movementProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, MovementDTO> kafkaMovementTemplate() {
//        return new KafkaTemplate<>(movementProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, BudgetSubtypeDTO> budgetSubtypeProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubtypeTemplate() {
//        return new KafkaTemplate<>(budgetSubtypeProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, SupplierDTO> supplierProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate() {
//        return new KafkaTemplate<>(supplierProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, InvoiceDTO> invoiceProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate() {
//        return new KafkaTemplate<>(invoiceProducerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, BudgetTypeDTO> budgetTypeProducerFactory() {
//        Map<String, Object> configProps = producerConfigProps();
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate() {
//        return new KafkaTemplate<>(budgetTypeProducerFactory());
//    }
//}
