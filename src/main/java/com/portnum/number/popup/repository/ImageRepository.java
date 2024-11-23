package com.portnum.number.popup.repository;

import com.portnum.number.popup.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i join fetch i.popup where i.id =:imageId")
    Optional<Image> findById(Long imageId);

    @Query("select i from Image i where i.id in :imgIds")
    List<Image> findByIds(List<Long> imgIds);

    @Query("select i from Image i where i.popup.id = :popupId")
    List<Image> findByPopupId(Long popupId);

    @Modifying
    @Query("delete from Image i where i.popup.id = :popupId")
    void deleteAllByPopupId(Long popupId);
}
