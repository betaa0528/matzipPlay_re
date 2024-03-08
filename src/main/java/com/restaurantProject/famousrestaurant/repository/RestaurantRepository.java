package com.restaurantProject.famousrestaurant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.restaurantProject.famousrestaurant.entity.QRestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.ReviewEntity;
import com.restaurantProject.famousrestaurant.repository.querydsl.RestaurantRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends
        JpaRepository<RestaurantEntity, Long>,
        RestaurantRepositoryCustom,
        QuerydslPredicateExecutor<RestaurantEntity>,
        QuerydslBinderCustomizer<QRestaurantEntity> {
    Page<RestaurantEntity> findByRestaurantNameContaining(String keyword, Pageable pageable);
    Page<RestaurantEntity> findByRestaurantAddressContaining(String keyword, Pageable pageable);

    List<RestaurantEntity> findByCategory(String category);

    Page<RestaurantEntity> findByCategory(String category, Pageable pageable);


    @Override
    default void customize(QuerydslBindings bindings, QRestaurantEntity root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.restaurantName, root.restaurantAddress);
        bindings.bind(root.restaurantName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.restaurantAddress).first(StringExpression::containsIgnoreCase);
    }

}
