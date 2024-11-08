package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.mapper.SupplierMapper;
import com.budgetMicroservice.model.Supplier;
import com.budgetMicroservice.repository.SupplierRepository;
import com.budgetMicroservice.service.SupplierService;
import com.budgetMicroservice.validator.SupplierValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;

    @Override
    @KafkaListener(topics = "create-supplier", groupId = "supplier_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public SupplierDTO create(SupplierDTO supplierDTO) throws SupplierValidationException {
        SupplierValidator.validateSupplierCreation(supplierDTO, supplierRepository);

        Supplier savedSupplier = supplierRepository.save(supplierMapper.toEntity(supplierDTO));
        SupplierDTO savedSupplierDTO = supplierMapper.toDTO(savedSupplier);

        kafkaSupplierTemplate.send("supplier-response", savedSupplierDTO);
        return savedSupplierDTO;
    }

    @Override
    @KafkaListener(topics = "update-supplier", groupId = "supplier_group", concurrency = "10", containerFactory = "supplierKafkaListenerContainerFactory")
    public SupplierDTO update(SupplierDTO supplierDTO) throws SupplierNotFoundException, SupplierValidationException {
        SupplierValidator.validateSupplierUpdate(supplierDTO, supplierRepository);
        findById(supplierDTO.getId());
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
            return;
        }

        throw new SupplierNotFoundException(id);
    }

    @Override
    @KafkaListener(topics = "find-all-suppliers", groupId = "pageable_group", concurrency = "10")
    public Page<SupplierDTO> findAll(Pageable pageable) {
        log.info(pageable.toString());
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);

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
    public Supplier findSupplierEntityById(UUID id) throws SupplierNotFoundException {
        return findById(id);
    }

    private Supplier findById(UUID id) throws SupplierNotFoundException {
        return supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));
    }
}