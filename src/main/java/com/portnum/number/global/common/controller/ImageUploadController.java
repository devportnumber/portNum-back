package com.portnum.number.global.common.controller;

import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.common.dto.response.PreSignedUrlResponse;
import com.portnum.number.global.common.provider.S3PreSignedUrlProvider;
import com.portnum.number.global.common.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/image")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @GetMapping
    public DataResponseDto generatePreSignedUrl(@RequestParam String imageName){
        PreSignedUrlResponse response = imageUploadService.generatePreSignedUrl(imageName);

        return DataResponseDto.of(response);
    }
}
