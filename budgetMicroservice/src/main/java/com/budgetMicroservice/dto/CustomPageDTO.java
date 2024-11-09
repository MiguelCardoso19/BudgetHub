package com.budgetMicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CustomPageDTO<T> {
    private List<T> content;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private CustomPageableDTO pageable;
    private boolean last;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    public CustomPageDTO(List<T> content, int pageSize, long totalElements,
                         CustomPageableDTO pageable, boolean last, boolean first,
                         int numberOfElements, boolean empty) {
        this.content = content;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.pageable = pageable;
        this.last = last;
        this.first = first;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
    }
}
