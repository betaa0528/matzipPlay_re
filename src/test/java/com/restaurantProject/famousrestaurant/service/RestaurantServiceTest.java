package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.geo.GeoTrans;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class RestaurantServiceTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void pageTest() {

        int page = 2;
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
        GeoPoint userPt = new GeoPoint(127.45247758491,36.3186118269791);

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
                GeoPoint pt1= new GeoPoint(restaurant.getMapX(), restaurant.getMapY());
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
}
