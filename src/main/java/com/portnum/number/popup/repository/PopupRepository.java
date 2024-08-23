package com.portnum.number.popup.repository;

import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.entity.Popup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PopupRepository extends JpaRepository<Popup, Long>, PopupCustomRepository {

    @Query("select p from Popup p join fetch p.admin where p.id =:popupId and p.deleted = false")
    Optional<Popup> findById(Long popupId);

    @Modifying
    @Query("update Popup p set p.deleted = true where p.id IN :popupIds")
    void deletePopups(List<Long> popupIds);

    @Query("select distinct(p) from Popup p join fetch p.images where p.id =:popupId and p.deleted = false")
    Optional<Popup> getPopupDetail(Long popupId);

//    @Query("select distinct(p) from Popup p join fetch p.admin ")
//    Optional<Popup> findByIdWithImages(Long popupId);
}
