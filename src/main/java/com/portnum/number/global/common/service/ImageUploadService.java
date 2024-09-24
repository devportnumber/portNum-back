package com.portnum.number.global.common.service;

import com.portnum.number.global.common.dto.response.PreSignedUrlResponse;
import com.portnum.number.global.common.provider.S3PreSignedUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final S3PreSignedUrlProvider s3PreSignedUrlProvider;

    public PreSignedUrlResponse generatePreSignedUrl(String imageName) {
        return s3PreSignedUrlProvider.getPreSignedUrl(imageName);
    }

    public void deleteImage(String imageUrl){
        s3PreSignedUrlProvider.deleteImageByPath(imageUrl);
    }

    public void deleteImages(List<String> imageUrls){
        s3PreSignedUrlProvider.deleteImagesByPaths(imageUrls);
    }
}
