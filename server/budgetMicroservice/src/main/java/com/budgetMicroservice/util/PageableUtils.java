package com.budgetMicroservice.util;

import com.budgetMicroservice.dto.CustomPageDTO;
import com.budgetMicroservice.dto.CustomPageableDTO;
import com.budgetMicroservice.dto.SortDTO;
import com.budgetMicroservice.enumerator.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
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

    public static Pageable convertToPageable(CustomPageableDTO customPageableDTO) {
        Sort.Direction direction = "desc".equalsIgnoreCase(customPageableDTO.getSortOrder())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        String sortField = customPageableDTO.getSortField() != null ? customPageableDTO.getSortField() : "id";

        return PageRequest.of(
                customPageableDTO.getPageNumber(),
                customPageableDTO.getPageSize(),
                Sort.by(direction, sortField)
        );
    }

    public static <T> CustomPageDTO<T> buildCustomPageDTO(CustomPageableDTO customPageableDTO, List<T> dtoList, Page<?> entityPage) {

        CustomPageableDTO pageableDTO = new CustomPageableDTO(
                customPageableDTO.getCorrelationId(),
                customPageableDTO.getPageNumber(),
                customPageableDTO.getPageSize(),
                customPageableDTO.getOffset(),
                customPageableDTO.isPaged(),
                customPageableDTO.isUnpaged(),
                customPageableDTO.getSort()
        );

        return new CustomPageDTO<>(
                dtoList,
                customPageableDTO.getPageSize(),
                entityPage.getTotalElements(),
                pageableDTO,
                entityPage.isLast(),
                entityPage.isFirst(),
                entityPage.getNumberOfElements(),
                entityPage.isEmpty()
        );
    }
}