package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.model.Movement;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementDTO toDTO(Movement movement);
    Movement toEntity(MovementDTO movementDTO);

}
