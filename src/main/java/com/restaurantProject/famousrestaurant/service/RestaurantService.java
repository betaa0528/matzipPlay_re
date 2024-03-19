package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.geo.GeoTrans;
import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final NaverClient naverClient;

    public RestaurantService(RestaurantRepository restaurantRepository, MemberRepository memberRepository, NaverClient naverClient) {
        this.restaurantRepository = restaurantRepository;
        this.memberRepository = memberRepository;
        this.naverClient = naverClient;
    }

    public List<Restaurant> findAll() {
        List<RestaurantEntity> restaurantEntityList = restaurantRepository.findAll();
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntityList) {
            restaurants.add(Restaurant.from(restaurantEntity));
        }

        return restaurants;
    }

    public Restaurant findById(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id).get();
        if (restaurantEntity.getImgLink() == null) {
            restaurantEntity.setImgLink(imgSearch(restaurantEntity.getRestaurantName()));
        }
        return Restaurant.from(restaurantEntity);
    }

    public String imgSearch(String query) {
        SearchImageReq searchImageReq = new SearchImageReq();
        searchImageReq.setQuery(query);
        SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);

        SearchImageRes.SearchImageItem searchImageItem = null;
        if (searchImageRes.getItems().stream().findFirst().isPresent()) {
            searchImageItem = searchImageRes.getItems().stream().findFirst().get();
        }


        if (searchImageItem == null) {
            return null;
        } else {
            return searchImageItem.getLink();
        }
    }

    private void imgSave(Page<RestaurantEntity> restaurants) {
        for (RestaurantEntity restaurantEntity : restaurants) {
            if (restaurantEntity.getImgLink() == null) {
                restaurantEntity.setImgLink(imgSearch(restaurantEntity.getRestaurantName()));
                restaurantRepository.save(restaurantEntity);
            }
        }
    }

    public Page<Restaurant> categoryPaging(String category, Pageable pageable, String order) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 4;
        Page<RestaurantEntity> restaurantEntities;
        switch (order) {
            case "review":
                restaurantEntities = restaurantRepository.findByCategoryOrderByReviewDesc(category, PageRequest.of(page, pageLimit, Sort.by("restaurantName")));
                break;
            case "wish":
                restaurantEntities = restaurantRepository.findByCategoryOrderByWishDesc(category, PageRequest.of(page, pageLimit, Sort.by("restaurantName")));
                break;
            default:
                restaurantEntities = restaurantRepository.findByCategory(category, PageRequest.of(page, pageLimit));
                break;
        }

        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (restaurantEntity.getImgLink() == null) {
                restaurantEntity.setImgLink(imgSearch(restaurantEntity.getRestaurantName()));
                restaurantRepository.save(restaurantEntity);
            }
        }
        Page<Restaurant> restaurants = restaurantEntities.map(Restaurant::from);
        return restaurants;
    }


    public Page<Restaurant> search(String keyword, Pageable pageable, String order) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 4;
        Page<RestaurantEntity> searchByName;

        switch (order){
            case "wish" :
                searchByName = restaurantRepository.SearchRestaurantOrderByWish(keyword,PageRequest.of(page,pageLimit));
                break;
            default:
                searchByName = restaurantRepository.SearchRestaurantOrderByReview(keyword, PageRequest.of(page,pageLimit));
                break;
        }

        imgSave(searchByName);
        if (searchByName.isEmpty()) {
            Page<RestaurantEntity> searchByAddress;
            switch (order){
                case "wish" :
                    searchByAddress = restaurantRepository.SearchAddressOrderByWish(keyword,PageRequest.of(page,pageLimit));
                    break;
                default:
                    searchByAddress = restaurantRepository.SearchAddressOrderByReview(keyword, PageRequest.of(page,pageLimit));
                    break;
            }
            if (searchByAddress.isEmpty()) return Page.empty();
            else {
                imgSave(searchByAddress);
                Page<Restaurant> searchResult = searchByAddress.map(Restaurant::from);
                return searchResult;
            }
        }

        return searchByName.map(Restaurant::from);
    }


    public void sortByDistance(List<Restaurant> restaurantList, GeoPoint memberPt) {
        Comparator<Restaurant> distanceComparator = (r1, r2) -> {
            GeoPoint pt1 = new GeoPoint(r1.getMapX(), r1.getMapY());
            GeoPoint pt2 = new GeoPoint(r2.getMapX(), r2.getMapY());
            double distanceToR1 = GeoTrans.getDistancebyGeo(pt1, memberPt);
            double distanceToR2 = GeoTrans.getDistancebyGeo(pt2, memberPt);
            r1.setDistance(Math.round(distanceToR1 * 100) / 100.0);
            r2.setDistance(Math.round(distanceToR2 * 100) / 100.0);
            return Double.compare(distanceToR1, distanceToR2);
        };

        restaurantList.sort(distanceComparator);
    }

    public void saveDistance(HttpSession session) {
        if (session.getAttribute("memberId") != null) {
            String memberId = (String) session.getAttribute("memberId");
            System.out.println(memberId);
            MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get(0);
            double mapx = Double.parseDouble(memberEntity.getMapX());
            double mapy = Double.parseDouble(memberEntity.getMapY());
            GeoPoint userPt = new GeoPoint(mapx, mapy);
            List<RestaurantEntity> all = restaurantRepository.findAll();
            for (RestaurantEntity restaurantEntity : all) {
                double distanceByGeo = GeoTrans.getDistancebyGeo(userPt, new GeoPoint(restaurantEntity.getMapX(), restaurantEntity.getMapY()));
                restaurantEntity.setDistance(Math.round(distanceByGeo * 100) / 100.0);
                restaurantRepository.save(restaurantEntity);
            }
        } else {
            GeoPoint basicPt = new GeoPoint(126.9783785, 37.5666612); // session 없으면 서울시청의 좌표값을 기본으로 넣음
            List<RestaurantEntity> all = restaurantRepository.findAll();
            for (RestaurantEntity restaurantEntity : all) {
                double distanceByGeo = GeoTrans.getDistancebyGeo(basicPt, new GeoPoint(restaurantEntity.getMapX(), restaurantEntity.getMapY()));
                restaurantEntity.setDistance(Math.round(distanceByGeo * 100) / 100.0);
                restaurantRepository.save(restaurantEntity);
            }
        }
    }


}
