package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    Invoice toEntity(InvoiceDTO invoiceDTO);
    InvoiceDTO toDTO(Invoice invoice);
    void updateFromDTO(InvoiceDTO invoiceDTO, @MappingTarget Invoice invoice);
}
