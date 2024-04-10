package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.entity.Article;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.ArticleRepository;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;


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
    }

    public void saveArticle(ArticleDto dto) {
        MemberEntity memberEntity = memberRepository.getByMemberId(dto.getMember().getMemberId());
        articleRepository.save(dto.toEntity(memberEntity));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            MemberEntity member = memberRepository.getByMemberId(dto.getMember().getMemberId());

            if (article.getMember().equals(member)) {
                if (dto.getTitle() != null) {
                    article.setTitle(dto.getTitle());
                }
                if (dto.getContent() != null) {
                    article.setContent(dto.getContent());
                }
                article.setArticleType(dto.getArticleType());
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글 업데이트시 필요한 정보를 찾을 수 없음. - {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(Long articleId, String username) {
//        Article article = articleRepository.getReferenceById(articleId);
//        MemberEntity member = memberRepository.getByMemberId(username);
//        if (article.getMember().equals(member)) {
//            articleRepository.deleteById(articleId);
//        }
        articleRepository.deleteById(articleId);
    }
}
