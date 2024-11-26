package com.portnum.number.popup.service;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.dto.request.*;
import com.portnum.number.popup.dto.response.ImageResponse;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.domain.Image;
import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.repository.ImageRepository;
import com.portnum.number.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PopupCommandService {

    private final AdminRepository adminRepository;
    private final PopupRepository popupRepository;
    private final ImageRepository imageRepository;

    private final ImageUploadService imageUploadService;

    @Value("${portnumber.admin.loginId}")
    private String adminLongId;

    @CachePut(value = "popupDetail", key = "#result.popupId", cacheManager = "popupCacheManager")
    public PopupDetailResponse create(PopupCreateRequest request) {
        Admin findAdmin = validateAdmin(request.getAdminId());

        Popup newPopup = Popup.builder()
                .name(request.getName())
                .address(request.getAddress())
                .category(request.getCategory())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .operatingHours(request.getOperatingHours())
                .stat(request.getStat())
                .point(request.getPoint())
                .description(request.getDescription())
                .detailDescription(request.getDetailDescription())
                .mapUrl(request.getMapUrl())
                .representImgUrl(request.getRepresentImgUrl())
                .keywords(request.getKeywords())
                .admin(findAdmin)
                .build();

        newPopup = popupRepository.save(newPopup);

        saveImages(request.getImages());

        return PopupDetailResponse.of(newPopup);
    }


    @CachePut(value = "popupDetail", key = "#result.popupId", cacheManager = "popupCacheManager")
    public PopupDetailResponse modify(PopupModifyRequest request, String loginId) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), loginId);
        findPopup.modifyPopup(request);

        modifyImagesLogic(findPopup, request.getImages());

        return PopupDetailResponse.of(findPopup);
    }


    @CacheEvict(value = "popupDetail", allEntries = true, cacheManager = "popupCacheManager")
    public void remove(PopupRemoveRequest request, String loginId) {
        for (Long popupId : request.getPopupIds()) {
            removeSinglePopup(popupId, request.getAdminId(), loginId);
        }

        popupRepository.deletePopups(request.getPopupIds());
    }

    @Caching(evict = {
            @CacheEvict(value = "popupDetail", key = "#popupId", cacheManager = "popupCacheManager")
    })
    public void removeSinglePopup(Long popupId, Long adminId, String loginId) {
        Popup findPopup = validatePopupAndGetWithImages(popupId, adminId, loginId);

        List<Image> images = imageRepository.findByPopupId(popupId);
        removeImageS3AndDb(images, findPopup.getId());

    }

    private void modifyImagesLogic(Popup findPopup, ImagesModifyRequest images) {
        if (images != null) {
            if (images.getAddImages() != null)
                addImages(images.getAddImages());
            if (images.getUpdateImages() != null)
                modifyImages(findPopup, images.getUpdateImages());
            if (images.getRemoveImages() != null)
                removeImages(findPopup, images.getRemoveImages());
        }
    }

    private void addImages(List<ImageRequest> images) {
        saveImages(images);
    }

    private void modifyImages(Popup popup, List<ImageResponse> updateImages) {
        modifyImagesUrl(popup.getId(), updateImages);
    }

    private void modifyImagesUrl(Long popupId, List<ImageResponse> updateImages) {
        List<String> orgImgUrls = new ArrayList<>();

        // 지금은 조회후 하나씩 업데이트 <- 쿼리가 좀 많이 나감
        // 성능 이슈 발생시 그냥 전체 삭제 후 다시 삽입
        for (ImageResponse imageResponse : updateImages) {
            Image image = imageRepository.findById(imageResponse.getImgId())
                    .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Image"));

            validatePopupImage(image, popupId);

//            imageUploadService.deleteImage(image.getImgUrl());
            orgImgUrls.add(image.getImgUrl());
            image.modifyUrl(imageResponse.getImgUrl());
        }

        imageUploadService.deleteImages(orgImgUrls);
    }

    private void removeImages(Popup popup, List<Long> imgIds) {
        List<Image> images = imageRepository.findByIds(imgIds);
        removeImageS3AndDb(images, popup.getId());
    }

    private void removeImageS3AndDb(List<Image> images, Long popupId) {
        List<String> imgUrls = new ArrayList<>();

        for (Image image : images) {
            validatePopupImage(image, popupId);
            imgUrls.add(image.getImgUrl());
        }
        imageRepository.deleteAllByPopupId(popupId);
        imageUploadService.deleteImages(imgUrls);
    }

    private void saveImages(List<ImageRequest> images) {
        if (images != null) {
            images.forEach(imageRequest -> imageRepository.save(
                    Image.from(imageRequest.getImgUrl())
            ));
        }
    }

    private void validatePopupImage(Image image, Long popupId) {
        if (!image.getPopup().getId().equals(popupId)) {
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Equals Image's PopupId");
        }
    }


    private Popup validatePopupAdmin(Long popupId, Long adminId, String loginId) {
        Popup popup = validatePopup(popupId);

        if (loginId.equals(adminLongId)) {
            return popup;
        } else if (!popup.getAdmin().getId().equals(adminId)) {
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Admin About Forum");
        } else {
            return popup;
        }
    }

    private Popup validatePopupAndGetWithImages(Long popupId, Long adminId, String loginId) {
        Popup popup = popupRepository.getPopupDetail(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));

        if (loginId.equals(adminLongId)) {
            return popup;
        } else if (!popup.getAdmin().getId().equals(adminId)) {
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Admin About Forum");
        } else {
            return popup;
        }
    }

    private Popup validatePopup(Long popupId) {
        return popupRepository.findById(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));
    }

    private Admin validateAdmin(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }

}
