package com.restaurantProject.famousrestaurant.dto.response;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PageResponse<T> {

    private List<T> list = new ArrayList<>();
    private Pageable pageable;

    public PageResponse(List<T> list, Pageable pageable) {
        this.list.addAll(list);
        this.pageable = pageable;
    }


}
