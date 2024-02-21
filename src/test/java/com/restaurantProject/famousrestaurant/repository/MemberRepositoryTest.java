package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;



    @DisplayName("select test")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // given

        // when
        MemberEntity memberEntity = memberRepository.findById(1L).orElseThrow();

        // then
        assertThat(memberEntity.getMemberId()).isEqualTo("minjoo");
    }

    @DisplayName("select test2")
    @Test
    void givenTestData_whenSelectingRefernceById_thenWorksFine() {
        // given

        // when
        MemberEntity memberEntity = memberRepository.getReferenceById(1L);

        // then
        assertThat(memberEntity.getMemberId()).isEqualTo("minjoo");
    }

}