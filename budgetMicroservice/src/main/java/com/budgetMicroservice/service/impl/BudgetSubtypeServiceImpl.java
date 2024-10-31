package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.validator.BudgetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetSubtypeServiceImpl implements BudgetSubtypeService {
    private final BudgetSubtypeRepository budgetSubtypeRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetTypeService budgetTypeService;

    @Override
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        BudgetType budgetType = budgetTypeService.findBudgetTypeEntityById(budgetSubtypeDTO.getBudgetTypeId());

        BudgetValidator.checkForExistingBudgetSubtype(budgetSubtypeDTO, budgetSubtypeRepository);

        BudgetSubtype budgetSubtype = budgetMapper.toEntity(budgetSubtypeDTO);
        budgetSubtype.setBudgetType(budgetType);
        budgetSubtypeRepository.save(budgetSubtype);

        return budgetMapper.toDTO(budgetSubtype);
    }

    @Override
    public BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException {
        BudgetSubtype budgetSubtype = budgetSubtypeRepository.findById(budgetSubtypeDTO.getId())
                .orElseThrow(() -> new BudgetSubtypeNotFoundException(budgetSubtypeDTO.getId()));

        BudgetValidator.checkForExistingBudgetSubtypeUpdate(budgetSubtypeDTO, budgetSubtypeRepository);

        budgetMapper.updateFromDTO(budgetSubtypeDTO, budgetSubtype);
        budgetSubtypeRepository.save(budgetSubtype);

        return budgetMapper.toDTO(budgetSubtype);
    }

    @Override
    public void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException {
        if (!budgetSubtypeRepository.existsById(subtypeId)) {
            throw new BudgetSubtypeNotFoundException(subtypeId);
        }
        budgetSubtypeRepository.deleteById(subtypeId);
    }

    @Override
    public Page<BudgetSubtypeDTO> findAllBudgetSubtypes(Pageable pageable) {
        Page<BudgetSubtype> budgetSubtypePage = budgetSubtypeRepository.findAll(pageable);
        return budgetSubtypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetSubtype findBudgetSubtypeEntityById(UUID id) throws BudgetSubtypeNotFoundException {
        return findById(id);
    }

    @Override
    public BudgetSubtypeDTO findBudgetSubtypeDTOById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetMapper.toDTO(findById(id));
    }

    @Override
    public void save(BudgetSubtype budgetSubtype) {
        budgetSubtypeRepository.save(budgetSubtype);
    }

    private BudgetSubtype findById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetSubtypeRepository.findById(id)
                .orElseThrow(() -> new BudgetSubtypeNotFoundException(id));

    }
}