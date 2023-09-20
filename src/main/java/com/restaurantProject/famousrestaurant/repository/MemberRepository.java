package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByMemberNaverId(String email);
    Optional<MemberEntity> findByMemberId(String id);
    Optional<MemberEntity> findByMemberPhoneNumber(String phoneNumber);

}
