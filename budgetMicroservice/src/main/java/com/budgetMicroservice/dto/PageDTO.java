package com.budgetMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    public static <T> PageDTO<T> from(Page<T> page) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setContent(page.getContent());
        pageDTO.setPageNumber(page.getNumber());
        pageDTO.setPageSize(page.getSize());
        pageDTO.setTotalElements(page.getTotalElements());
        return pageDTO;
    }

    public Page<T> toPage() {
        return new PageImpl<>(content, PageRequest.of(pageNumber, pageSize), totalElements);
    }
}
