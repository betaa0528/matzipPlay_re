package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.geo.GeoTrans;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DisplayName("비즈니스 로직 - 음식점")
//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RestaurantServiceTest {

//    @InjectMocks
//    private RestaurantService sut;
//    @Mock
//    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private WishListRepository wishListRepository;

    @Test
    public void pageTest() {

        int page = 2;
        int pageLimit = 3;
        Page<RestaurantEntity> restaurantEntities =
                restaurantRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("restaurantEntities.getContent() : " + restaurantEntities.getContent()); // 요청페이지에 해당하는글
        System.out.println("restaurantEntities.getTotalElements() : " + restaurantEntities.getTotalElements()); // 전체 글갯수
        System.out.println("restaurantEntities.getNumber() : " + restaurantEntities.getNumber()); // DB로 요청한 페이지번호
        System.out.println("restaurantEntities.getTotalPages : " + restaurantEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("restaurantEntities.getSize : " + restaurantEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("restaurantEntities.hasPrevious : " + restaurantEntities.hasPrevious()); // 이전 페이지 존재여부
        System.out.println("restaurantEntities.isFirst : " + restaurantEntities.isFirst()); // 첫 페이지 여부
        System.out.println("restaurantEntities.isLast : " + restaurantEntities.isLast()); // 마지막 페이지 여부
        GeoPoint userPt = new GeoPoint(127.45247758491, 36.3186118269791);

        Page<Restaurant> restaurants = restaurantEntities.map(restaurantEntity -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(restaurantEntity.getId());
            restaurant.setRestaurantName(restaurantEntity.getRestaurantName());
            restaurant.setRestaurantAddress(restaurantEntity.getRestaurantAddress());
            restaurant.setRestaurantRoadAddress(restaurantEntity.getRestaurantRoadAddress());
            restaurant.setImgLink(restaurantEntity.getImgLink());
            restaurant.setCategory(restaurantEntity.getCategory());
            restaurant.setMapX(restaurantEntity.getMapX());
            restaurant.setMapY(restaurantEntity.getMapY());
            GeoPoint pt1 = new GeoPoint(restaurant.getMapX(), restaurant.getMapY());
            GeoPoint convert = GeoTrans.convert(GeoTrans.TM, GeoTrans.GEO, pt1);
            restaurant.setDistance(GeoTrans.getDistancebyGeo(userPt, convert));
            return restaurant;
        });

        List<Restaurant> sortedRestaurants = restaurants.getContent()
                .stream()
                .sorted(Comparator.comparing(Restaurant::getDistance)) // 레스토랑 이름을 기준으로 정렬
                .collect(Collectors.toList());

        sortedRestaurants.stream().forEach(System.out::println);

        restaurants.stream().forEach(System.out::println);
    }

    @DisplayName("음식점 검색 - 음식점명이나 주소에 검색어가 포함되면 결과를 반환한다.")
    @Test
    void givenSearchParameter_whenSearchingRestaurants_thenRestaurantList() {
        // Given
        String keyword = "테스트";
        Pageable pageable = Pageable.ofSize(10);

        given(restaurantRepository.findByRestaurantNameOrAddress(keyword)).willReturn(List.of());

        // When
//        Page<Restaurant> restaurantList = sut.search(keyword, pageable);

        // Then
//        assertThat(restaurantList).isEmpty();
    }


    @DisplayName("음식점 검색 - 검색어 키워드 중 음식점이 있으면 음식점을 반환한다.")
    @Test
    public void givenSearchKeyword_whenSearchingParameterRestaurantsName_thenReturnsMatchingRestaurants() {
        // Given
        String keyword = "테스트";
        RestaurantEntity restaurant1 = new RestaurantEntity();
        restaurant1.setId(1L);
        restaurant1.setRestaurantName("테스트 음식점");
        restaurant1.setRestaurantAddress("그냥 주소");

        RestaurantEntity restaurant2 = new RestaurantEntity();
        restaurant2.setId(2L);
        restaurant2.setRestaurantName("다른 음식점");
        restaurant2.setRestaurantAddress("다른 주소");

        RestaurantEntity restaurant3 = new RestaurantEntity();
        restaurant3.setId(3L);
        restaurant3.setRestaurantName("다른 음식점");
        restaurant3.setRestaurantAddress("테스트 주소");

        restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));
        Pageable pageable = Pageable.ofSize(10);

        // When
        Page<RestaurantEntity> paging = restaurantRepository.findByRestaurantNameContaining(keyword, pageable);
//        Page<Restaurant> result = sut.search(keyword, pageable);

        // Then
        assertEquals("테스트 음식점", paging.getContent().get(0).getRestaurantName());
//        assertEquals(1, result.getTotalElements());
    }

    @DisplayName("음식점 정렬 - 타입에 따라 정렬한 음식점들을 보여준다")
    @Test
    void givenOrderType_whenSearchingRestaurant_thenReturnsOrderedRestaurants() {
        WishListEntity wish1 = new WishListEntity();
        wish1.setId(1L);
        wish1.setRestaurantId(11L);

        WishListEntity wish2 = new WishListEntity();
        wish2.setId(2L);
        wish2.setRestaurantId(11L);

        List<WishListEntity> list = new ArrayList<>();
        wishListRepository.saveAll(List.of(wish1, wish2));
        list.add(wish1);
        list.add(wish2);

//        restaurantRepository.saveAll(List.of(restaurant1, restaurant2, restaurant3));
//        System.out.println(restaurantRepository.findAll());
        Page<RestaurantEntity> page = restaurantRepository.findByRestaurantNameContaining("초밥", PageRequest.of(0, 10));
//        page.getContent().forEach(System.out::println);
        page.getContent().get(0).setWishListEntity(list);
        int size = page.getContent().get(0).getWishListEntity().size();
        assertEquals(3, page.getTotalElements());
        assertEquals(2, size);
    }

    @Test
    void givenKeyword_whenSearchingRestaurant_thenOrderByReviewCount() {
        Page<RestaurantEntity> sushi = restaurantRepository.SearchRestaurantOrderByWish(
                "마라탕", PageRequest.of(0, 4,Sort.by("restaurantName")));
//        Page<RestaurantEntity> sushi1 = restaurantRepository.findByRestaurantNameContainingOrderByReview(
//                "마라탕", PageRequest.of(1, 4,Sort.by("restaurantName")));
//        sushi.forEach(System.out::println);
//        System.out.println("=====================");
//        sushi1.forEach(System.out::println);
        assertEquals("라화방 마라탕", sushi.getContent().get(0).getRestaurantName());
    }

}
