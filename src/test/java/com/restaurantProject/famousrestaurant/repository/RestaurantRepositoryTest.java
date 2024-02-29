package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("RestaurantRepository 테스트")
@DataJpaTest
class RestaurantRepositoryTest {

    private final RestaurantRepository restaurantRepository;

    RestaurantRepositoryTest(
            @Autowired RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenWorkFine() {

        // given

        // when
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

        // then
        assertThat(restaurantEntities)
                .isNotNull()
                .hasSize(683);
    }

    @DisplayName("select distinct test")
    @Test
    void givenTestData_whenDistinctSelecting_thenWorkFine() {

        // given

        // when
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        HashSet<String> hashSet = (HashSet<String>) restaurantEntities.stream()
                .map(RestaurantEntity::getRestaurantName)
                        .collect(Collectors.toSet());

        // then
        assertThat(hashSet)
                .isNotNull()
                .hasSize(683);
    }

    @DisplayName("search test - 검색어 키워드가 음식점명에 포함될 때")
    @Test
    void givenSearchKeyword_whenSearchingRestaurantName_thenReturnMatchingResult() {

        // given
        RestaurantEntity restaurant1 = new RestaurantEntity();
        restaurant1.setId(1L);
        restaurant1.setRestaurantName("테스트");
        restaurant1.setRestaurantAddress("서울시 중구");

        RestaurantEntity restaurant2 = new RestaurantEntity();
        restaurant2.setId(2L);
        restaurant2.setRestaurantName("맛있다");
        restaurant2.setRestaurantAddress("서울시 중구");

        String keyword = "테스트";
        Pageable pageable = Pageable.ofSize(10);
        // when
        Page<RestaurantEntity> result = restaurantRepository.findByRestaurantNameContaining(keyword, pageable);


        // then
        assertThat(1).isEqualTo(result.getTotalElements());
        assertEquals("테스트", result.getContent().get(0).getRestaurantName());

    }

    @DisplayName("search test - 검색어 키워드가 음식점 주소에 포함 될 때")
    @Test
    void givenSearchKeyword_whenSearchingRestaurantAddress_thenReturnMatchingResult() {

        // given
        RestaurantEntity restaurant1 = new RestaurantEntity();
        restaurant1.setId(1L);
        restaurant1.setRestaurantName("테스트");
        restaurant1.setRestaurantAddress("서울시 중구");

        RestaurantEntity restaurant2 = new RestaurantEntity();
        restaurant2.setId(2L);
        restaurant2.setRestaurantName("맛있다");
        restaurant2.setRestaurantAddress("서울시 테스트구");

        String keyword = "테스트";
        Pageable pageable = Pageable.ofSize(10);
        // when
        Page<RestaurantEntity> result = restaurantRepository.findByRestaurantAddressContaining(keyword, pageable);


        // then
        assertThat(1).isEqualTo(result.getTotalElements());
        assertEquals("킹왕짱", result.getContent().get(0).getRestaurantName());

    }

    @DisplayName("Page객체의 메소드 테스트")
    @Test
    void given_whenMethodOfPage_thenMatchResult() {
        String keyword = "akdt";
        Pageable pageable = Pageable.ofSize(10);
        Page<RestaurantEntity> result = restaurantRepository.findByRestaurantAddressContaining(keyword, pageable);

        System.out.println(result.getTotalElements());
        System.out.println(result.isEmpty());
    }


    @Test
    public void test() {

        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        restaurantEntities.forEach(System.out::println);
        for(RestaurantEntity entity : restaurantEntities){
            if(entity.getRestaurantName().equals("마싯는음식점")){
                Long id = entity.getId();
                restaurantRepository.deleteById(id);
            }
        }
        restaurantRepository.deleteById(2L);
        restaurantRepository.findAll().forEach(System.out::println);
    }

    @Test
    public void preparedTest() {
        String jdbcUrl = "jdbc:mysql://localhost:3306/db_famousRestaurant";
        String username = "user_famousRestaurant";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {

            String sqlFilePath = "C:\\Users\\minju\\Downloads\\famousrestaurant\\src\\test\\resources\\restaurantDB.txt";
            StringBuilder query = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    query.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] queries = query.toString().split(";");

            for (String sql : queries) {
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long count = restaurantRepository.count();
        System.out.println(count);
    }

}