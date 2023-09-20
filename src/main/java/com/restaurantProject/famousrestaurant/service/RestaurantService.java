package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Restaurant;
import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import com.restaurantProject.famousrestaurant.geo.GeoPoint;
import com.restaurantProject.famousrestaurant.geo.GeoTrans;
import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalRes;
import com.restaurantProject.famousrestaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

    public String imgSearch(String query){
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

    public int saveFindCategory(HttpSession session) throws InterruptedException {
        SearchLocalReq searchLocalReq = new SearchLocalReq();
        String[] categoryArr = {"까페", "분식", "일식", "중국식", "한식", "패스트부드"};
        for(String category : categoryArr){
            for(int i=0; i<10 ; i++) {
                searchLocalReq.setQuery("서울 " + category + i);
                SearchLocalRes searchLocalRes = naverClient.searchLocal(searchLocalReq);
                List<SearchLocalRes.SearchLocalItem> items = searchLocalRes.getItems();
                for (SearchLocalRes.SearchLocalItem item : items) {
                    RestaurantEntity restaurantEntity = new RestaurantEntity();
                    restaurantEntity.setRestaurantName(item.getTitle());
                    restaurantEntity.setRestaurantAddress(item.getAddress());
                    restaurantEntity.setRestaurantRoadAddress(item.getRoadAddress());
                    restaurantEntity.setCategory(category);
                    restaurantEntity.setMapX((double) item.getMapx() / 10000000);
                    restaurantEntity.setMapY((double) item.getMapy() / 10000000);
                    if (session != null) {
                        GeoPoint resPoint = new GeoPoint((double) item.getMapx() / 10000000, (double) item.getMapy() / 10000000);
                        GeoPoint userPoint = (GeoPoint) session.getAttribute("memberPoint");
//                        restaurantEntity.setDistance(GeoTrans.getDistancebyGeo(resPoint, userPoint));
                    } else {
//                        restaurantEntity.setDistance(0.0);
                    }
                    restaurantRepository.save(restaurantEntity);
                }
            }
            Thread.sleep(1000);
        }
        return 1;

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
        int pageLimit = 1;
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

    // 검색창에서 음식점을 검색해서 결과를 되돌려주는 메소드
    // 만일 검색결과 중 DB에 없는 식당이 있다면 DB에저장함.
    public List<Restaurant> search(String target) {
        SearchLocalReq searchLocalReq = new SearchLocalReq();
        SearchLocalRes searchLocalRes = null;
        List<Restaurant> restaurants = new ArrayList<>();
        List<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for(int i=0; i<2; i++){
            searchLocalReq.setQuery(target + i);
            searchLocalRes = naverClient.searchLocal(searchLocalReq);
            List<SearchLocalRes.SearchLocalItem> items = searchLocalRes.getItems();
            for(SearchLocalRes.SearchLocalItem item : items){
                item.setTitle(titleTrans(item.getTitle())); // api결과 목록의 음식점명의 태그명을 지움
                if(apiTitleChk(item.getTitle())){
                    RestaurantEntity restaurantEntity = new RestaurantEntity();
                    restaurantEntity.setRestaurantName(item.getTitle());
                    restaurantEntity.setRestaurantAddress(item.getAddress());
                    restaurantEntity.setRestaurantRoadAddress(item.getRoadAddress());
                    restaurantEntity.setCategory(apiCategoryTrans(item.getCategory()));
                    restaurantEntity.setMapX((double) item.getMapx() / 10000000);
                    restaurantEntity.setMapY((double) item.getMapy() / 10000000);
                    restaurantRepository.save(restaurantEntity);
                    restaurantEntities.add(restaurantEntity);
                } else {
                    restaurantEntities.add(restaurantRepository.findByRestaurantName(item.getTitle()));
                }
            }
        }

        for(RestaurantEntity restaurantEntity : restaurantEntities){
            restaurants.add(Restaurant.toRestaurant(restaurantEntity));
        }
        return restaurants;
    }

    //api에서 불러온 값들 중 음식점명의 태그를 지움
    public String titleTrans(String title) {
        title = title.replace("<b>", "");
        title = title.replace("</b>", "");
        title = title.trim();

        return title;
    }
    // api검색을 했을 때 db에 식당이 있는지 확인함 없다면 true을 반환함
    public boolean apiTitleChk(String title){
        RestaurantEntity byRestaurantName = restaurantRepository.findByRestaurantName(title);
        return byRestaurantName == null; // true => db에 식당이 없다
    }

    // naver api 카테고리를 기준대로 변경함.
    public String apiCategoryTrans(String category) {
        if(category.contains("한식")){
            return "한식";
        } else if(category.contains("일식")) {
            return "일식";
        } else if(category.contains("중식")) {
            return "중식";
        } else if(category.contains("분식")) {
            return "분식";
        } else if(category.contains("카페")) {
            return "카페";
        } else if(category.contains("양식")) {
            return "양식";
        } else {
            return "기타";
        }
    }
}
