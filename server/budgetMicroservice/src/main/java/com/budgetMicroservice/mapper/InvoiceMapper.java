package com.budgetMicroservice.mapper;

import com.budgetMicroservice.dto.InvoiceDTO;
import com.budgetMicroservice.dto.MovementDTO;
import com.budgetMicroservice.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    Invoice toEntity(InvoiceDTO invoiceDTO);

    @Mapping(target = "movement.budgetType.subtypes", ignore = true)
    @Mapping(target = "movement.subtype.budgetType", ignore = true)
    @Mapping(target = "movement.invoice", ignore = true)
    InvoiceDTO toDTO(Invoice invoice);

    List<MovementDTO> toDTOList(Page<Invoice> invoicesPage);
}
