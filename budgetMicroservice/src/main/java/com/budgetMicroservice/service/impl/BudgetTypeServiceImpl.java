package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetTypeRepository;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.validator.BudgetValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException {
        BudgetValidator.checkForExistingBudgetType(budgetTypeDTO, budgetTypeRepository);

        BudgetType budgetType = budgetMapper.toEntity(budgetTypeDTO);
        budgetTypeRepository.save(budgetType);

        return budgetMapper.toDTO(budgetType);
    }

    @Override
    public BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException {
        BudgetType budgetType = budgetTypeRepository.findById(budgetTypeDTO.getId())
                .orElseThrow(() -> new BudgetTypeNotFoundException(budgetTypeDTO.getId()));

        BudgetValidator.checkForExistingBudgetTypeUpdate(budgetTypeDTO, budgetTypeRepository);

        budgetMapper.updateFromDTO(budgetTypeDTO, budgetType);
        budgetTypeRepository.save(budgetType);

        return budgetMapper.toDTO(budgetType);
    }

    @Override
    public void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException {
        if (!budgetTypeRepository.existsById(id)) {
            throw new BudgetTypeNotFoundException(id);
        }
        budgetTypeRepository.deleteById(id);
    }

    @Override
    public Page<BudgetTypeDTO> findAllBudgetTypes(Pageable pageable) {
        Page<BudgetType> budgetTypePage = budgetTypeRepository.findAll(pageable);

        return budgetTypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetType findBudgetTypeEntityById(UUID id) throws BudgetSubtypeNotFoundException {
        return findById(id);
    }

    @Override
    public BudgetTypeDTO findBudgetTypeDTOById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetMapper.toDTO(findById(id));
    }

    @Override
    public void save(BudgetType budgetType) {
        budgetTypeRepository.save(budgetType);
    }

    private BudgetType findById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetTypeRepository.findById(id)
                .orElseThrow(() -> new BudgetSubtypeNotFoundException(id));
    }
}
