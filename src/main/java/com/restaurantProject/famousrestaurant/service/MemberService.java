package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
}
