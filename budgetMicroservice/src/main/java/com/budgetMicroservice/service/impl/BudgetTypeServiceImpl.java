package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetTypeMapper;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetTypeRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetTypeMapper budgetTypeMapper;
    private final BudgetSubtypeService budgetSubtypeService;

    @Override
    public BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO) {
        BudgetType budgetType = budgetTypeMapper.toEntity(budgetTypeDTO);
        budgetTypeRepository.save(budgetType);

        return budgetTypeMapper.toDTO(budgetType);
    }

    @Override
    public BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException {
        BudgetType budgetType = budgetTypeRepository.findById(budgetTypeDTO.getId())
                .orElseThrow(() -> new BudgetTypeNotFoundException(budgetTypeDTO.getId()));

        budgetTypeMapper.updateFromDTO(budgetTypeDTO, budgetType);
        budgetTypeRepository.save(budgetType);

        return budgetTypeMapper.toDTO(budgetType);
    }

    @Override
    public void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException {
        if (!budgetTypeRepository.existsById(id)) {
            throw new BudgetTypeNotFoundException(id);
        }
        budgetTypeRepository.deleteById(id);
    }

    /**
     *
     * TO IMPLEMENT LATER
     *
     */
    @Override
    public BudgetTypeDTO getBudgetTotalValue(UUID typeId) throws BudgetTypeNotFoundException {
        BudgetType budgetType = budgetTypeRepository.findById(typeId)
                .orElseThrow(() -> new BudgetTypeNotFoundException(typeId));

        double totalSpent = budgetSubtypeService.getTotalSpentForType(typeId);

        BudgetTypeDTO budgetTypeDTO = budgetTypeMapper.toDTO(budgetType);
        budgetTypeDTO.setTotalSpent(totalSpent);

        return budgetTypeDTO;
    }

    @Override
    public BudgetTypeDTO findBudgetTypeById(UUID id) throws BudgetTypeNotFoundException {
        BudgetType budgetType = budgetTypeRepository.findById(id)
                .orElseThrow(() -> new BudgetTypeNotFoundException(id));

        return budgetTypeMapper.toDTO(budgetType);
    }

    @Override
    public Page<BudgetTypeDTO> findAllBudgetTypes(Pageable pageable) {
        Page<BudgetType> budgetTypePage = budgetTypeRepository.findAll(pageable);

        return budgetTypePage.map(budgetTypeMapper::toDTO);
    }
}
