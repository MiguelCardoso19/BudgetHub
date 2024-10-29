package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetTypeMapper;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetSubtypeServiceImpl implements BudgetSubtypeService {
    private final BudgetSubtypeRepository budgetSubtypeRepository;
    private final BudgetTypeService budgetTypeService;
    private final BudgetTypeMapper budgetTypeMapper;

    @Override
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetTypeNotFoundException {
        BudgetTypeDTO budgetType = budgetTypeService.findBudgetTypeById(budgetSubtypeDTO.getBudgetTypeId());

        BudgetSubtype budgetSubtype = budgetTypeMapper.toEntity(budgetSubtypeDTO);
        budgetSubtype.setBudgetType(budgetTypeMapper.toEntity(budgetType));
        budgetSubtypeRepository.save(budgetSubtype);

        return budgetTypeMapper.toDTO(budgetSubtype);
    }

    /**
     *
     * TO IMPLEMENT LATER
     *
     */
    @Override
    public Double getTotalSpentForType(UUID typeId) throws BudgetTypeNotFoundException {
        BudgetTypeDTO budgetType = budgetTypeService.findBudgetTypeById(typeId);

        return budgetTypeMapper.toEntity(budgetType).getSubtypes().stream()
                .mapToDouble(BudgetSubtype::getTotalSpent)
                .sum();
    }

    @Override
    public BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException {
        BudgetSubtype budgetSubtype = budgetSubtypeRepository.findById(budgetSubtypeDTO.getId())
                .orElseThrow(() -> new BudgetSubtypeNotFoundException(budgetSubtypeDTO.getId()));

        budgetTypeMapper.updateFromDTO(budgetSubtypeDTO, budgetSubtype);
        budgetSubtypeRepository.save(budgetSubtype);

        return budgetTypeMapper.toDTO(budgetSubtype);
    }

    @Override
    public void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException {
        if (!budgetSubtypeRepository.existsById(subtypeId)) {
            throw new BudgetSubtypeNotFoundException(subtypeId);
        }
        budgetSubtypeRepository.deleteById(subtypeId);
    }

    @Override
    public BudgetSubtypeDTO findBudgetSubtypeById(UUID id) throws BudgetSubtypeNotFoundException {
        BudgetSubtype budgetSubtype = budgetSubtypeRepository.findById(id)
                .orElseThrow(() -> new BudgetSubtypeNotFoundException(id));

        return budgetTypeMapper.toDTO(budgetSubtype);
    }

    @Override
    public Page<BudgetSubtypeDTO> findAllBudgetSubtypes(Pageable pageable) {
        Page<BudgetSubtype> budgetSubtypePage = budgetSubtypeRepository.findAll(pageable);
        return budgetSubtypePage.map(budgetTypeMapper::toDTO);
    }
}