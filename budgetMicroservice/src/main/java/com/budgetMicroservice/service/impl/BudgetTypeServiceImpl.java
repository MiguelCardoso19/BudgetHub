package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.dto.CustomPageDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.exception.SupplierNotFoundException;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetTypeRepository;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.BudgetValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetValidator budgetValidator;
    private final KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate;
    private final KafkaTemplate<String, BudgetTypeNotFoundException> kafkaBudgetTypeNotFoundExceptionTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Override
    @KafkaListener(topics = "create-budget-type", groupId = "budget_type_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException {
        budgetValidator.checkForExistingBudgetType(budgetTypeDTO, budgetTypeRepository);
        BudgetType budgetType = budgetMapper.toEntity(budgetTypeDTO);
        budgetTypeRepository.save(budgetType);
        BudgetTypeDTO savedBudgetTypeDTO = budgetMapper.toDTO(budgetType);
        savedBudgetTypeDTO.setCorrelationId(budgetTypeDTO.getCorrelationId());
        kafkaBudgetTypeTemplate.send("budget-type-response", savedBudgetTypeDTO);
        return savedBudgetTypeDTO;
    }

    @Override
    @KafkaListener(topics = "update-budget-type", groupId = "budget_type_group", concurrency = "10", containerFactory = "budgetTypeKafkaListenerContainerFactory")
    public BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException, BudgetTypeNotFoundException {
        findById(budgetTypeDTO.getId());
        budgetValidator.checkForExistingBudgetTypeUpdate(budgetTypeDTO, budgetTypeRepository);
        BudgetType budgetType = budgetMapper.toEntity(budgetTypeDTO);
        BudgetTypeDTO savedBudgetTypeDTO = budgetMapper.toDTO(budgetTypeRepository.save(budgetType));
        kafkaBudgetTypeTemplate.send("budget-type-response", savedBudgetTypeDTO);
        return savedBudgetTypeDTO;
    }

    @Override
    @KafkaListener(topics = "delete-budget-type", groupId = "uuid_group", concurrency = "10")
    public void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException {
        if (!budgetTypeRepository.existsById(id)) {
            kafkaBudgetTypeNotFoundExceptionTemplate.send("budget-type-not-found-exception-response", new BudgetTypeNotFoundException(id));
            throw new BudgetTypeNotFoundException(id);
        }

        budgetTypeRepository.deleteById(id);
        kafkaUuidTemplate.send("budget-type-delete-success-response",id);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "find-all-budget-type", groupId = "pageable_group", concurrency = "10", containerFactory = "customPageableKafkaListenerContainerFactory")
    public Page<BudgetTypeDTO> findAllBudgetTypes(CustomPageableDTO customPageableDTO) {
        Page<BudgetType> budgetTypePage = budgetTypeRepository.findAll(PageableUtils.convertToPageable(customPageableDTO));
        List<BudgetTypeDTO> budgetTypeDTOs = budgetMapper.toDTOTypeList(budgetTypePage);
        kafkaCustomPageTemplate.send("budget-type-page-response", PageableUtils.buildCustomPageDTO(customPageableDTO, budgetTypeDTOs, budgetTypePage));

        return budgetTypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetType findBudgetTypeEntityById(UUID id) throws BudgetTypeNotFoundException {
        return findById(id);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "find-budget-type-by-id", groupId = "uuid_group", concurrency = "10")
    public BudgetTypeDTO findBudgetTypeDTOById(UUID id) throws BudgetTypeNotFoundException {
        BudgetTypeDTO budgetTypeDTO = budgetMapper.toDTO(findById(id));
        kafkaBudgetTypeTemplate.send("budget-type-response", budgetTypeDTO);
        return budgetTypeDTO;
    }

    @Override
    public void save(BudgetType budgetType) {
        budgetTypeRepository.save(budgetType);
    }

    @Transactional
    public BudgetType findById(UUID id) throws BudgetTypeNotFoundException {
        Optional<BudgetType> budgetType = budgetTypeRepository.findById(id);

        if(budgetType.isPresent()){
            return budgetType.get();
        }

        kafkaBudgetTypeNotFoundExceptionTemplate.send("budget-type-not-found-exception-response", new BudgetTypeNotFoundException(id));
        throw new BudgetTypeNotFoundException(id);
    }
}
