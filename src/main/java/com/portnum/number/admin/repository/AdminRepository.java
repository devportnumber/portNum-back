package com.portnum.number.admin.repository;

import com.portnum.number.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a where a.email =:email and a.deleted = false")
    Optional<Admin> findByEmail(String email);
}
