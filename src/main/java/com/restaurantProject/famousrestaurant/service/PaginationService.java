package com.restaurantProject.famousrestaurant.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    private static final int BAR_LENGTH = 5;

    private List<Integer> pageNumberList;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages) {
        int startNumber = Math.max(currentPageNumber - (BAR_LENGTH / 2) , 1); // 시작페이지를 1로 주기위함
        int endNumber = Math.min(startNumber + BAR_LENGTH , totalPages + 1);

        pageNumberList = IntStream.range(startNumber, endNumber).boxed().collect(Collectors.toList());
        return pageNumberList;
    }
}
