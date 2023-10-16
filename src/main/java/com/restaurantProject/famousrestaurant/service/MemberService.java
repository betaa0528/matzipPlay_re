package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    public String getId(long l) {
        Optional<MemberEntity> memberEntity = repository.findById(l);
        return memberEntity.isPresent() ? memberEntity.get().getMemberId() : null;
    }

    public Member getByMemberId(Object memberId) {
        MemberEntity memberEntity = repository.findByMemberId((String) memberId).get();
        return Member.toMember(memberEntity);
    }

}
