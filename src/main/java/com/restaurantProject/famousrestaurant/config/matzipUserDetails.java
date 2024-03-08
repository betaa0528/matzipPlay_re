package com.restaurantProject.famousrestaurant.config;

import com.restaurantProject.famousrestaurant.dto.security.BoardPrincipal;
import com.restaurantProject.famousrestaurant.entity.Authority;
import com.restaurantProject.famousrestaurant.entity.MemberEntity;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class matzipUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    public matzipUserDetails(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password;
        List<GrantedAuthority> authorities;
        List<MemberEntity> member = memberRepository.findByMemberId(username);
        if (member.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + username);
        } else {
            userName = member.get(0).getMemberId();
            password = member.get(0).getMemberPass();
            authorities = getAuthorities(member.get(0).getAuthorities());
        }
        return new BoardPrincipal(userName, password, authorities);
    }

    private List<GrantedAuthority> getAuthorities(Set<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

}
