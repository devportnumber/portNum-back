package com.portnum.number.popup.entity;

import com.portnum.number.global.common.domain.BaseTimeEntity;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PopupStatus stat;

    @Embedded
    private Point point;

    @Embedded
    private Address address;

    @Column(name = "description")
    private String description;

    @Column(name = "map_url")
    private String mapUrl;
}
