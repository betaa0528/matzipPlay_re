package com.restaurantProject.famousrestaurant.controller;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.dto.request.ArticleRequest;
import com.restaurantProject.famousrestaurant.dto.response.ArticleResponse;
import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import com.restaurantProject.famousrestaurant.entity.constant.FormStatus;
import com.restaurantProject.famousrestaurant.repository.ArticleRepository;
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
import org.springframework.web.bind.annotation.*;

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
    public String article(@AuthenticationPrincipal BoardPrincipal principal,
                          @PathVariable Long articleId, Model model) {
        articleService.countViews(articleId);
        ArticleDto article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("principal", principal);
        return "articles/detail";
    }

    @GetMapping("/form")
    public String articleForm(@AuthenticationPrincipal BoardPrincipal principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("formStatus", FormStatus.CREATE);
        return "articles/articleForm";
    }

    @PostMapping("/form")
    public String postNewArticle(
            @AuthenticationPrincipal BoardPrincipal principal,
            @ModelAttribute ArticleRequest request) {
        articleService.saveArticle(request.toDto(principal.toDto()));
        return "redirect:/articles/";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(
            @AuthenticationPrincipal BoardPrincipal principal,
            @PathVariable Long articleId, Model model) {
        ArticleResponse response = ArticleResponse.from(articleService.findById(articleId));
        model.addAttribute("principal", principal);
        model.addAttribute("article", response);
        model.addAttribute("formStatus", FormStatus.UPDATE);
        return "articles/articleForm";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(
            @AuthenticationPrincipal BoardPrincipal principal,
            @PathVariable Long articleId, ArticleRequest request) {
        articleService.updateArticle(articleId, request.toDto(principal.toDto()));
        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @AuthenticationPrincipal BoardPrincipal principal,
            @PathVariable Long articleId) {
        articleService.deleteArticle(articleId, principal.getUsername());
        return "redirect:/articles";
    }
}
