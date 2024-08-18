package com.portnum.number.store.domain;

import com.portnum.number.global.common.domain.enums.Valid;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팝업 엔티티
 *
 * @author 송병선
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners({AuditingEntityListener.class})
@Table(name = "store")
public class Store {

  /**
   * 팝업 ID
   */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer storeId;
  /**
   * 팝업 이름
   */
  @Column(name = "name", nullable = false)
  private String name;
  /**
   * 팝업 키워드
   */
  @Column(name = "keywords")
  private String keywords;
  /**
   * 팝업 지역
   */
  @Column(name = "neighborhood", nullable = false)
  private String neighborhood;
  /**
   * 팝업 카테고리
   */
  @Column(name = "category", nullable = false)
  private String category;
  /**
   * 경도
   */
  @Column(name = "longitude", nullable = false)
  private String longitude;
  /**
   * 위도
   */
  @Column(name = "latitude", nullable = false)
  private String latitude;
  /**
   * 주소
   */
  @Column(name = "address", nullable = false)
  private String address;
  /**
   * 상세 주소
   */
  @Column(name = "address_detail", nullable = false)
  private String addressDetail;
  /**
   * 설명
   */
  @Column(name = "description", nullable = false)
  private String description;
  /**
   * map url
   */
  @Column(name = "map_url", nullable = false)
  private String mapUrl;
  /**
   * 영업 시작 일자
   */
  @Column(name = "start_date", nullable = false)
  private String startDate;
  /**
   * 영업 종료 일자
   */
  @Column(name = "end_date", nullable = false)
  private String endDate;
  /**
   * 영업 시작 시간
   */
  @Column(name = "start_time", nullable = false)
  private String startTime;
  /**
   * 영업 종료 시간
   */
  @Column(name = "end_time", nullable = false)
  private String endTime;
  /**
   * 유효여부
   */
  @Convert(converter = Valid.TypeCodeConverter.class)
  @Column(name = "valid", nullable = false)
  private Valid valid = Valid.TRUE;
  /**
   * 등록 일시
   */
  @CreatedDate
  @Column(name = "reg_dt", updatable = false, nullable = false)
  private LocalDateTime regDt;
  /**
   * 팝업 이미지 목록
   */
  @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @SQLRestriction("valid = 'Y'")
  private final List<StoreImage> imageList = new ArrayList<>();

  public Store(String name, String keywords, String neighborhood, String category, String longitude, String latitude,
      String address, String addressDetail, String description, String mapUrl, String startDate, String endDate,
      String startTime, String endTime) {
    this.name = name;
    this.keywords = keywords;
    this.neighborhood = neighborhood;
    this.category = category;
    this.longitude = longitude;
    this.latitude = latitude;
    this.address = address;
    this.addressDetail = addressDetail;
    this.description = description;
    this.mapUrl = mapUrl;
    this.startDate = startDate;
    this.endDate = endDate;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * 팝업 이미지 저장
   */
  public void addImageList(List<StoreImage> addImageList) {
    if (Objects.isNull(addImageList) || addImageList.isEmpty()) {
      return;
    }

    for (StoreImage image : addImageList) {
      this.imageList.add(image);
      image.setStore(this);
    }
  }

  public void updateValid(Valid valid) {
    this.valid = valid;
  }
}
