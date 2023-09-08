package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

@SpringBootTest
class RestaurantRepositoryTest {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    public void test() {
//        List<RestaurantEntity> all = restaurantRepository.findAll();
//        all.stream().forEach(System.out::println);
//        System.out.println(restaurantRepository.findByRestaurantName("왔따쪽갈비"));
        System.out.println(restaurantRepository.findById(1L).get().getRestaurantName());
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