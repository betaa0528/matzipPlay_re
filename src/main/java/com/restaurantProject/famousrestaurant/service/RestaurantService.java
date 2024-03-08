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

    public Page<Restaurant> categoryPaging(String category, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 4;
        Page<RestaurantEntity> restaurantEntities =
                restaurantRepository.findByCategory(category, PageRequest.of(page, pageLimit));

        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            if (restaurantEntity.getImgLink() == null) {
                restaurantEntity.setImgLink(imgSearch(restaurantEntity.getRestaurantName()));
                restaurantRepository.save(restaurantEntity);
            }
        }
//        System.out.println("restaurantEntities.getContent() : " + restaurantEntities.getContent()); // 요청페이지에 해당하는글
//        System.out.println("restaurantEntities.getTotalElements() : " + restaurantEntities.getTotalElements()); // 전체 글갯수
//        System.out.println("restaurantEntities.getNumber() : " + restaurantEntities.getNumber()); // DB로 요청한 페이지번호
//        System.out.println("restaurantEntities.getTotalPages : " + restaurantEntities.getTotalPages()); // 전체 페이지 갯수
//        System.out.println("restaurantEntities.getSize : " + restaurantEntities.getSize()); // 한 페이지에 보여지는 글 갯수
//        System.out.println("restaurantEntities.hasPrevious : " + restaurantEntities.hasPrevious()); // 이전 페이지 존재여부
//        System.out.println("restaurantEntities.isFirst : " + restaurantEntities.isFirst()); // 첫 페이지 여부
//        System.out.println("restaurantEntities.isLast : " + restaurantEntities.isLast()); // 마지막 페이지 여부

        Page<Restaurant> restaurants = restaurantEntities.map(Restaurant::from);
        return restaurants;
    }


    // 검색창에서 음식점을 검색해서 결과를 되돌려주는 메소드
    // 만일 검색결과 중 DB에 없는 식당이 있다면 DB에저장함.
//    public List<Restaurant> search(String target, HttpSession session) {
//        SearchLocalReq searchLocalReq = new SearchLocalReq();
//        SearchLocalRes searchLocalRes = null;
//        List<Restaurant> restaurants = new ArrayList<>();
//        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
//        GeoPoint memberPt = new GeoPoint(126.9783785, 37.5666612);
//        if (session.getAttribute("memberId") != null) {
//            String memberId = (String) session.getAttribute("memberId");
//            MemberEntity memberEntity = memberRepository.findByMemberId(memberId).get(0);
//            memberPt = new GeoPoint(Double.parseDouble(memberEntity.getMapX()), Double.parseDouble(memberEntity.getMapY()));
//        }
//
//        for (int i = 0; i < 2; i++) {
//            searchLocalReq.setQuery(target + i);
//            searchLocalRes = naverClient.searchLocal(searchLocalReq);
//            List<SearchLocalRes.SearchLocalItem> items = searchLocalRes.getItems();
//            for (SearchLocalRes.SearchLocalItem item : items) {
//                String imgLink = imgSearch(item.getTitle());
//                item.setTitle(titleTrans(item.getTitle())); // api결과 목록의 음식점명의 태그명을 지움
//                if (apiTitleChk(item.getTitle())) {
//                    RestaurantEntity restaurantEntity = new RestaurantEntity();
//                    restaurantEntity.setRestaurantName(item.getTitle());
//                    restaurantEntity.setRestaurantAddress(item.getAddress());
//                    restaurantEntity.setRestaurantRoadAddress(item.getRoadAddress());
//                    restaurantEntity.setCategory(apiCategoryTrans(item.getCategory()));
//                    restaurantEntity.setMapX((double) item.getMapx() / 10000000);
//                    restaurantEntity.setMapY((double) item.getMapy() / 10000000);
//                    restaurantEntity.setImgLink(imgLink);
//                    double distanceByGeo = GeoTrans.getDistancebyGeo(new GeoPoint((double) item.getMapx() / 10000000, (double) item.getMapy() / 10000000), memberPt);
//                    restaurantEntity.setDistance(Math.round(distanceByGeo * 100) / 100.0);
//                    restaurantRepository.save(restaurantEntity);
//                    restaurantEntities.add(restaurantEntity);
//                } else {
//                    restaurantEntities.add(restaurantRepository.findByRestaurantName(item.getTitle()));
//                }
//            }
//        }
//
//        for (RestaurantEntity restaurantEntity : restaurantEntities) {
//            restaurants.add(Restaurant.toRestaurant(restaurantEntity));
//        }
//        sortByDistance(restaurants, memberPt); // 거리 기준으로 정렬하는 메소드
//        return restaurants;
//    }

    public Page<Restaurant> search(String keyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 4;
        Page<RestaurantEntity> searchByName = restaurantRepository.findByRestaurantNameContaining(keyword, PageRequest.of(page, pageLimit));
        imgSave(searchByName);

        if (searchByName.isEmpty()) {
            Page<RestaurantEntity> searchByAddress = restaurantRepository.findByRestaurantAddressContaining(keyword, PageRequest.of(page, pageLimit));
            if (searchByAddress.isEmpty()) return Page.empty();
            else {
                imgSave(searchByAddress);
                Page<Restaurant> searchResult = searchByAddress.map(Restaurant::from);
                return searchResult;
            }
        }

        return searchByName.map(Restaurant::from);

    }

    //api에서 불러온 값들 중 음식점명의 태그를 지움
    public String titleTrans(String title) {
        title = title.replace("<b>", "");
        title = title.replace("</b>", "");
        title = title.trim();

        return title;
    }

    // api검색을 했을 때 db에 식당이 있는지 확인함 없다면 true을 반환함
//    public boolean apiTitleChk(String title) {
//        RestaurantEntity byRestaurantName = restaurantRepository.findByRestaurantName(title);
//        return byRestaurantName == null; // true => db에 식당이 없다
//    }

    // naver api 카테고리를 기준대로 변경함.
    public String apiCategoryTrans(String category) {
        if (category.contains("한식")) {
            return "한식";
        } else if (category.contains("일식")) {
            return "일식";
        } else if (category.contains("중식")) {
            return "중식";
        } else if (category.contains("분식")) {
            return "분식";
        } else if (category.contains("카페")) {
            return "카페";
        } else if (category.contains("양식")) {
            return "양식";
        } else {
            return "기타";
        }
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
