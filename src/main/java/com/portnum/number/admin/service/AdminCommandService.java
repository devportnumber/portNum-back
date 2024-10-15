package com.portnum.number.admin.service;

import com.portnum.number.admin.dto.request.*;
import com.portnum.number.admin.entity.Admin;
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
import org.springframework.util.StringUtils;

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
        String urlName = RandomUtils.generateALPHANUMERICRandomCode();

        Admin newAdmin = Admin.of(request, urlName);
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
        if(StringUtils.hasText(request.getOldPassword()) && passwordEncoder.matches(request.getOldPassword(), findAdmin.getPassword())){
            findAdmin.modifyPassword(passwordEncoder.encode(request.getNewPassword()));
            logoutProcess(accessToken, findAdmin);
        } else if(!StringUtils.hasText(request.getOldPassword())){
            findAdmin.modifyPassword(passwordEncoder.encode(request.getNewPassword()));
            logoutProcess(accessToken, findAdmin);
        } else{
            return false;
        }

        return true;
    }


    public boolean lostEmail(LostRequest request) {
        if(existsEmailAndNickName(request)){
            mailService.sendEmail(request.getEmail(), request.getEmail());
            return true;
        } else{
            return false;
        }
    }

    public String lostLoginId(LostLoginIdRequest request) {
        Admin findAdmin = findEmailAndName(request);

        return findAdmin.getLoginId();
    }


    public boolean lostPassword(LostPasswordRequest request) {
        Admin findAdmin = findEmailAndLoginId(request);

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

    private Admin findEmailAndLoginId(LostPasswordRequest request) {
        return adminRepository.findByEmailWithLoginId(
                request.getEmail(), request.getLoginId()).orElse(null);
    }

    private Admin findEmailAndName(LostLoginIdRequest request) {
        return adminRepository.findByEmailWithName(
                request.getEmail(), request.getName()).orElseThrow(() ->{
                    throw  new GlobalException(Code.VALIDATION_ERROR, "User Not Found");
        });
    }

    private void logoutProcess(String accessToken, Admin findAdmin) {
        redisService.deleteValues(findAdmin.getEmail());
        long accessTokenValidityInSeconds = jwtTokenProvider.getAccessTokenValidityInSeconds();
        redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenValidityInSeconds));
    }
}
