package com.portnum.number.fixture;

import com.portnum.number.popup.domain.Image;
import com.portnum.number.popup.domain.Popup;

public class ImageFixture {
    public static Image createImageForFail(Long imageId) {
        return Image.builder()
                .id(imageId)
                .imgUrl("imgUrl" + imageId)
                .popup(Popup.builder().id(9999L).build())
                .build();
    }

    public static Image createImageForSuccess(Long imageId, Long popupId) {
        return Image.builder()
                .id(imageId)
                .imgUrl("imgUrl" + imageId)
                .popup(Popup.builder().id(popupId).build())
                .build();
    }

    public static Image createImage(Popup popup){
        return Image.builder()
                .popup(popup)
                .imgUrl("imgUrl")
                .build();
    }

    public static Image createImage(Long imageId, Popup popup){
        return Image.builder()
                .id(imageId)
                .popup(popup)
                .imgUrl("imgUrl")
                .build();
    }
}
