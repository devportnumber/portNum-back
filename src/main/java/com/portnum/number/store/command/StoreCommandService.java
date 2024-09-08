package com.portnum.number.store.command;

import com.portnum.number.global.common.domain.enums.Valid;
import com.portnum.number.store.domain.Store;
import com.portnum.number.store.domain.StoreImage;
import com.portnum.number.store.query.StoreOneService;
import com.portnum.number.store.repository.StoreRepository;
import com.portnum.number.store.request.StoreEntryRequest;
import com.portnum.number.store.request.StoreImageAddRequest;
import com.portnum.number.store.request.StoreValidRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.RequiredArgsConstructor;

/**
 * 팝업 등록/수정/삭제 서비스
 *
 * @author 송병선
 */
@RequiredArgsConstructor
@Service
public class StoreCommandService {

  private final StoreRepository storeRepository;

  private final StoreOneService storeOneService;

  /**
   * 팝업 등록
   *
   * @param param 팝업 정보
   */
  @Transactional
  public void save(StoreEntryRequest param) {
    Store store = storeRepository.save(makeStoreEntity(param));

    // 이미지 엔티티 생성
    AtomicInteger order = new AtomicInteger(1);
    List<StoreImage> imageList =
        param.getImageUrlList().stream().map(url -> new StoreImage(url, order.getAndIncrement())).toList();
    store.addImageList(imageList);
  }

  /**
   * 팝업 이미지 추가 등록
   *
   * @param param 등록 정보
   */
  @Transactional
  public void save(StoreImageAddRequest param) {
    Store store = storeOneService.getStore(param.getStoreId());

    // 이미지 엔티티 생성
    AtomicInteger order = new AtomicInteger(store.getImageList().isEmpty() ? 1 :
        store.getImageList().stream().mapToInt(StoreImage::getSeq).max().orElse(0) + 1);
    List<StoreImage> imageList =
        param.getImageUrlList().stream().map(url -> new StoreImage(url, order.getAndIncrement())).toList();
    store.addImageList(imageList);
  }

  /**
   * 팝업 유효여부 수정
   *
   * @param param 수정 정보
   */
  @Transactional
  public void update(StoreValidRequest param) {
    Store store = storeOneService.getStore(param.getStoreId());
    store.updateValid(Valid.fromCode(param.getValid()));
  }

  /**
   * 팝업 엔티티 생성
   *
   * @param param 등록 정보
   * @return 팝업 엔티티
   */
  private Store makeStoreEntity(StoreEntryRequest param) {
    return new Store(param.getName(), param.getKeywords(), param.getNeighborhood(), param.getCategory(),
        param.getLongitude(), param.getLatitude(), param.getAddress(), param.getAddressDetail(), param.getDescription(),
        param.getMapUrl(), param.getStartDate(), param.getEndDate(), param.getStartTime(), param.getEndTime());
  }
}
