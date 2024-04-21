package com.portnum.number.store.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 팝업 유효여부 수정 요청
 *
 * @author 송병선
 */
@Getter
@Setter
public class StoreValidRequest {

  /**
   * 팝업 ID
   */
  private Integer storeId;
  /**
   * 유효여부
   */
  private String valid;
}
