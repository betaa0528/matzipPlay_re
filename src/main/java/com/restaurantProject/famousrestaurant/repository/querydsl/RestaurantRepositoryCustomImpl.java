package com.restaurantProject.famousrestaurant.repository.querydsl;

import com.querydsl.core.QueryResults;
import com.restaurantProject.famousrestaurant.entity.QRestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class RestaurantRepositoryCustomImpl extends QuerydslRepositorySupport implements RestaurantRepositoryCustom {


    public RestaurantRepositoryCustomImpl() {
        super(RestaurantEntity.class);
    }

    @Override
    public List<RestaurantEntity> findByRestaurantNameOrAddress(String keyword) {

        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity;

        return from(restaurant)
                .distinct().select(restaurant)
                .where(restaurant.restaurantName.containsIgnoreCase(keyword)
                        .or(restaurant.restaurantAddress.containsIgnoreCase(keyword)))
                .fetch();

    }

    @Override
    public PageImpl<RestaurantEntity> SearchRestaurantOrderByReview(String keyword, Pageable pageable) {
        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity;

        QueryResults result = from(restaurant).distinct().select(restaurant)
                .where(restaurant.restaurantName.containsIgnoreCase(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.reviewEntity.size().desc())
                .orderBy(restaurant.restaurantName.asc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public PageImpl<RestaurantEntity> SearchRestaurantOrderByWish(String keyword, Pageable pageable) {
        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity;

        QueryResults result = from(restaurant).distinct().select(restaurant)
                .where(restaurant.restaurantName.containsIgnoreCase(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.wishListEntity.size().desc())
                .orderBy(restaurant.restaurantName.asc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public PageImpl<RestaurantEntity> SearchAddressOrderByReview(String keyword, Pageable pageable) {
        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity;

        QueryResults result = from(restaurant).distinct().select(restaurant)
                .where(restaurant.restaurantAddress.containsIgnoreCase(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.reviewEntity.size().desc())
                .orderBy(restaurant.restaurantName.asc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public PageImpl<RestaurantEntity> SearchAddressOrderByWish(String keyword, Pageable pageable) {
        QRestaurantEntity restaurant = QRestaurantEntity.restaurantEntity;

        QueryResults result = from(restaurant).distinct().select(restaurant)
                .where(restaurant.restaurantAddress.containsIgnoreCase(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.wishListEntity.size().desc())
                .orderBy(restaurant.restaurantName.asc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
