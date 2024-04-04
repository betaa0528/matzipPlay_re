package com.restaurantProject.famousrestaurant.service;

import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.dto.Review;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ReviewService reviewService;

    public String getId(long l) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(l);
        return memberEntity.isPresent() ? memberEntity.get().getMemberId() : null;
    }

//    public Member getByMemberId(Object memberId) {
//        MemberEntity memberEntity = repository.findByMemberId((String) memberId).get();
//        return Member.toMember(memberEntity);
//    }
//
//    public MemberEntity getMember() {
//        return memberRepository.getReferenceById("minjoo");
//    }

    public HashMap<String, Member> getByMemberIdList(Long restId) {
        List<Review> reviews = reviewService.findByRestaurantIdList(restId);
        List<Member> members = new ArrayList<>();
        for (Review review : reviews) {
            MemberEntity memberEntity = memberRepository.getByMemberId(review.getMemberId());
            members.add(Member.from(memberEntity));
        }

        return getMemberOfReviewList(members, reviews);
    }

    private HashMap<String, Member> getMemberOfReviewList(List<Member> members, List<Review> reviews) {
        HashMap<String, Member> memberHashMap = new HashMap<>();
        for (Member member : members) {
            for (Review review : reviews) {
                if(review.getMemberId().equals(member.getMemberId())){
                    memberHashMap.put(member.getMemberId(), member);
                }
            }
        }

        return memberHashMap;
    }
}
