package com.portnum.number.store.response;

import com.portnum.number.store.domain.Store;
import lombok.Getter;
import lombok.ToString;

/**
 * 팝업 목록 조회 반환 형식
 *
 * @author 송병선
 */
@Getter
@ToString
public class StoreListResponse {

  /**
   * 팝업 ID
   */
  private Integer storeId;
  /**
   * 팝업 이름
   */
  private String name;
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

  public StoreListResponse(Store store) {
    this.storeId = store.getStoreId();
    this.name = store.getName();
    this.category = store.getCategory();
    this.longitude = store.getLongitude();
    this.latitude = store.getLatitude();
  }
}
