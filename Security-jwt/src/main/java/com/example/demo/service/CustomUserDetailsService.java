package com.example.demo.service;

import java.lang.System.Logger;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CustomUserDetails;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	log.info(username + "@@@@@@@@@@@@@@@");
    	// DB에서 조회
        UserEntity userData = userRepository.findByUsername(username);
        log.info(userData + "!!!!!!!!!!!!!!!!!!!!");
        if (userData != null) {
        	//UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        throw new UsernameNotFoundException("유저정보를 찾을 수 없습니다 " + username);
    }
}