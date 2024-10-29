package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BudgetTypeMapper {

    @Mapping(target = "budgetTypeId", source = "budgetType.id")
    BudgetSubtypeDTO toDTO(BudgetSubtype budgetSubtype);

    BudgetTypeDTO toDTO(BudgetType budgetType);

    BudgetType toEntity(BudgetTypeDTO budgetTypeDTO);

    BudgetSubtype toEntity(BudgetSubtypeDTO budgetSubtypeDTO);

    void updateFromDTO(BudgetTypeDTO budgetTypeDTO, @MappingTarget BudgetType budgetType);

    void updateFromDTO(BudgetSubtypeDTO budgetSubtypeDTO, @MappingTarget BudgetSubtype budgetSubtype);
}