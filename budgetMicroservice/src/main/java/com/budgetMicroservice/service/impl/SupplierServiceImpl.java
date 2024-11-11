package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.CustomPageDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.mapper.SupplierMapper;
import com.budgetMicroservice.model.Supplier;
import com.budgetMicroservice.repository.SupplierRepository;
import com.budgetMicroservice.service.SupplierService;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.SupplierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final SupplierValidator supplierValidator;
    private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, SupplierNotFoundException> kafkaSupplierNotFoundExceptionTemplate;

    @Override
    @KafkaListener(topics = "create-supplier", groupId = "supplier_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public SupplierDTO create(SupplierDTO supplierDTO) throws SupplierValidationException {
        supplierValidator.validateSupplierCreation(supplierDTO, supplierRepository);
        Supplier savedSupplier = supplierRepository.save(supplierMapper.toEntity(supplierDTO));
        SupplierDTO savedSupplierDTO = supplierMapper.toDTO(savedSupplier);
        savedSupplierDTO.setCorrelationId(supplierDTO.getCorrelationId());

        kafkaSupplierTemplate.send("supplier-response", savedSupplierDTO);
        return savedSupplierDTO;
    }

    @Override
    @KafkaListener(topics = "update-supplier", groupId = "supplier_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public SupplierDTO update(SupplierDTO supplierDTO) throws SupplierNotFoundException, SupplierValidationException {
        findById(supplierDTO.getId());
        supplierValidator.validateSupplierUpdate(supplierDTO, supplierRepository);
        Supplier existingSupplier = supplierMapper.toEntity(supplierDTO);
        SupplierDTO updatedSupplierDTO = supplierMapper.toDTO(supplierRepository.save(existingSupplier));

        kafkaSupplierTemplate.send("supplier-response", updatedSupplierDTO);

        return updatedSupplierDTO;
    }

    @Override
    @KafkaListener(topics = "delete-supplier", groupId = "uuid_group", concurrency = "10")
    public void delete(UUID id) throws SupplierNotFoundException {
        if (supplierRepository.existsById(id)) {
            supplierRepository.delete(findById(id));
            kafkaUuidTemplate.send("supplier-delete-success-response", id);
            return;
        }

        kafkaSupplierNotFoundExceptionTemplate.send("supplier-not-found-exception-response", new SupplierNotFoundException(id));
        throw new SupplierNotFoundException(id);
    }

    @Override
    @KafkaListener(topics = "find-all-suppliers", groupId = "pageable_group", concurrency = "10", containerFactory = "customPageableKafkaListenerContainerFactory")
    public Page<SupplierDTO> findAll(CustomPageableDTO customPageableDTO) {
        Page<Supplier> supplierPage = supplierRepository.findAll(PageableUtils.convertToPageable(customPageableDTO));
        List<SupplierDTO> supplierDTOs = supplierMapper.toDTOList(supplierPage);
        kafkaCustomPageTemplate.send("page-response", PageableUtils.buildCustomPageDTO(customPageableDTO, supplierDTOs, supplierPage));
        return supplierPage.map(supplierMapper::toDTO);
    }

    @Override
    @KafkaListener(topics = "find-by-id-supplier", groupId = "uuid_group", concurrency = "10")
    public SupplierDTO findSupplierDTOById(UUID id) throws SupplierNotFoundException {
        SupplierDTO supplierDTO = supplierMapper.toDTO(findById(id));
        kafkaSupplierTemplate.send("supplier-response", supplierDTO);
        return supplierDTO;
    }

    @Override
    public boolean existsById(UUID id) {
        return supplierRepository.existsById(id);
    }

    @Override
    public Supplier findSupplierEntityById(UUID id) throws SupplierNotFoundException {
        return findById(id);
    }

    private Supplier findById(UUID id) throws SupplierNotFoundException {
        Optional<Supplier> supplier = supplierRepository.findById(id);

        if (supplier.isPresent()) {
            return supplier.get();
        }

        kafkaSupplierNotFoundExceptionTemplate.send("supplier-not-found-exception-response", new SupplierNotFoundException(id));
        throw new SupplierNotFoundException(id);
    }
}