package com.portnum.number.store.batch.job;

import com.portnum.number.global.common.domain.enums.Valid;
import com.portnum.number.global.common.utils.DateUtils;
import com.portnum.number.store.domain.Store;
import com.portnum.number.store.query.StoreListService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팝업 유효여부 수정 처리 작업
 *
 * @author 송병
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class StoreValidJob {

  private final StoreListService storeListService;

  @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
  @Transactional
  public void execute() {
    log.info("팝업 유효여부 수정 처리 작업 시작");
    List<Store> list = storeListService.getStoreList();

    list.forEach(store -> {
      LocalDate startDate = DateUtils.convertToDate(store.getStartDate());
      LocalDate endDate = DateUtils.convertToDate(store.getEndDate());

      if (DateUtils.isBetween(startDate, endDate, LocalDate.now())) {
        store.updateValid(Valid.TRUE);
      } else {
        store.updateValid(Valid.FALSE);
      }
    });

    log.info("팝업 유효여부 수정 처리 작업 종료");
  }
}
