package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.model.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toEntity(SupplierDTO supplierDTO);
    SupplierDTO toDTO(Supplier supplier);
    List<SupplierDTO> toDTOList(Page<Supplier> suppliers);
}
