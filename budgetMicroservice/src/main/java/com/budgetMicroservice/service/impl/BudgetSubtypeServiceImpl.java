package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.CustomPageDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.exception.BudgetExceededException;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.exception.BudgetTypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.util.BudgetUtils;
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
public class BudgetSubtypeServiceImpl implements BudgetSubtypeService {
    private final BudgetSubtypeRepository budgetSubtypeRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetValidator budgetValidator;
    private final BudgetTypeService budgetTypeService;
    private final BudgetUtils budgetUtils;
    private final KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubtypeTemplate;
    private final KafkaTemplate<String, UUID> kafkaUuidTemplate;
    private final KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;
    private final KafkaTemplate<String, BudgetSubtypeNotFoundException> kafkaBudgetSubtypeNotFoundExceptionTemplate;

    @Override
    @Transactional
    @KafkaListener(topics = "add-budget-subtype", groupId = "budget_subtype_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeAlreadyExistsException, BudgetSubtypeNotFoundException, BudgetExceededException, BudgetTypeNotFoundException {
        BudgetType budgetType = budgetTypeService.findBudgetTypeEntityById(budgetSubtypeDTO.getBudgetTypeId());
        budgetValidator.checkForExistingBudgetSubtype(budgetSubtypeDTO, budgetSubtypeRepository);
        budgetUtils.checkBudgetExceeded(budgetType, budgetSubtypeDTO, budgetSubtypeRepository, null);
        BudgetSubtype budgetSubtype = budgetMapper.toEntity(budgetSubtypeDTO);
        budgetSubtype.setBudgetType(budgetType);
        budgetSubtypeRepository.save(budgetSubtype);
        BudgetSubtypeDTO savedBudgetSubtypeDTO = budgetMapper.toDTO(budgetSubtype);
        savedBudgetSubtypeDTO.setCorrelationId(budgetSubtypeDTO.getCorrelationId());
        kafkaBudgetSubtypeTemplate.send("budget-subtype-response", savedBudgetSubtypeDTO);
        return savedBudgetSubtypeDTO;
    }

    @Override
    @KafkaListener(topics = "update-budget-subtype", groupId = "budget_subtype_group", concurrency = "10", containerFactory = "budgetSubtypeKafkaListenerContainerFactory")
    public BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException, BudgetExceededException {
        BudgetSubtype existingBudgetSubtype = findById(budgetSubtypeDTO.getId());
        budgetUtils.checkBudgetExceeded(existingBudgetSubtype.getBudgetType(), budgetSubtypeDTO, budgetSubtypeRepository, existingBudgetSubtype);
        budgetValidator.checkForExistingBudgetSubtypeUpdate(budgetSubtypeDTO, budgetSubtypeRepository);
        BudgetSubtype budgetSubtype = budgetMapper.toEntity(budgetSubtypeDTO);
        BudgetSubtypeDTO savedBudgetSubtypeDTO = budgetMapper.toDTO(budgetSubtypeRepository.save(budgetSubtype));

        kafkaBudgetSubtypeTemplate.send("budget-subtype-response", savedBudgetSubtypeDTO);

        return savedBudgetSubtypeDTO;
    }

    @Override
    @KafkaListener(topics = "delete-budget-subtype", groupId = "uuid_group", concurrency = "10")
    public void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException {
        Optional<BudgetSubtype> budgetSubtype = budgetSubtypeRepository.findById(subtypeId);

        if (budgetSubtype.isPresent()) {
            BudgetType budgetType = budgetSubtype.get().getBudgetType();
            budgetType.setAvailableFunds(budgetType.getAvailableFunds() - budgetSubtype.get().getAvailableFunds());
            budgetTypeService.save(budgetType);
            budgetSubtypeRepository.deleteById(subtypeId);
            kafkaUuidTemplate.send("budget-subtype-delete-success-response", subtypeId);
            return;
        }

        kafkaBudgetSubtypeNotFoundExceptionTemplate.send("budget-subtype-not-found-exception-response", new BudgetSubtypeNotFoundException(subtypeId));
        throw new BudgetSubtypeNotFoundException(subtypeId);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "find-all-budget-subtype", groupId = "pageable_group", concurrency = "10", containerFactory = "customPageableKafkaListenerContainerFactory")
    public Page<BudgetSubtypeDTO> findAllBudgetSubtypes(CustomPageableDTO customPageableDTO) {
        Page<BudgetSubtype> budgetSubtypePage = budgetSubtypeRepository.findAll(PageableUtils.convertToPageable(customPageableDTO));
        List<BudgetSubtypeDTO> budgetSubtypeDTOs = budgetMapper.toDTOSubtypeList(budgetSubtypePage);
        kafkaCustomPageTemplate.send("budget-subtype-page-response", PageableUtils.buildCustomPageDTO(customPageableDTO, budgetSubtypeDTOs, budgetSubtypePage));
        return budgetSubtypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetSubtype findBudgetSubtypeEntityById(UUID id) throws BudgetSubtypeNotFoundException {
        return findById(id);
    }

    @Override
    @Transactional
    @KafkaListener(topics = "find-budget-subtype-by-id", groupId = "uuid_group", concurrency = "10")
    public BudgetSubtypeDTO findBudgetSubtypeDTOById(UUID id) throws BudgetSubtypeNotFoundException {
        BudgetSubtypeDTO budgetSubtypeDTO = budgetMapper.toDTO(findById(id));
        kafkaBudgetSubtypeTemplate.send("budget-subtype-response", budgetSubtypeDTO);
        return budgetSubtypeDTO;
    }

    @Override
    public void save(BudgetSubtype budgetSubtype) {
        budgetSubtypeRepository.save(budgetSubtype);
    }

    @Transactional
    public BudgetSubtype findById(UUID id) throws BudgetSubtypeNotFoundException {
        Optional<BudgetSubtype> budgetSubtype = budgetSubtypeRepository.findById(id);

        if (budgetSubtype.isPresent()) {
            return budgetSubtype.get();
        }
        kafkaBudgetSubtypeNotFoundExceptionTemplate.send("budget-subtype-not-found-exception-response", new BudgetSubtypeNotFoundException(id));
        throw new BudgetSubtypeNotFoundException(id);
    }
}