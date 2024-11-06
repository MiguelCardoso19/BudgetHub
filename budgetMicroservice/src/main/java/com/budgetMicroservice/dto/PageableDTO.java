package com.budgetMicroservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize
@Data
public class PageableDTO {
    private int pageNumber;
    private int pageSize;

    public Pageable toPageable() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
