package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.ArticleDto;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.Article;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberRepository memberRepository;


    @Test
    public void articleTest() {
        MemberEntity member = memberRepository.findById(1L).get();
        Member memberDto = Member.from(member);

//        ArticleDto dto = new ArticleDto("title1", "content1", memberDto, "FREE");
////        System.out.println(dto);
//        Article article = Article.of(
//                member,
//                dto.getTitle(),
//                dto.getContent(),
//                ArticleType.FREE);
//        articleRepository.save(article);
//        List<Article> all = articleRepository.findAll();
//        all.forEach(System.out::println);

//        Article article = new Article() ;
    }

}