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

    public Member findById(Long id){
        MemberEntity memberEntity = repository.findById(id).get();
        Member member = new Member();
        member.setMemberId(member.getMemberId());
        member.setMemberAddress(member.getMemberAddress());
        member.setMapX(126.8863995);
        member.setMapY(37.5269193);
        return member;
    }
}
