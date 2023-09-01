package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
