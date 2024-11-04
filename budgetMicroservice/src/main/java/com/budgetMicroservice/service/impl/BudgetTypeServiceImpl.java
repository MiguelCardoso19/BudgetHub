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
public class BudgetTypeServiceImpl implements BudgetTypeService {
    private final BudgetTypeRepository budgetTypeRepository;
    private final BudgetMapper budgetMapper;
 //   private final KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate;
 //   private final KafkaTemplate<String, String> kafkaStringTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
  //  @KafkaListener(topics = "add-budget-type", groupId = "budget_type_group", concurrency = "10")
    public BudgetTypeDTO createBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeAlreadyExistsException {
        BudgetValidator.checkForExistingBudgetType(budgetTypeDTO, budgetTypeRepository);

        BudgetType budgetType = budgetMapper.toEntity(budgetTypeDTO);
        budgetTypeRepository.save(budgetType);
        BudgetTypeDTO savedBudgetTypeDTO = budgetMapper.toDTO(budgetType);
     //   kafkaBudgetTypeTemplate.send("add-budget-type", savedBudgetTypeDTO);

        return savedBudgetTypeDTO;
    }

    @Override
  //  @KafkaListener(topics = "update-budget-type", groupId = "budget_type_group", concurrency = "10")
    public BudgetTypeDTO updateBudgetType(BudgetTypeDTO budgetTypeDTO) throws BudgetTypeNotFoundException, BudgetTypeAlreadyExistsException {
        BudgetType budgetType = budgetTypeRepository.findById(budgetTypeDTO.getId()).orElseThrow(() -> new BudgetTypeNotFoundException(budgetTypeDTO.getId()));

        BudgetValidator.checkForExistingBudgetTypeUpdate(budgetTypeDTO, budgetTypeRepository);

        budgetMapper.updateFromDTO(budgetTypeDTO, budgetType);
        budgetTypeRepository.save(budgetType);
        BudgetTypeDTO savedBudgetTypeDTO = budgetMapper.toDTO(budgetType);

    //    kafkaBudgetTypeTemplate.send("update-budget-type", savedBudgetTypeDTO);
        return savedBudgetTypeDTO;
    }

    @Override
//    @KafkaListener(topics = "delete-budget-type", groupId = "budget_type_group", concurrency = "10")
    public void deleteBudgetType(UUID id) throws BudgetTypeNotFoundException {
        if (!budgetTypeRepository.existsById(id)) {
            throw new BudgetTypeNotFoundException(id);
        }

        budgetTypeRepository.deleteById(id);
    }

    @Override
 //   @KafkaListener(topics = "find-all-budget-type", groupId = "budget_type_group", concurrency = "10")
    public Page<BudgetTypeDTO> findAllBudgetTypes(Pageable pageable) throws JsonProcessingException {
        Page<BudgetType> budgetTypePage = budgetTypeRepository.findAll(pageable);
      //  kafkaStringTemplate.send("find-all-budget-types", objectMapper.writeValueAsString(budgetTypePage));
        return budgetTypePage.map(budgetMapper::toDTO);
    }

    @Override
    public BudgetType findBudgetTypeEntityById(UUID id) throws BudgetSubtypeNotFoundException {
        return findById(id);
    }

    @Override
  //  @KafkaListener(topics = "find-budget-type-by-id", groupId = "budget_type_group", concurrency = "10")
    public BudgetTypeDTO findBudgetTypeDTOById(UUID id) throws BudgetSubtypeNotFoundException {
        BudgetTypeDTO budgetTypeDTO = budgetMapper.toDTO(findById(id));
    //    kafkaBudgetTypeTemplate.send("find-budget-type-by-id", budgetTypeDTO);
        return budgetTypeDTO;
    }

    @Override
    public void save(BudgetType budgetType) {
        budgetTypeRepository.save(budgetType);
    }

    private BudgetType findById(UUID id) throws BudgetSubtypeNotFoundException {
        return budgetTypeRepository.findById(id).orElseThrow(() -> new BudgetSubtypeNotFoundException(id));
    }
}
