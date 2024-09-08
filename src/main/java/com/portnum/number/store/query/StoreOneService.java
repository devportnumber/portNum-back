package com.portnum.number.store.query;

import com.portnum.number.global.common.domain.enums.Valid;
import com.portnum.number.store.domain.Store;
import com.portnum.number.store.repository.StoreRepository;
import com.portnum.number.store.response.StoreOneResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 팝업 단일 조회 서비스
 *
 * @author 송병선
 */
@RequiredArgsConstructor
@Service
public class StoreOneService {

  private final StoreRepository storeRepository;

  /**
   * 팝업 단일 조회
   *
   * @param storeId 팝업 ID
   * @return 팝업 정보
   */
  @Transactional(readOnly = true)
  public StoreOneResponse getOne(Integer storeId) {
    return new StoreOneResponse(getStore(storeId));
  }

  /**
   * 팝업 엔티티 조회
   *
   * @param storeId 팝업 ID
   * @return 팝업 엔티티
   */
  @Transactional(readOnly = true)
  public Store getStore(Integer storeId) {
    return storeRepository.findByStoreIdAndValid(storeId, Valid.TRUE)
                          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팝업 ID입니다."));
  }
}
