package com.portnum.number.store.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 팝업 이미지 추가 등록 요청
 *
 * @author 송병선
 */
@Getter
@Setter
public class StoreImageAddRequest {

  /**
   * 팝업 ID
   */
  private Integer storeId;
  /**
   * 추가할 이미지 url 목록
   */
  private List<String> imageUrlList;
}
