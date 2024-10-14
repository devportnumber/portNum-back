package com.portnum.number.popup.entity;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.global.common.converter.StringListConverter;
import com.portnum.number.global.common.domain.BaseTimeEntity;
import com.portnum.number.popup.dto.request.PopupCreateRequest;
import com.portnum.number.popup.dto.request.PopupModifyRequest;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SQLDelete(sql = "UPDATE Popup SET deleted = true WHERE popup_id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Popup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popup_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PopupCategory category;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    private String operatingHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PopupStatus stat;

    @Embedded
    private Point point;

    @Embedded
    private Address address;

    private String description;

    private String detailDescription;

    private String mapUrl;

    private String representImgUrl;

    @Column(nullable = false, length = 1000)
    @Convert(converter = StringListConverter.class)
    private List<String> keywords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "popup")
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private Boolean deleted = Boolean.FALSE;

    /* 연관관계 메서드 */
    public void addImage(Image image) {
        image.modifyPopup(this);
        getImages().add(image);
    }

    /* 생성 메서드 */
    public static Popup of(PopupCreateRequest request, Admin admin){
        Popup newPopup = new Popup();

        newPopup.modifyPopup(request, admin);

        return newPopup;
    }


    /* 수정 메서드 */
    private void modifyPopup(PopupCreateRequest request, Admin admin){
        this.name = request.getName();
        this.address = request.getAddress();
        this.category =request.getCategory();
        this.startDate =request.getStartDate();
        this.endDate =request.getEndDate();
        this.operatingHours = request.getOperatingHours();
        this.stat = request.getStat();
        this.point = request.getPoint();
        this.description = request.getDescription();
        this.detailDescription = request.getDetailDescription();
        this.mapUrl = request.getMapUrl();
        this.keywords = request.getKeywords();
        this.representImgUrl = request.getRepresentImgUrl();
        this.admin = admin;
    }

    public void modifyPopup(PopupModifyRequest request){
        this.name = request.getName();
        this.address = request.getAddress();
        this.category =request.getCategory();
        this.startDate =request.getStartDate();
        this.endDate =request.getEndDate();
        this.operatingHours = request.getOperatingHours();
        this.stat = request.getStat();
        this.point = request.getPoint();
        this.description = request.getDescription();
        this.detailDescription = request.getDetailDescription();
        this.mapUrl = request.getMapUrl();
        this.keywords = request.getKeywords();
    }

    public void modifyPopupStatus(PopupStatus stat){
        this.stat = stat;
    }


    public void modifyRepresentImgUrl(String representImgUrl){
        this.representImgUrl = representImgUrl;
    }
}
