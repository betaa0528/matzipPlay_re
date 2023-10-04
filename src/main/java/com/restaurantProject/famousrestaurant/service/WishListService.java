package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.WishList;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.entity.WishListEntity;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import com.restaurantProject.famousrestaurant.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository repository;
    private final RestaurantRepository restaurantRepository;

    public int updateWishList(WishList wishList) {
        int chk = wishListCheck(wishList.getMemberWishId(), wishList.getRestaurantId());
        RestaurantEntity re = restaurantRepository.findById(wishList.getRestaurantId()).get();


        if(chk == 0){
            repository.save(WishListEntity.toWishListEntity(wishList,re)); // db에 없는경우, DTO객체를 Entity로 만들어서 새롭게 저장
            return 1;
        } else {
            repository.deleteById(getWishListEntity(wishList.getMemberWishId(), wishList.getRestaurantId()).getId()); // db에 있으므로 저장된 Entity의 Id로 삭제
            return 0;
        }
    }

    public WishListEntity getWishListEntity(String memberId, Long id) {
        return repository.findByMemberWishIdAndRestaurantId(memberId, id);
    }

    public int wishListCheck(String memberId, Long id) {
        WishListEntity wishListEntity = getWishListEntity(memberId, id);
        if(wishListEntity!=null) {
            return 1; // 찜이 되었을 때
        } else {
            return 0; // 찜 하지 않았을때
        }

    }
}
