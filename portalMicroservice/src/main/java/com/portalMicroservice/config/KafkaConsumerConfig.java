package com.portalMicroservice.config;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.dto.payment.CreatePaymentResponseDTO;
import com.portalMicroservice.dto.payment.StripeCardTokenDTO;
import com.portalMicroservice.dto.payment.StripeSepaTokenDTO;
import com.portalMicroservice.exception.budget.*;
import com.portalMicroservice.exception.payment.FailedToCancelPaymentException;
import com.portalMicroservice.exception.payment.FailedToConfirmPaymentException;
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
    public ConsumerFactory<String, CustomPageDTO> customPageConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "pageable_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<CustomPageDTO> jsonDeserializer = new JsonDeserializer<>(CustomPageDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomPageDTO> customPageKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CustomPageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customPageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SupplierDTO> supplierConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "supplier_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<SupplierDTO> jsonDeserializer = new JsonDeserializer<>(SupplierDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SupplierDTO> supplierKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SupplierDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(supplierConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InvoiceDTO> invoiceConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "invoice_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<InvoiceDTO> jsonDeserializer = new JsonDeserializer<>(InvoiceDTO.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InvoiceDTO> invoiceKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InvoiceDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(invoiceConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetTypeDTO> budgetTypeConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_type_response_group");
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
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_response_group");
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
    public ConsumerFactory<String, MovementDTO> movementConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_response_group");
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
    public ConsumerFactory<String, SupplierNotFoundException> supplierNotFoundExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "supplier_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<SupplierNotFoundException> jsonDeserializer = new JsonDeserializer<>(SupplierNotFoundException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SupplierNotFoundException> supplierNotFoundExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SupplierNotFoundException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(supplierNotFoundExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, SupplierValidationException> supplierValidationExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "supplier_validation_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<SupplierValidationException> jsonDeserializer = new JsonDeserializer<>(SupplierValidationException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SupplierValidationException> supplierValidationExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SupplierValidationException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(supplierValidationExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetTypeNotFoundException> budgetTypeNotFoundExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_type_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetTypeNotFoundException> jsonDeserializer = new JsonDeserializer<>(BudgetTypeNotFoundException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetTypeNotFoundException> budgetTypeNotFoundExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetTypeNotFoundException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetTypeNotFoundExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetTypeAlreadyExistsException> budgetTypeAlreadyExistsExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_type_already_exists_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetTypeAlreadyExistsException> jsonDeserializer = new JsonDeserializer<>(BudgetTypeAlreadyExistsException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetTypeAlreadyExistsException> budgetTypeAlreadyExistsExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetTypeAlreadyExistsException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetTypeAlreadyExistsExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetSubtypeNotFoundException> budgetSubtypeNotFoundExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetSubtypeNotFoundException> jsonDeserializer = new JsonDeserializer<>(BudgetSubtypeNotFoundException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeNotFoundException> budgetSubtypeNotFoundExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeNotFoundException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetSubtypeNotFoundExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetSubtypeAlreadyExistsException> budgetSubtypeAlreadyExistsExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_already_exists_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetSubtypeAlreadyExistsException> jsonDeserializer = new JsonDeserializer<>(BudgetSubtypeAlreadyExistsException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeAlreadyExistsException> budgetSubtypeAlreadyExistsExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetSubtypeAlreadyExistsException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetSubtypeAlreadyExistsExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetExceededException> budgetExceededExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "budget_subtype_budget_exceeded_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetExceededException> jsonDeserializer = new JsonDeserializer<>(BudgetExceededException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> budgetExceededExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetExceededExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, InvoiceNotFoundException> invoiceNotFoundExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "invoice_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<InvoiceNotFoundException> jsonDeserializer = new JsonDeserializer<>(InvoiceNotFoundException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InvoiceNotFoundException> invoiceNotFoundExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, InvoiceNotFoundException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(invoiceNotFoundExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, FailedToUploadFileException> failedToUploadFileExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "failed_to_upload_file_exception_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<FailedToUploadFileException> jsonDeserializer = new JsonDeserializer<>(FailedToUploadFileException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FailedToUploadFileException> failedToUploadFileExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FailedToUploadFileException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(failedToUploadFileExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementNotFoundException> movementNotFoundExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<MovementNotFoundException> jsonDeserializer = new JsonDeserializer<>(MovementNotFoundException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementNotFoundException> movementNotFoundExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementNotFoundException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementNotFoundExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementValidationException> movementValidationExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_not_found_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<MovementValidationException> jsonDeserializer = new JsonDeserializer<>(MovementValidationException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementValidationException> movementValidationExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementValidationException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementValidationExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetExceededException> movementBudgetExceededExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_budget_exceeded_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetExceededException> jsonDeserializer = new JsonDeserializer<>(BudgetExceededException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> movementBudgetExceededExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementBudgetExceededExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementsNotFoundForBudgetTypeException> movementsNotFoundForBudgetTypeExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_not_found_for_budget_type_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<MovementsNotFoundForBudgetTypeException> jsonDeserializer = new JsonDeserializer<>(MovementsNotFoundForBudgetTypeException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementsNotFoundForBudgetTypeException> movementsNotFoundForBudgetTypeExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementsNotFoundForBudgetTypeException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementsNotFoundForBudgetTypeExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, MovementsNotFoundForBudgetSubtypeException> movementsNotFoundForBudgetSubtypeExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "movement_not_found_for_budget_subtype_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<MovementsNotFoundForBudgetSubtypeException> jsonDeserializer = new JsonDeserializer<>(MovementsNotFoundForBudgetSubtypeException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MovementsNotFoundForBudgetSubtypeException> movementsNotFoundForBudgetSubtypeExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MovementsNotFoundForBudgetSubtypeException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(movementsNotFoundForBudgetSubtypeExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, GenerateExcelException> generateExcelExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "generate_excel_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<GenerateExcelException> jsonDeserializer = new JsonDeserializer<>(GenerateExcelException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GenerateExcelException> generateExcelExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenerateExcelException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(generateExcelExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "string_group");
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CreatePaymentResponseDTO> createPaymentResponseConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "create_payment_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<CreatePaymentResponseDTO> jsonDeserializer = new JsonDeserializer<>(CreatePaymentResponseDTO.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreatePaymentResponseDTO> createPaymentResponseKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CreatePaymentResponseDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createPaymentResponseConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StripeCardTokenDTO> stripeCardTokenConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "stripe_card_token_group");
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
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "stripe_sepa_token_group");
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

    @Bean
    public ConsumerFactory<String, FailedToCancelPaymentException> failedToCancelPaymentExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "failed_to_cancel_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<FailedToCancelPaymentException> jsonDeserializer = new JsonDeserializer<>(FailedToCancelPaymentException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FailedToCancelPaymentException> failedToCancelPaymentExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FailedToCancelPaymentException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(failedToCancelPaymentExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, FailedToConfirmPaymentException> failedToConfirmPaymentExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "failed_to_cancel_payment_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<FailedToConfirmPaymentException> jsonDeserializer = new JsonDeserializer<>(FailedToConfirmPaymentException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FailedToConfirmPaymentException> failedToConfirmPaymentExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FailedToConfirmPaymentException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(failedToConfirmPaymentExceptionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BudgetExceededException> budgetExceededForPaymentExceptionConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "payment_exceeded_available_funds_response_group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        JsonDeserializer<BudgetExceededException> jsonDeserializer = new JsonDeserializer<>(BudgetExceededException.class);
        jsonDeserializer.setRemoveTypeHeaders(false);
        jsonDeserializer.setUseTypeMapperForKey(true);
        jsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> budgetExceededForPaymentExceptionKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BudgetExceededException> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(budgetExceededForPaymentExceptionConsumerFactory());
        return factory;
    }
}