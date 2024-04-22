package com.portnum.number.store.repository;

import com.portnum.number.common.domain.enums.Valid;
import com.portnum.number.store.domain.Store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 팝업 JPA 저장소
 */
public interface StoreRepository extends JpaRepository<Store, Integer> {

  Optional<Store> findByStoreIdAndValid(Integer storeId, Valid valid);
}
