package com.portnum.number.admin.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminQueryService {

    private final AdminRepository adminRepository;
    public Admin findAdmin(Long adminId){
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }

    public boolean validateEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public boolean validateNickName(String nickName) {
        return adminRepository.existsByNickName(nickName);
    }
}
