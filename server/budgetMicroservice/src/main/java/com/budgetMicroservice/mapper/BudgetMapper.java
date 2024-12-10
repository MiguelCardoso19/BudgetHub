package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.BudgetSubtypeDTO;
import com.budgetMicroservice.dto.BudgetTypeDTO;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(target = "budgetType.subtypes", ignore = true)
    BudgetSubtypeDTO toDTO(BudgetSubtype budgetSubtype);

    @Mapping(target = "subtypes", qualifiedByName = "withoutBudgetType")
    BudgetTypeDTO toDTO(BudgetType budgetType);

    BudgetType toEntity(BudgetTypeDTO budgetTypeDTO);

    BudgetSubtype toEntity(BudgetSubtypeDTO budgetSubtypeDTO);

    List<BudgetTypeDTO> toDTOTypeList(Page<BudgetType> budgetTypePage);

    List<BudgetSubtypeDTO> toDTOSubtypeList(Page<BudgetSubtype> budgetSubtypePage);

    @Named("withoutBudgetType")
    default List<BudgetSubtypeDTO> mapSubtypesWithoutBudgetType(List<BudgetSubtype> subtypes) {
        if (subtypes == null) {
            return Collections.emptyList();
        }

        return subtypes.stream().map(
                        subtype -> {
                            BudgetSubtypeDTO dto = toDTO(subtype);
                            dto.setBudgetType(null);
                            return dto;
                        })
                .collect(Collectors.toList());
    }
}