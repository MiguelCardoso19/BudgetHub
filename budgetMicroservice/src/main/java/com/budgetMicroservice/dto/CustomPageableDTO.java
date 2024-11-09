package com.budgetMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomPageableDTO {
    private int pageNumber;
    private int pageSize;
    private int offset;
    private boolean paged;
    private boolean unpaged;
    private SortDTO sort;

    public String getSortField() {
        return sort != null ? sort.getField() : null;
    }

    public String getSortOrder() {
        return sort != null && sort.getDirection() != null ? sort.getDirection().name() : "ASC";
    }
}
