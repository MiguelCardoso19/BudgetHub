package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.exception.SupplierValidationException;
import com.budgetMicroservice.mapper.SupplierMapper;
import com.budgetMicroservice.model.Supplier;
import com.budgetMicroservice.repository.SupplierRepository;
import com.budgetMicroservice.service.SupplierService;
import com.budgetMicroservice.validator.SupplierValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
  //  private final KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;
 //   private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
  //  @KafkaListener(topics = "create-supplier", groupId = "supplier_group", concurrency = "10")
    public SupplierDTO create(SupplierDTO supplierDTO) throws SupplierValidationException {
        SupplierValidator.validateSupplierCreation(supplierDTO, supplierRepository);

        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);
        SupplierDTO savedSupplierDTO = supplierMapper.toDTO(savedSupplier);
     //   kafkaSupplierTemplate.send("create-supplier", savedSupplierDTO);

        return savedSupplierDTO;
    }

    @Override
   // @KafkaListener(topics = "update-supplier", groupId = "supplier_group", concurrency = "10")
    public SupplierDTO update(SupplierDTO supplierDTO) throws SupplierNotFoundException, SupplierValidationException {
        SupplierValidator.validateSupplierUpdate(supplierDTO, supplierRepository);

        Supplier existingSupplier = findById(supplierDTO.getId());
        supplierMapper.updateFromDTO(supplierDTO, existingSupplier);

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        SupplierDTO updatedSupplierDTO = supplierMapper.toDTO(updatedSupplier);
     //   kafkaSupplierTemplate.send("update-supplier", updatedSupplierDTO);

        return updatedSupplierDTO;
    }

    @Override
   // @KafkaListener(topics = "delete-supplier", groupId = "supplier_group", concurrency = "10")
    public void delete(UUID id) throws SupplierNotFoundException {
        supplierRepository.delete(findById(id));
    }

    @Override
  //  @KafkaListener(topics = "find-all-suppliers", groupId = "supplier_group", concurrency = "10")
    public Page<SupplierDTO> findAll(Pageable pageable) throws JsonProcessingException {
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);
     //   kafkaStringTemplate.send("find-all-suppliers", objectMapper.writeValueAsString(supplierPage));

        return supplierPage.map(supplierMapper::toDTO);
    }

    @Override
    public SupplierDTO findSupplierDTOById(UUID id) throws SupplierNotFoundException {
        return supplierMapper.toDTO(findById(id));
    }

    @Override
    public Supplier findSupplierEntityById(UUID id) throws SupplierNotFoundException {
        return findById(id);
    }

    private Supplier findById(UUID id) throws SupplierNotFoundException {
        return supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));
    }
}