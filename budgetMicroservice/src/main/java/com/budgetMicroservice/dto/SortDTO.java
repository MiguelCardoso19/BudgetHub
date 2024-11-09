package com.budgetMicroservice.dto;

import com.budgetMicroservice.enumerator.SortDirection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SortDTO {
    private boolean sorted;
    private boolean unsorted;
    private boolean empty;
    private String field;
    private SortDirection direction;
}
