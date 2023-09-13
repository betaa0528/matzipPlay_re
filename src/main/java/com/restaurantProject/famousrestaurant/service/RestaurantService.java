package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalReq;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final NaverClient naverClient;

    public List<Restaurant> findAll() {
        List<RestaurantEntity> restaurantEntityList = restaurantRepository.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            restaurants.add(Restaurant.toRestaurant(restaurantEntity));
        }

        return restaurants;
    }

    public Restaurant findById(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id).get();
        return Restaurant.toRestaurant(restaurantEntity);
    }

    public String search(String query){
        SearchImageReq searchImageReq = new SearchImageReq();
        searchImageReq.setQuery(query);
        SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);

        SearchImageRes.SearchImageItem searchImageItem = searchImageRes.getItems().stream().findFirst().get();
        String link = searchImageItem.getLink();

        return link;
    }

    public List<RestaurantEntity> findByCategory(String category) {
         return restaurantRepository.findByCategory(category);
    }

    public Page<Restaurant> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;
        Page<RestaurantEntity> restaurantEntities =
                restaurantRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("restaurantEntities.getContent() : " + restaurantEntities.getContent()); // 요청페이지에 해당하는글
        System.out.println("restaurantEntities.getTotalElements() : " + restaurantEntities.getTotalElements()); // 전체 글갯수
        System.out.println("restaurantEntities.getNumber() : " + restaurantEntities.getNumber()); // DB로 요청한 페이지번호
        System.out.println("restaurantEntities.getTotalPages : "  + restaurantEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("restaurantEntities.getSize : " + restaurantEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("restaurantEntities.hasPrevious : " + restaurantEntities.hasPrevious()); // 이전 페이지 존재여부
        System.out.println("restaurantEntities.isFirst : " + restaurantEntities.isFirst()); // 첫 페이지 여부
        System.out.println("restaurantEntities.isLast : " + restaurantEntities.isLast()); // 마지막 페이지 여부


        Page<Restaurant> restaurants = restaurantEntities.map(rest -> new Restaurant(rest.getId(), rest.getRestaurantName(), rest.getRestaurantAddress(), rest.getRestaurantRoadAddress(), rest.getCategory()));
        return restaurants;
    }

    public Page<Restaurant> categoryPaging(String category, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 3;
        Page<RestaurantEntity> restaurantEntities =
                restaurantRepository.findByCategory(category, PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("restaurantEntities.getContent() : " + restaurantEntities.getContent()); // 요청페이지에 해당하는글
        System.out.println("restaurantEntities.getTotalElements() : " + restaurantEntities.getTotalElements()); // 전체 글갯수
        System.out.println("restaurantEntities.getNumber() : " + restaurantEntities.getNumber()); // DB로 요청한 페이지번호
        System.out.println("restaurantEntities.getTotalPages : "  + restaurantEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("restaurantEntities.getSize : " + restaurantEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("restaurantEntities.hasPrevious : " + restaurantEntities.hasPrevious()); // 이전 페이지 존재여부
        System.out.println("restaurantEntities.isFirst : " + restaurantEntities.isFirst()); // 첫 페이지 여부
        System.out.println("restaurantEntities.isLast : " + restaurantEntities.isLast()); // 마지막 페이지 여부


        Page<Restaurant> restaurants = restaurantEntities.map(rest -> new Restaurant(rest.getId(), rest.getRestaurantName(), rest.getRestaurantAddress(), rest.getRestaurantRoadAddress(), rest.getCategory()));
        return restaurants;
    }
}
