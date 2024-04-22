package com.portnum.number.store.controller;

import com.portnum.number.store.command.StoreCommandService;
import com.portnum.number.store.query.StoreListService;
import com.portnum.number.store.query.StoreOneService;
import com.portnum.number.store.request.StoreEntryRequest;
import com.portnum.number.store.request.StoreImageAddRequest;
import com.portnum.number.store.request.StoreValidRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 팝업 API
 *
 * @author 송병선
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/store", produces = MediaType.APPLICATION_JSON_VALUE)
public class StoreController {

  private final StoreCommandService storeCommandService;
  private final StoreOneService storeOneService;
  private final StoreListService storeListService;

  /**
   * 팝업 단일 조회
   *
   * @return 팝업 정보
   */
  @GetMapping
  public ResponseEntity<?> doGet(@RequestParam(required = false) Integer storeId) {
    return ResponseEntity.ok(storeOneService.getOne(storeId));
  }

  /**
   * 팝업 목록 조회
   *
   * @return 팝업 목록
   */
  @GetMapping("/list")
  public ResponseEntity<?> doGetAsList() {
    return ResponseEntity.ok(storeListService.getList());
  }

  /**
   * 팝업 등록
   */
  @PostMapping
  public ResponseEntity<?> doPost(@RequestBody StoreEntryRequest param) {
    storeCommandService.save(param);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 팝업 이미지 추가 등록
   */
  @PostMapping("/image")
  public ResponseEntity<?> doPostImage(@RequestBody StoreImageAddRequest param) {
    storeCommandService.save(param);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * 팝업 유효여부 수정
   */
  @PutMapping("/valid")
  public ResponseEntity<?> doPutValid(@RequestBody StoreValidRequest param) {
    storeCommandService.update(param);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
