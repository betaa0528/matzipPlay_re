package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.service.ArticleService;
import com.restaurantProject.famousrestaurant.service.PaginationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @AuthenticationPrincipal BoardPrincipal principal,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC, page = 1) Pageable pageable,
            Model model) {
        Page<ArticleDto> articles = articleService.searchArticles(pageable);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        model.addAttribute("principal", principal);
        model.addAttribute("articles", articles);
        model.addAttribute("barNumbers", barNumbers);
        return "articles/article";
    }

    @GetMapping("/{articleId}")
    public String article( @AuthenticationPrincipal BoardPrincipal principal,
                           @PathVariable Long articleId, Model model) {
        articleService.countViews(articleId);
        ArticleDto article = articleService.findById(articleId);
        log.info("contorller view : " + article.getViews());
        model.addAttribute("article", article);
        model.addAttribute("principal", principal);
        return "articles/detail";
    }


}
