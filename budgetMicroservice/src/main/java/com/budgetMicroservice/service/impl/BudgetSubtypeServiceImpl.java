package com.budgetMicroservice.service.impl;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.exception.BudgetSubtypeAlreadyExistsException;
import com.budgetMicroservice.exception.BudgetSubtypeNotFoundException;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.BudgetSubtypeService;
import com.budgetMicroservice.service.BudgetTypeService;
import com.budgetMicroservice.validator.BudgetValidator;
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
public class BudgetSubtypeServiceImpl implements BudgetSubtypeService {
    private final BudgetSubtypeRepository budgetSubtypeRepository;
    private final BudgetMapper budgetMapper;
    private final BudgetTypeService budgetTypeService;
    private final KafkaTemplate<String, BudgetSubtypeDTO> kafkaBudgetSubtypeTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @KafkaListener(topics = "add-budget-subtype", groupId = "budget_subtype_group", concurrency = "10")
    public BudgetSubtypeDTO addSubtypeToBudget(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeAlreadyExistsException, BudgetSubtypeNotFoundException {
        BudgetType budgetType = budgetTypeService.findBudgetTypeEntityById(budgetSubtypeDTO.getBudgetTypeId());

        BudgetValidator.checkForExistingBudgetSubtype(budgetSubtypeDTO, budgetSubtypeRepository);

        BudgetSubtype budgetSubtype = budgetMapper.toEntity(budgetSubtypeDTO);
        budgetSubtype.setBudgetType(budgetType);
        budgetSubtypeRepository.save(budgetSubtype);

        BudgetSubtypeDTO savedBudgetSubtypeDTO = budgetMapper.toDTO(budgetSubtype);
        kafkaBudgetSubtypeTemplate.send("budget-subtype-response", savedBudgetSubtypeDTO);
        return savedBudgetSubtypeDTO;
    }

    @Override
    @KafkaListener(topics = "update-budget-subtype", groupId = "budget_subtype_group", concurrency = "10")
    public BudgetSubtypeDTO updateBudgetSubtype(BudgetSubtypeDTO budgetSubtypeDTO) throws BudgetSubtypeNotFoundException, BudgetSubtypeAlreadyExistsException {
        BudgetSubtype budgetSubtype = budgetSubtypeRepository.findById(budgetSubtypeDTO.getId()).orElseThrow(() -> new BudgetSubtypeNotFoundException(budgetSubtypeDTO.getId()));

        BudgetValidator.checkForExistingBudgetSubtypeUpdate(budgetSubtypeDTO, budgetSubtypeRepository);

        budgetMapper.updateFromDTO(budgetSubtypeDTO, budgetSubtype);
        budgetSubtypeRepository.save(budgetSubtype);
        BudgetSubtypeDTO savedBudgetSubtypeDTO = budgetMapper.toDTO(budgetSubtype);
        kafkaBudgetSubtypeTemplate.send("budget-subtype-response", savedBudgetSubtypeDTO);

        return savedBudgetSubtypeDTO;
    }

    @Override
    @KafkaListener(topics = "delete-budget-subtype", groupId = "uuid_group", concurrency = "10")
    public void deleteBudgetSubtype(UUID subtypeId) throws BudgetSubtypeNotFoundException {
        if (!budgetSubtypeRepository.existsById(subtypeId)) {
            throw new BudgetSubtypeNotFoundException(subtypeId);
        }
        budgetSubtypeRepository.deleteById(subtypeId);
    }

    @Override
    @KafkaListener(topics = "find-all-budget-subtype", groupId = "budget_subtype_group", concurrency = "10")
    public Page<BudgetSubtypeDTO> findAllBudgetSubtypes(Pageable pageable) throws JsonProcessingException {
        Page<BudgetSubtype> budgetSubtypePage = budgetSubtypeRepository.findAll(pageable);
        kafkaStringTemplate.send("budget-subtype-response", objectMapper.writeValueAsString(budgetSubtypePage));
        return budgetSubtypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetSubtype findBudgetSubtypeEntityById(UUID id) throws BudgetSubtypeNotFoundException {
        return findById(id);
    }

    @Override
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

    private BudgetSubtype findById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetSubtypeRepository.findById(id).orElseThrow(() -> new BudgetSubtypeNotFoundException(id));

    }
}