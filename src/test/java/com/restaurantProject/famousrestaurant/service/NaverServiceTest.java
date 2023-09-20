package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.naver.NaverClient;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchImageRes;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalReq;
import com.restaurantProject.famousrestaurant.naver.dto.SearchLocalRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class NaverServiceTest {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private NaverClient naverClient;

    @Test
    public void NaverClientTest() throws InterruptedException {
//        String link = restaurantService.search("호랭이분식");
//        System.out.println(link);

//        SearchImageReq searchImageReq = new SearchImageReq();
//        searchImageReq.setQuery("호랭이분식");
//        SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);
//
//        System.out.println(searchImageRes);

        SearchLocalReq searchLocalReq = new SearchLocalReq();
        searchLocalReq.setDisplay(5);
        List<String> categoryList = new ArrayList<>();
        SearchLocalRes searchLocalRes = null;
        searchLocalReq.setQuery("패스트푸드");

//        for(int i=0; i<1; i++){
//            searchLocalRes = naverClient.searchLocal(searchLocalReq);
//            String[] split = searchLocalRes.getItems().get(0).getCategory().split(">");
//            if(!categoryList.contains(split[0])){
//                categoryList.add(split[0]);
//            }
//            if(split.length > 1){
//                if(!categoryList.contains(split[1])){
//                    categoryList.add(searchLocalRes.getItems().get(0).getCategory());
//                }
//            }

//            System.out.println(split.length);

//            searchLocalRes.getItems().stream().forEach(System.out::println);
//            if(i%9==0){
//                Thread.sleep(800);
//            }
//        }
//        categoryList.forEach(System.out::println);
//        double mapx = (double)searchLocalRes.getItems().get(0).getMapx()/10000000;
//        double mapy = (double)searchLocalRes.getItems().get(0).getMapy()/10000000;
//        List<SearchLocalRes.SearchLocalItem> items = searchLocalRes.getItems();
//        for(SearchLocalRes.SearchLocalItem item : items){
//            if(item.getTitle().contains("<b>") || item.getTitle().contains("</b>")){
//                item.setTitle(item.getTitle().replaceAll("<b>", " "));
//                item.setTitle(item.getTitle().replaceAll("</b>", " "));
//                item.setTitle(item.getTitle().trim());
//            }
//            System.out.println(item);
//        }
//        System.out.println(mapx + ", " + mapy);

        searchLocalReq.setQuery("서울 분식집1");
        searchLocalRes = naverClient.searchLocal(searchLocalReq);
        searchLocalRes.getItems().stream().forEach(System.out::println);
        List<SearchLocalRes.SearchLocalItem> items = searchLocalRes.getItems();
//        for(SearchLocalRes.SearchLocalItem item : items){
//            if(item.getCategory().contains("중식")){
//                item.setCategory("중식");
//            } else {
//                item.setCategory("기타");
//            }
//        }
//        System.out.println("======================================");
//        searchLocalRes.getItems().stream().forEach(System.out::println);
//        searchLocalReq.setQuery("서울 중국집2");
//        searchLocalRes = naverClient.searchLocal(searchLocalReq);
//        searchLocalRes.getItems().stream().forEach(System.out::println);


    }
}
