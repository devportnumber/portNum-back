package com.portnum.number.popup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String imgUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id", nullable = false)
    private Popup popup;

    //===생성 메서드===//
    public static Image of(Popup popup, String imgUrl){
        Image image = new Image();

        image.modifyUrl(imgUrl);
        popup.addImage(image);

        return image;
    }



    //===수정 메서드===//
    public void modifyUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    protected void modifyPopup(Popup popup){
        this.popup = popup;
    }
}
