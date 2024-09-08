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
        System.out.println("====================" + email);
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);
        findPopup.modifyPopup(request);

        return PopupInfoResponse.of(findPopup);
    }

    public void remove(PopupRemoveRequest request, String email) {
        for(Long popupId : request.getPopupIds()){
            Popup findPopup = validatePopupAdmin(popupId, request.getAdminId(), email);

            List<Long> imgIds = findPopup.getImages()
                    .stream()
                    .map(Image::getId)
                    .toList();

            removeImageS3AndDb(imgIds, findPopup.getId());
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

        for(ImageResponse imageResponse : request.getImages()){
            Image image = imageRepository.findById(imageResponse.getImgId())
                    .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Image"));

            validatePopupImage(image, request.getPopupId());

            imageUploadService.deleteImage(image.getImgUrl());
            image.modifyUrl(imageResponse.getImgUrl());

            images.add(ImageResponse.of(image));
        }

        return images;
    }

    public void removeImages(ImageRemoveRequest request, String email) {
        Popup findPopup = validatePopupAdmin(request.getPopupId(), request.getAdminId(), email);

        findPopup.modifyRepresentImgUrl(request.getRepresentUrl());

        removeImageS3AndDb(request.getImgIds(), request.getPopupId());
    }

    private void removeImageS3AndDb(List<Long> imgIds, Long popupId) {
        for(Long imageId : imgIds){
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Image"));

            validatePopupImage(image, popupId);

            imageUploadService.deleteImage(image.getImgUrl());
        }

        imageRepository.deleteAllByIdInBatch(imgIds);
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

    private Popup validatePopup(Long popupId) {
        return popupRepository.findById(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));
    }

    private Admin validateAdmin(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }

}
