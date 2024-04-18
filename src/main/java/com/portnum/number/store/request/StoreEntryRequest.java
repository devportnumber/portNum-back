package com.portnum.number.store.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 팝업 등록 요청
 *
 * @author 송병선
 */
@Getter
@Setter
public class StoreEntryRequest {

  /**
   * 팝업 이름
   */
  private String name;
  /**
   * 팝업 키워드
   */
  private String keywords;
  /**
   * 팝업 지역
   */
  private String neighborhood;
  /**
   * 팝업 카테고리
   */
  private String category;
  /**
   * 경도
   */
  private String longitude;
  /**
   * 위도
   */
  private String latitude;
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
   * map url
   */
  private String mapUrl;
  /**
   * 영업 시작 일자
   */
  private String startDate;
  /**
   * 영업 종료 일자
   */
  private String endDate;
  /**
   * 영업 시작 시간
   */
  private String startTime;
  /**
   * 영업 종료 시간
   */
  private String endTime;
}
