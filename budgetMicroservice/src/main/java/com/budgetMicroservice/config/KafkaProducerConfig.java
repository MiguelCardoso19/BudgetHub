package com.budgetMicroservice.config;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka-bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, CustomPageDTO> customPageProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CustomPageDTO> customPageKafkaTemplate() {
        return new KafkaTemplate<>(customPageProducerFactory());
    }

    @Bean
    public ProducerFactory<String, NotificationRequestDTO> notificationRequestProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, NotificationRequestDTO> notificationRequestKafkaTemplate() {
        return new KafkaTemplate<>(notificationRequestProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UUID> uuidProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, UUID> uuidKafkaTemplate() {
        return new KafkaTemplate<>(uuidProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MovementDTO> movementProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MovementDTO> kafkaMovementTemplate() {
        return new KafkaTemplate<>(movementProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetSubtypeDTO> budgetSubtypeProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetSubtypeDTO> budgetSubtypeKafkaTemplate() {
        return new KafkaTemplate<>(budgetSubtypeProducerFactory());
    }

    @Bean
    public ProducerFactory<String, SupplierDTO> supplierProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SupplierDTO> supplierKafkaTemplate() {
        return new KafkaTemplate<>(supplierProducerFactory());
    }

    @Bean
    public ProducerFactory<String, InvoiceDTO> invoiceProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, InvoiceDTO> invoiceKafkaTemplate() {
        return new KafkaTemplate<>(invoiceProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetTypeDTO> budgetTypeProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetTypeDTO> budgetTypekafkaTemplate() {
        return new KafkaTemplate<>(budgetTypeProducerFactory());
    }

    @Bean
    public ProducerFactory<String, SupplierNotFoundException> supplierNotFoundExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SupplierNotFoundException> supplierNotFoundExceptionkafkaTemplate() {
        return new KafkaTemplate<>(supplierNotFoundExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, SupplierValidationException> supplierValidationExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, SupplierValidationException> supplierValidationExceptionkafkaTemplate() {
        return new KafkaTemplate<>(supplierValidationExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetTypeNotFoundException> budgetTypeNotFoundExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetTypeNotFoundException> budgetTypeNotFoundExceptionkafkaTemplate() {
        return new KafkaTemplate<>(budgetTypeNotFoundExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetTypeAlreadyExistsException> budgetTypeAlreadyExistsExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetTypeAlreadyExistsException> budgetTypeAlreadyExistsExceptionkafkaTemplate() {
        return new KafkaTemplate<>(budgetTypeAlreadyExistsExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetSubtypeNotFoundException> budgetSubtypeNotFoundExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetSubtypeNotFoundException> budgetSubtypeNotFoundExceptionkafkaTemplate() {
        return new KafkaTemplate<>(budgetSubtypeNotFoundExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetSubtypeAlreadyExistsException> budgetSubtypeAlreadyExistsExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetSubtypeAlreadyExistsException> budgetSubtypeAlreadyExistsExceptionkafkaTemplate() {
        return new KafkaTemplate<>(budgetSubtypeAlreadyExistsExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, BudgetExceededException> budgetExceededExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, BudgetExceededException> budgetExceededExceptionkafkaTemplate() {
        return new KafkaTemplate<>(budgetExceededExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, InvoiceNotFoundException> invoiceNotFoundExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, InvoiceNotFoundException> invoiceNotFoundExceptionkafkaTemplate() {
        return new KafkaTemplate<>(invoiceNotFoundExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, FailedToUploadFileException> failedToUploadFileExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, FailedToUploadFileException> failedToUploadFileExceptionProducerFactorykafkaTemplate() {
        return new KafkaTemplate<>(failedToUploadFileExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MovementNotFoundException> movementNotFoundExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MovementNotFoundException> movementNotFoundExceptionkafkaTemplate() {
        return new KafkaTemplate<>(movementNotFoundExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MovementValidationException> movementValidationExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MovementValidationException> movementValidationExceptionExceptionkafkaTemplate() {
        return new KafkaTemplate<>(movementValidationExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MovementsNotFoundForBudgetTypeException> movementsNotFoundForBudgetTypeExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MovementsNotFoundForBudgetTypeException> movementsNotFoundForBudgetTypeExceptionkafkaTemplate() {
        return new KafkaTemplate<>(movementsNotFoundForBudgetTypeExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MovementsNotFoundForBudgetSubtypeException> movementsNotFoundForBudgetSubtypeExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, MovementsNotFoundForBudgetSubtypeException> movementsNotFoundForBudgetSubtypeExceptionkafkaTemplate() {
        return new KafkaTemplate<>(movementsNotFoundForBudgetSubtypeExceptionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, GenerateExcelException> generateExcelExceptionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, GenerateExcelException> generateExcelExceptionkafkaTemplate() {
        return new KafkaTemplate<>(generateExcelExceptionProducerFactory());
    }
}
