package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {


    MemberEntity getReferenceById(String memberId);

    Optional<MemberEntity> findByMemberNaverId(String email);
    List<MemberEntity> findByMemberId(String id);
    Optional<MemberEntity> findByMemberPhoneNumber(String phoneNumber);

}
