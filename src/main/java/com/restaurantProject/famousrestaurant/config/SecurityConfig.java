package com.restaurantProject.famousrestaurant.config;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.restaurantProject.famousrestaurant.dto.Member;
import com.restaurantProject.famousrestaurant.repository.MemberRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static java.util.Arrays.stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((request) -> request
                        .antMatchers("/review", "/mypage").authenticated()
                        .antMatchers("/restaurant", "/").permitAll()
                        .antMatchers("/admin").hasRole("ADMIN")
                        .antMatchers("/review/form").hasRole("USER")
//                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                )
                .headers().frameOptions().sameOrigin().and()
                .formLogin(Customizer.withDefaults())
//                .logout(logout -> logout.logoutSuccessUrl("/"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
