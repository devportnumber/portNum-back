package com.portnum.number.store.query;

import com.portnum.number.global.common.domain.enums.Valid;
import com.portnum.number.store.domain.Store;
import com.portnum.number.store.repository.StoreRepository;
import com.portnum.number.store.response.StoreListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팝업 목록 조회 서비스
 *
 * @author 송병선
 */
@RequiredArgsConstructor
@Service
public class StoreListService {

  private final StoreRepository storeRepository;

  /**
   * 팝업 목록 조회
   *
   * @return 팝업 목록
   */
  @Transactional(readOnly = true)
  public List<StoreListResponse> getList() {
    return storeRepository.findAll().stream()
        .filter(it -> it.getValid().equals(Valid.TRUE))
        .map(StoreListResponse::new)
        .toList();
  }

  /**
   * 팝업 엔티티 목록 조회
   *
   * @return 팝업 엔티티 목록
   */
  @Transactional(readOnly = true)
  public List<Store> getStoreList() {
    return storeRepository.findAll();
  }
}
