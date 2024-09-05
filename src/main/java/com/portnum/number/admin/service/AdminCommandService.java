package com.portnum.number.admin.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.config.EncryptHelper;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final ImageUploadService imageUploadService;
    private final PasswordEncoder passwordEncoder;

    public AdminInfoResponse create(AdminCreateRequest request){
        validateEmailAndNickName(request.getEmail(), request.getNickName());

        request.modifyPassword(passwordEncoder.encode(request.getPassword()));
        Admin newAdmin = Admin.of(request);
        adminRepository.save(newAdmin);

        return AdminInfoResponse.of(newAdmin);
    }

    public AdminInfoResponse modify(AdminModifyRequest request) {
        Admin findAdmin = validateAdmin(request.getAdminId());

        updateAdminProfile(findAdmin.getProfileUrl(), request.getProfileUrl());
        findAdmin.modifyAdmin(request);

        return AdminInfoResponse.of(findAdmin);
    }

    private void updateAdminProfile(String existProfileUrl, String newProfileUrl) {
        if(existProfileUrl != null && newProfileUrl != null){
            imageUploadService.deleteImage(existProfileUrl);
        }
    }

    private Admin validateAdmin(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }

    private void validateEmailAndNickName(String email, String nickName){
//        log.info("{}", adminRepository.existsByEmail(email));
        if(adminRepository.existsByEmail(email) || adminRepository.existsByNickName(nickName))
            throw new GlobalException(Code.VALIDATION_ERROR, "Email Or NickName Already Exists");
    }
}
