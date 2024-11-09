package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.model.Movement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @Mapping(target = "budgetType.subtypes", ignore = true)
    @Mapping(target = "budgetSubtype.budgetType", ignore = true)
    @Mapping(target = "invoice.movement", ignore = true)
    MovementDTO toDTO(Movement movement);

    Movement toEntity(MovementDTO movementDTO);

    @Named("toDTOWithoutBudgetType")
    @Mapping(target = "budgetType", ignore = true)
    @Mapping(target = "invoice.movement", ignore = true)
    MovementDTO toDTOWithoutBudgetType(Movement movement);

    @Named("toDTOWithoutBudgetSubtype")
    @Mapping(target = "budgetSubtype", ignore = true)
    @Mapping(target = "invoice.movement", ignore = true)
    MovementDTO toDTOWithoutBudgetSubtype(Movement movement);

    List<MovementDTO> toDTOList(Page<Movement> movementPage);
}
