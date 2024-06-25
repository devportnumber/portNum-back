package com.portnum.number.admin.repository;

import com.portnum.number.admin.domain.AdminStore;
import com.portnum.number.common.domain.enums.Valid;
import com.portnum.number.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 팝업 JPA 저장소
 */
public interface AdminRepository extends JpaRepository<AdminStore, Integer> {

}
