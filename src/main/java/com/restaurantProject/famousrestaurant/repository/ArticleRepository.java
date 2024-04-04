package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
