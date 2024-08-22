package com.portnum.number.admin.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCommandService {

    private final AdminRepository adminRepository;

    private final ImageUploadService imageUploadService;

    public AdminInfoResponse create(AdminCreateRequest request){
        Admin newAdmin = Admin.of(request);

        // 추후 이메일 중복 여부 검증 로직 추가 (Security 사용시?)

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
}