package com.portalMicroservice.util;

import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.SortDTO;
import com.portalMicroservice.enumerator.SortDirection;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class PageableUtils {

    public static CustomPageableDTO convertToCustomPageable(Pageable pageable) {
        String sortField = pageable.getSort().isSorted()
                ? pageable.getSort().iterator().next().getProperty()
                : null;

        String sortOrder = pageable.getSort().isSorted()
                ? pageable.getSort().iterator().next().getDirection().name()
                : "ASC";

        SortDTO sortDTO = new SortDTO();
        sortDTO.setSorted(pageable.getSort().isSorted());
        sortDTO.setUnsorted(pageable.getSort().isUnsorted());
        sortDTO.setEmpty(pageable.getSort().isEmpty());
        sortDTO.setField(sortField);
        sortDTO.setDirection(SortDirection.valueOf(sortOrder));

        return new CustomPageableDTO(UUID.randomUUID(), pageable.getPageNumber(), pageable.getPageSize(), (int) pageable.getOffset(), pageable.isPaged(), pageable.isUnpaged(), sortDTO);
    }
}