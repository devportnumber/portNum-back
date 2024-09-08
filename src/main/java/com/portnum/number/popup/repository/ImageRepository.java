package com.portnum.number.popup.repository;

import com.portnum.number.popup.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i join fetch i.popup where i.id =:imageId")
    Optional<Image> findById(Long imageId);
}
