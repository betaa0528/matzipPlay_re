package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.entity.Article;
import com.restaurantProject.famousrestaurant.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;


    public Page<ArticleDto> searchArticles(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 10;
        return articleRepository.findAll(PageRequest.of(page, pageLimit, pageable.getSort())).map(ArticleDto::from);
    }

    public ArticleDto findById(Long articleId) {
        Article article = articleRepository.findById(articleId).get();
        return ArticleDto.from(article);
    }

    public void countViews(Long articleId) {
        Article article = articleRepository.findById(articleId).get();
        article.setViews(article.getViews() + 1);
        articleRepository.save(article);
        log.info("views : " + article.getViews());
    }
}
