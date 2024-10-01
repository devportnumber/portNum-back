package com.portnum.number.admin.service;

import com.portnum.number.admin.dto.request.AdminModifyPasswordRequest;
import com.portnum.number.admin.dto.request.LostRequest;
import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.common.service.MailService;
import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import com.portnum.number.global.utils.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private final ImageUploadService imageUploadService;
    private final MailService mailService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

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

    public boolean modifyPassword(AdminModifyPasswordRequest request, String accessToken) {
        Admin findAdmin = validateAdmin(request.getAdminId());

        if(passwordEncoder.matches(request.getOldPassword(), findAdmin.getPassword())){
            findAdmin.modifyPassword(passwordEncoder.encode(request.getNewPassword()));
            logoutProcess(accessToken, findAdmin);
            return true;
        } else{
            return false;
        }
    }


    public boolean lostEmail(LostRequest request) {
        if(existsEmailAndNickName(request)){
            mailService.sendEmail(request.getEmail(), request.getEmail());
            return true;
        } else{
            return false;
        }
    }

    public boolean lostPassword(LostRequest request) {
        Admin findAdmin = findEmailAndNickName(request);

        if(findAdmin != null) {
            String randomPassword = RandomUtils.generateRandomCode();
            findAdmin.modifyPassword(passwordEncoder.encode(randomPassword));
            findAdmin.modifyIsRqPwChange();

            mailService.sendEmail(request.getEmail(), randomPassword);
            return true;
        } else{
            return false;
        }
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

    private boolean existsEmailAndNickName(LostRequest request){
        return adminRepository.existsByEmailWithNickName(request.getEmail(), request.getNickName());
    }

    private Admin findEmailAndNickName(LostRequest request) {
        return adminRepository.findByEmailWithNickName(
                request.getEmail(), request.getNickName()).orElse(null);
    }

    private void logoutProcess(String accessToken, Admin findAdmin) {
        redisService.deleteValues(findAdmin.getEmail());
        long accessTokenValidityInSeconds = jwtTokenProvider.getAccessTokenValidityInSeconds();
        redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenValidityInSeconds));
    }
}
