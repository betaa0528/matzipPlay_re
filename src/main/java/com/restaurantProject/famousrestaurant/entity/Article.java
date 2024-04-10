package com.restaurantProject.famousrestaurant.entity;

import com.restaurantProject.famousrestaurant.entity.constant.ArticleType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @Setter
    @Column(length = 1000)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "memberId")
    @ToString.Exclude
    private MemberEntity member;

    @Setter
    @Enumerated(EnumType.STRING)
    private ArticleType articleType;

    @ColumnDefault("0")
    @Setter
    private int views;

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article" , cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ArticleComment> articleComments = new LinkedHashSet<>();

    private Article(MemberEntity member, String title, String content, ArticleType articleType) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.articleType = articleType;
    }

    public static Article of(MemberEntity member, String title, String content, ArticleType articleType) {
        return new Article(member, title, content, articleType);
    }

}
