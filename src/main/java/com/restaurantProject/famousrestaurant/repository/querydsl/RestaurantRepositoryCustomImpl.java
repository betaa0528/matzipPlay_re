package com.restaurantProject.famousrestaurant.repository.querydsl;

import com.restaurantProject.famousrestaurant.entity.QRestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
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


//        return jpaQueryFactory.selectFrom(restaurantEntity)
//                .where(containName(keyword), containAddress(keyword)).fetch();

//    private BooleanExpression containAddress(String address) {
//        if (address ==null || address.isEmpty()) {
//            return null;
//        }
//        return restaurantEntity.restaurantAddress.containsIgnoreCase(address);
//    }
//
//    private BooleanExpression containName(String restaurantName) {
//        if(restaurantName == null || restaurantName.isEmpty()) {
//            return null;
//        }
//
//        return restaurantEntity.restaurantName.containsIgnoreCase(restaurantName);
//    }
}
