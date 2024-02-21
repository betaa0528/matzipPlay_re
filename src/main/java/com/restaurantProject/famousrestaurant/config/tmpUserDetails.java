package com.restaurantProject.famousrestaurant.config;

import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class tmpUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    public tmpUserDetails(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password;
        List<GrantedAuthority> authorities;
        List<MemberEntity> member = memberRepository.findByMemberId(username);
        if (member.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        } else{
            userName = member.get(0).getMemberId();
            password = member.get(0).getMemberPass();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(member.get(0).getRole()));
        }
        return new User(userName,password,authorities);
    }
}
