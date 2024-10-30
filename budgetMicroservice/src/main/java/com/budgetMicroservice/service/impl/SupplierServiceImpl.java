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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public Supplier findById(UUID id) throws SupplierNotFoundException {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) throws SupplierValidationException {
        SupplierValidator.validateSupplierCreation(supplierDTO, supplierRepository);

        Supplier supplier = supplierMapper.toEntity(supplierDTO);
        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toDTO(savedSupplier);
    }

    @Override
    public SupplierDTO update(SupplierDTO supplierDTO) throws SupplierNotFoundException, SupplierValidationException {
        SupplierValidator.validateSupplierUpdate(supplierDTO, supplierRepository);

        Supplier existingSupplier = findById(supplierDTO.getId());

        supplierMapper.updateFromDTO(supplierDTO, existingSupplier);

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDTO(updatedSupplier);
    }

    @Override
    public void delete(UUID id) throws SupplierNotFoundException {
        Supplier existingSupplier = findById(id);
        supplierRepository.delete(existingSupplier);
    }

    @Override
    public Page<SupplierDTO> findAll(Pageable pageable) {
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);
        return supplierPage.map(supplierMapper::toDTO);
    }

    @Override
    public SupplierDTO findSupplierById(UUID id) throws SupplierNotFoundException {
        return supplierMapper.toDTO(findById(id));
    }
}
