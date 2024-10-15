package com.portnum.number.popup.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.dto.request.*;
import com.portnum.number.popup.dto.response.ImageResponse;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.entity.Image;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.repository.ImageRepository;
import com.portnum.number.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        Popup newPopup = Popup.of(request, findAdmin);

        popupRepository.save(newPopup);

        saveImages(newPopup, request.getImages());

        return PopupDetailResponse.of(newPopup);
    }

//    public PopupInfoResponse modify(PopupModifyRequest request, String loginId) {
//        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), loginId);
//        findPopup.modifyPopup(request);
//
//        return PopupInfoResponse.of(findPopup);
//    }

    @CachePut(value = "popupDetail", key = "#result.popupId", cacheManager = "popupCacheManager")
    public PopupDetailResponse modify(PopupModifyRequest request, String loginId) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), loginId);
        findPopup.modifyPopup(request);

        modifyImagesLogic(findPopup, request.getImages());

//        addImages(findPopup, request.getImages().getAddImages());
//        modifyImages(findPopup, request.getImages().getUpdateImages());
//        removeImages(findPopup, request.getImages().getRemoveImages());

        return PopupDetailResponse.of(findPopup);
    }

//    @CacheEvict(value = "popupDetail", key = "#request.popupIds", cacheManager = "popupCacheManager")
//    public void remove(PopupRemoveRequest request, String loginId) {
//        for(Long popupId : request.getPopupIds()){
//            Popup findPopup = validatePopupAndGetWithImages(popupId, request.getAdminId(), loginId);
//
//            List<Image> images = findPopup.getImages();
//
//            removeImageS3AndDb(images, findPopup.getId());
//
//        }
//
//        popupRepository.deletePopups(request.getPopupIds());
//    }

    @Caching(evict = {
            @CacheEvict(value = "popupDetail", key = "#popupId", cacheManager = "popupCacheManager")
    })
    public void removeSinglePopup(Long popupId, Long adminId, String loginId) {
        Popup findPopup = validatePopupAndGetWithImages(popupId, adminId, loginId);

        List<Image> images = findPopup.getImages();
        removeImageS3AndDb(images, findPopup.getId());

    }

    @CacheEvict(value = "popupDetail", allEntries = true, cacheManager = "popupCacheManager")
    public void remove(PopupRemoveRequest request, String loginId) {
        for(Long popupId : request.getPopupIds()) {
            removeSinglePopup(popupId, request.getAdminId(), loginId);
        }

        popupRepository.deletePopups(request.getPopupIds());
    }

    private void modifyImagesLogic(Popup findPopup, ImagesModifyRequest images) {
        if(images != null) {
            if(images.getAddImages() != null)
                addImages(findPopup, images.getAddImages());
            if(images.getUpdateImages() != null)
                modifyImages(findPopup, images.getUpdateImages());
            if(images.getRemoveImages() != null)
                removeImages(findPopup, images.getRemoveImages());
        }
    }

    public void addImages(Popup popup, List<ImageRequest> images) {
        saveImages(popup, images);
    }

    public List<ImageResponse> modifyImages(Popup popup, List<ImageResponse> updateImages) {
        return modifyImagesUrl(popup.getId(), updateImages);
    }

    private List<ImageResponse> modifyImagesUrl(Long popupId, List<ImageResponse> updateImages) {
        List<ImageResponse> images = new ArrayList<>();
        List<String> orgImgUrls = new ArrayList<>();

        // 지금은 조회후 하나씩 업데이트 <- 쿼리가 좀 많이 나감
        // 성능 이슈 발생시 그냥 전체 삭제 후 다시 삽입
        for(ImageResponse imageResponse : updateImages){
            Image image = imageRepository.findById(imageResponse.getImgId())
                    .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Image"));

            validatePopupImage(image, popupId);

//            imageUploadService.deleteImage(image.getImgUrl());
            orgImgUrls.add(image.getImgUrl());
            image.modifyUrl(imageResponse.getImgUrl());
            images.add(ImageResponse.of(image));
        }

        imageUploadService.deleteImages(orgImgUrls);

        return images;
    }

    public void removeImages(Popup popup, List<Long> imgIds) {
        List<Image> images = imageRepository.findByIds(imgIds);
        removeImageS3AndDb(images, popup.getId());
    }

    private void removeImageS3AndDb(List<Image> images, Long popupId) {
        List<Long> imgIds = new ArrayList<>();
        List<String> imgUrls = new ArrayList<>();

        for(Image image : images){
            validatePopupImage(image, popupId);
            imgIds.add(image.getId());
            imgUrls.add(image.getImgUrl());
//            imageUploadService.deleteImage(image.getImgUrl());
        }
        imageRepository.deleteAllByIdInBatch(imgIds);
        imageUploadService.deleteImages(imgUrls);
    }

    private void saveImages(Popup popup, List<ImageRequest> images) {
        if(images != null){
            images.forEach(imageRequest -> imageRepository.save(
                    Image.of(popup, imageRequest.getImgUrl())
            ));
        }
    }

    private void validatePopupImage(Image image, Long popupId) {
        if(!image.getPopup().getId().equals(popupId)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Equals Image's PopupId");
        }
    }



    private Popup validatePopupAdmin(Long popupId, Long adminId, String loginId){
        Popup popup = validatePopup(popupId);

        if(loginId.equals(adminLongId)){
            return popup;
        } else if(!popup.getAdmin().getId().equals(adminId)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Admin About Forum");
        } else{
            return popup;
        }
    }

    private Popup validatePopupAndGetWithImages(Long popupId, Long adminId, String loginId){
        Popup popup = popupRepository.getPopupDetail(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));

        if(loginId.equals(adminLongId)){
            return popup;
        } else if(!popup.getAdmin().getId().equals(adminId)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Admin About Forum");
        } else{
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



//    public PopupInfoResponse addImages(ImageAddRequest request, String loginId) {
//        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), loginId);
//
//        saveImages(findPopup, request.getImages());
//
//        return PopupInfoResponse.of(findPopup);
//    }



//    public List<ImageResponse> modifyImages(ImageModifyRequest request, String loginId) {
//        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), loginId);
//
//        findPopup.modifyRepresentImgUrl(request.getRepresentUrl());
//
//        return modifyImagesUrl(request);
//    }

}
