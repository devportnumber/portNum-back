package com.portnum.number.global.common.provider;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.portnum.number.global.aop.annotation.Retry;
import com.portnum.number.global.common.dto.response.PreSignedUrlResponse;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.utils.IdentityGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3PreSignedUrlProvider {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // AWS S3 버킷명과 동일

    private final AmazonS3Client amazonS3Client;
    private final IdentityGeneratorUtil identityGeneratorUtil; // 프로젝트 내에서 만든 ULID 생성기

    public PreSignedUrlResponse getPreSignedUrl(String imageName) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = getGeneratePreSignedUrlRequest("store", imageName);

        return generatePreSignedUrl(generatePresignedUrlRequest);
    }

    private PreSignedUrlResponse generatePreSignedUrl(GeneratePresignedUrlRequest generatePresignedUrlRequest) {
        String preSignedUrl;
        String imageSaveUrl;
        try {
            preSignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
            imageSaveUrl = amazonS3Client.getUrl(bucket, generatePresignedUrlRequest.getKey()).toString();
        } catch (AmazonServiceException e) {
            log.error("Pre-signed Url 생성 실패했습니다. {}", e.getMessage());
            throw new IllegalStateException("Pre-signed Url 생성 실패했습니다.");
        }

        return PreSignedUrlResponse.of(preSignedUrl, imageSaveUrl);
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String directory, String imageName) {
        String savedImageName = generateUniqueImageName(imageName);
        String savedImagePath = "image" + "/" + directory + "/" + savedImageName;


        return getPreSignedUrlRequest(bucket, savedImagePath);
    }

    private GeneratePresignedUrlRequest getPreSignedUrlRequest(String bucket, String imageName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(new Date(System.currentTimeMillis() + 180000));

        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());


        return generatePresignedUrlRequest;
    }

    /**
     * image path 형식 : "images" + "/" + "기능 dir" + "/" + ULID + 확장자;
     */
    private String generateUniqueImageName(String imageName) {
        String ext = imageName.substring(imageName.lastIndexOf("."));
        return identityGeneratorUtil.generateIdentity() + ext;
    }

    @Retry
    @Async
    public void deleteImageByPath(String imagePath) {
        try {
            // URL에서 key 추출
            String key = imagePath.substring(imagePath.indexOf("image/"));

            log.info("key = {}", key);
            amazonS3Client.deleteObject(bucket, key);
        } catch (AmazonServiceException e) {
            log.error("이미지 삭제 실패했습니다. {}", e.getMessage());
            throw new GlobalException(Code.INTERNAL_ERROR, "이미지 삭제 실패했습니다.");
        }
    }
}