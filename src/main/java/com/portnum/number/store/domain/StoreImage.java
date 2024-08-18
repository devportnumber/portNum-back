package com.portnum.number.store.domain;

import com.portnum.number.global.common.domain.BaseTimeEntity;
import com.portnum.number.global.common.domain.enums.Valid;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 팝업 이미지 엔티티
 *
 * @author 송병선
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_img")
public class StoreImage extends BaseTimeEntity {

  /**
   * 팝업 이미지 ID
   */
  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer imgId;
  /**
   * 이미지 url
   */
  @Column(name = "url", nullable = false)
  private String url;
  /**
   * 이미지 순서
   */
  @Column(name = "seq", nullable = false)
  private Integer seq;
  /**
   * 유효여부
   */
  @Convert(converter = Valid.TypeCodeConverter.class)
  @Column(name = "valid", nullable = false)
  private Valid valid = Valid.TRUE;
  /**
   * 팝업 정보
   */
  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  public StoreImage(String url, Integer seq) {
    this.url = url;
    this.seq = seq;
  }

  void setStore(Store store) {
    this.store = store;
  }
}
