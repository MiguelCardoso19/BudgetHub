package com.portalMicroservice.dto.budget;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageableDTO {
    private int pageNumber;
    private int pageSize;
    private int offset;
    private boolean paged;
    private boolean unpaged;
    private SortDTO sort;

    @JsonIgnore
    public String getSortField() {
        return sort != null ? sort.getField() : null;
    }

    @JsonIgnore
    public String getSortOrder() {
        return sort != null && sort.getDirection() != null ? sort.getDirection().name() : "ASC";
    }
}