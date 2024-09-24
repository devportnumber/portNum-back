package com.portnum.number.popup.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.dto.request.*;
import com.portnum.number.popup.dto.response.ImageResponse;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.entity.Image;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.repository.ImageRepository;
import com.portnum.number.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${portnumber.admin.email}")
    private String adminEmail;

    public PopupInfoResponse create(PopupCreateRequest request) {
        Admin findAdmin = validateAdmin(request.getAdminId());

        Popup newPopup = Popup.of(request, findAdmin);

        popupRepository.save(newPopup);

        saveImages(newPopup, request.getImages());

        return PopupInfoResponse.of(newPopup);
    }

    public PopupInfoResponse modify(PopupModifyRequest request, String email) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);
        findPopup.modifyPopup(request);

        return PopupInfoResponse.of(findPopup);
    }

    public void remove(PopupRemoveRequest request, String email) {
        for(Long popupId : request.getPopupIds()){
            Popup findPopup = validatePopupAndGetWithImages(popupId, request.getAdminId(), email);

            List<Image> images = findPopup.getImages();

            removeImageS3AndDb(images, findPopup.getId());

        }

        popupRepository.deletePopups(request.getPopupIds());
    }


    public PopupInfoResponse addImages(ImageAddRequest request, String email) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);

        saveImages(findPopup, request.getImages());

        return PopupInfoResponse.of(findPopup);
    }

    public List<ImageResponse> modifyImages(ImageModifyRequest request, String email) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);

        findPopup.modifyRepresentImgUrl(request.getRepresentUrl());

        return modifyImagesUrl(request);
    }

    private List<ImageResponse> modifyImagesUrl(ImageModifyRequest request) {
        List<ImageResponse> images = new ArrayList<>();
        List<String> orgImgUrls = new ArrayList<>();

        // 지금은 조회후 하나씩 업데이트 <- 쿼리가 좀 많이 나감
        // 성능 이슈 발생시 그냥 전체 삭제 후 다시 삽입
        for(ImageResponse imageResponse : request.getImages()){
            Image image = imageRepository.findById(imageResponse.getImgId())
                    .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Image"));

            validatePopupImage(image, request.getPopupId());

//            imageUploadService.deleteImage(image.getImgUrl());
            orgImgUrls.add(image.getImgUrl());
            image.modifyUrl(imageResponse.getImgUrl());

            images.add(ImageResponse.of(image));
        }

        imageUploadService.deleteImages(orgImgUrls);

        return images;
    }

    public void removeImages(ImageRemoveRequest request, String email) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);

        findPopup.modifyRepresentImgUrl(request.getRepresentUrl());

        List<Image> images = imageRepository.findByIds(request.getImgIds());
        removeImageS3AndDb(images, request.getPopupId());
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

    private void validatePopupImage(Image image, Long popupId) {
        if(!image.getPopup().getId().equals(popupId)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Equals Image's PopupId");
        }
    }

    private void saveImages(Popup popup, List<ImageRequest> images) {
        if(images != null){
            images.forEach(imageRequest -> imageRepository.save(
                            Image.of(popup, imageRequest.getImgUrl())
                    ));
        }
    }


    private Popup validatePopupAdmin(Long popupId, Long adminId, String email){
        Popup popup = validatePopup(popupId);

        if(email.equals(adminEmail)){
            return popup;
        } else if(!popup.getAdmin().getId().equals(adminId)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Admin About Forum");
        } else{
            return popup;
        }
    }

    private Popup validatePopupAndGetWithImages(Long popupId, Long adminId, String email){
        Popup popup = popupRepository.getPopupDetail(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));

        if(email.equals(adminEmail)){
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

}
