package com.paymentMicroservice.config;

import com.paymentMicroservice.dto.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka-bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, BudgetTypeDTO> budgetTypeConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_type_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetTypeDTO> jsonDeserializer = new JsonDeserializer<>(BudgetTypeDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetTypeDTO> budgetTypeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetTypeDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetTypeConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetSubtypeDTO> budgetSubtypeConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetSubtypeDTO> jsonDeserializer = new JsonDeserializer<>(BudgetSubtypeDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeDTO> budgetSubtypeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetSubtypeConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementUpdateStatusRequestDTO> movementUpdateStatusRequestConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_update_status_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, MovementUpdateStatusRequestDTO.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(MovementUpdateStatusRequestDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementUpdateStatusRequestDTO> movementUpdateStatusRequestKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementUpdateStatusRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementUpdateStatusRequestConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementDTO> movementConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<MovementDTO> jsonDeserializer = new JsonDeserializer<>(MovementDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementDTO> movementKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CreatePaymentDTO> createPaymentRequestConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "create_payment_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<CreatePaymentDTO> jsonDeserializer = new JsonDeserializer<>(CreatePaymentDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreatePaymentDTO> createPaymentRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CreatePaymentDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createPaymentRequestConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentActionRequestDTO> paymentActionRequestConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "payment_action_request_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<PaymentActionRequestDTO> jsonDeserializer = new JsonDeserializer<>(PaymentActionRequestDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentActionRequestDTO> paymentActionRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentActionRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentActionRequestConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RefundChargeRequestDTO> refundChargeRequestConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "refund_charge_request_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<RefundChargeRequestDTO> jsonDeserializer = new JsonDeserializer<>(RefundChargeRequestDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RefundChargeRequestDTO> refundChargeRequestKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RefundChargeRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(refundChargeRequestConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StripeCardTokenDTO> stripeCardTokenConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "create_card_token_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<StripeCardTokenDTO> jsonDeserializer = new JsonDeserializer<>(StripeCardTokenDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StripeCardTokenDTO> stripeCardTokenKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StripeCardTokenDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stripeCardTokenConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StripeSepaTokenDTO> stripeSepaTokenConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "create_sepa_token_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<StripeSepaTokenDTO> jsonDeserializer = new JsonDeserializer<>(StripeSepaTokenDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StripeSepaTokenDTO> stripeSepaTokenKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StripeSepaTokenDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stripeSepaTokenConsumerFactory());
        return factory;
    }
}