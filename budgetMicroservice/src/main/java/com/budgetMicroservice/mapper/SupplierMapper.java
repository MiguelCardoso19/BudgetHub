package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    Supplier toEntity(SupplierDTO supplierDTO);
    SupplierDTO toDTO(Supplier supplier);
    void updateFromDTO(SupplierDTO supplierDTO, @MappingTarget Supplier supplier);
}
