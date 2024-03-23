package com.portnum.number.store.command;

import com.portnum.number.store.domain.Store;
import com.portnum.number.store.repository.StoreRepository;
import com.portnum.number.store.request.StoreEntryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팝업 등록/수정/삭제 서비스
 *
 * @author 송병선
 */
@RequiredArgsConstructor
@Service
public class StoreCommandService {

  private final StoreRepository storeRepository;

  /**
   * 팝업 등록
   *
   * @param param 팝업 정보
   */
  @Transactional
  public void entry(StoreEntryRequest param) {
    storeRepository.save(makeStoreEntity(param));
  }

  /**
   * 팝업 엔티티 생성
   *
   * @param param 등록 정보
   * @return 팝업 엔티티
   */
  private Store makeStoreEntity(StoreEntryRequest param) {
    return new Store(param.getName(), param.getKeywords(), param.getNeighborhood(),
        param.getCategory(), param.getLongitude(), param.getLatitude(), param.getAddress(),
        param.getAddressDetail(), param.getImages(), param.getDescription(), param.getStartDate(),
        param.getEndDate(), param.getStartTime(), param.getEndTime());
  }
}
