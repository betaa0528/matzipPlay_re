package com.restaurantProject.famousrestaurant.repository;

import com.restaurantProject.famousrestaurant.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
}
