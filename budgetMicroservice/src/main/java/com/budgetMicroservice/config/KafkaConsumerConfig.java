//package com.budgetMicroservice.config;
//
//import com.budgetMicroservice.dto.BudgetSubtypeDTO;
//import com.budgetMicroservice.dto.BudgetTypeDTO;
//import com.budgetMicroservice.dto.InvoiceDTO;
//import com.budgetMicroservice.dto.SupplierDTO;
//import com.budgetMicroservice.model.BudgetSubtype;
//import com.budgetMicroservice.model.BudgetType;
//import com.budgetMicroservice.model.Invoice;
//import com.budgetMicroservice.model.Supplier;
//import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableKafka
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Value("${kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Bean
//    public ConsumerFactory<String, String> stringConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "string_group");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(stringConsumerFactory());
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, SupplierDTO> supplierConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Supplier.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "supplier_group");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, SupplierDTO> supplierKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, SupplierDTO> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(supplierConsumerFactory());
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, InvoiceDTO> invoiceConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, Invoice.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "invoice_group");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, InvoiceDTO> invoiceKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, InvoiceDTO> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(invoiceConsumerFactory());
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, BudgetTypeDTO> budgetTypeConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BudgetType.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_type_group");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, BudgetTypeDTO> budgetTypeKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, BudgetTypeDTO> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(budgetTypeConsumerFactory());
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//
//    @Bean
//    public ConsumerFactory<String, BudgetSubtypeDTO> budgetSubtypeConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BudgetSubtype.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_group");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeDTO> budgetSubtypeKafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeDTO> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(budgetSubtypeConsumerFactory());
//        factory.getContainerProperties().setPollTimeout(3000);
//        return factory;
//    }
//}