package com.portnum.number.store.response;

import com.portnum.number.store.domain.Store;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

/**
 * 팝업 단일 조회 반환 형식
 *
 * @author 송병선
 */
@Getter
@ToString
public class StoreOneResponse {

  /**
   * 팝업 이름
   */
  private String name;
  /**
   * 팝업 키워드
   */
  private List<String> keywordList;
  /**
   * 팝업 지역
   */
  private String neighborhood;
  /**
   * 팝업 카테고리
   */
  private String category;
  /**
   * 주소
   */
  private String address;
  /**
   * 상세 주소
   */
  private String addressDetail;
  /**
   * 팝업 이미지
   */
  private String images;
  /**
   * 설명
   */
  private String description;
  /**
   * 영업 기간
   */
  private String businessPeriod;
  /**
   * 영업 시간
   */
  private String businessHours;

  public StoreOneResponse(Store store) {
    this.name = store.getName();
    this.keywordList = Arrays.stream(store.getKeywords().split(",")).toList();
    this.neighborhood = store.getNeighborhood();
    this.category = store.getCategory();
    this.address = store.getAddress();
    this.addressDetail = store.getAddressDetail();
    this.images = store.getImages();
    this.description = store.getDescription();
    this.businessPeriod = store.getStartDate() + " - " + store.getEndDate();
    this.businessHours = store.getStartTime() + " ~ " + store.getEndTime();
  }
}
