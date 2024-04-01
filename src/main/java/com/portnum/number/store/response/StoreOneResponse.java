package com.portnum.number.store.response;

import com.portnum.number.common.utils.DateUtils;
import com.portnum.number.store.domain.Store;
import java.time.LocalDate;
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
   * 팝업 Id
   */
  private Integer id;
  /**
   * 팝업 카테고리
   */
  private String category;
  /**
   * 영업 기간
   */
  private String dates;
  /**
   * 영업 시간
   */
  private String time;
  /**
   * 팝업 이름
   */
  private String name;
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
  private String address_detail;
  /**
   * 팝업 지역
   */
  private String neighborhood;
  /**
   * 팝업 키워드
   */
  private List<String> keywords;
  /**
   * 설명
   */
  private String description;
  /**
   * 팝업 이미지
   */
  private List<String> images;
  /**
   * 등록일
   */
  private String regdt;

  public StoreOneResponse(Store store) {
    this.id = store.getStoreId();
    this.category = store.getCategory();
    this.dates = store.getStartDate() + " - " + store.getEndDate();
    this.time = DateUtils.getDayOfWeek(LocalDate.now()) + " " + store.getStartTime() + " ~ "
        + store.getEndTime();
    this.name = store.getName();
    this.longitude = store.getLongitude();
    this.latitude = store.getLatitude();
    this.address = store.getAddress();
    this.address_detail = store.getAddressDetail();
    this.neighborhood = store.getNeighborhood();
    this.keywords = Arrays.stream(store.getKeywords().split(",")).toList();
    this.description = store.getDescription();
    this.images = Arrays.stream(store.getImages().split(",")).toList();
    this.regdt = store.getRegDt().toLocalDate().toString();
  }
}