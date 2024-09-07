package com.portnum.number.global.security.custom;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.entity.RoleType;
import com.portnum.number.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final AdminRepository adminRepository;

    /**
     * loadByUsername() : 사용자 이름(email)을 입력받아 User에서 사용자 정보를 조회한다.
     * 조회한 User 객체가 존재하면 createUserDetails() 메서드를 사용해서 CusotmUserDetails 객체를 생성하고 반환한다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin findAdmin = adminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return CustomUserDetails.of(findAdmin);
    }

    private UserDetails createUserDetails(Admin admin){
        return CustomUserDetails.of(admin);
    }
}
