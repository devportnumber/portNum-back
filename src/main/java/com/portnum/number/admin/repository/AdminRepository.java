package com.portnum.number.admin.repository;

import com.portnum.number.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a where a.email =:email and a.deleted = false")
    Optional<Admin> findByEmail(String email);

    @Query("select a from Admin a where a.loginId =:loginId and a.deleted = false")
    Optional<Admin> findByLoginId(String loginId);

    @Query("select count(a) > 0 from Admin a where a.email = :email and a.deleted = false")
    boolean existsByEmail(@Param("email") String email);

    @Query("select count(a) > 0 from Admin a where a.nickName = :nickName and a.deleted = false")
    boolean existsByNickName(String nickName);

    @Query("select count(a) > 0 from Admin a where a.email = :email and a.nickName = :nickName and a.deleted = false")
    boolean existsByEmailWithNickName(String email, String nickName);

    @Query("select a from Admin a where a.email = :email and a.nickName = :nickName and a.deleted = false")
    Optional<Admin> findByEmailWithNickName(String email, String nickName);

    @Query("select a from Admin a where a.nickName =:nickName and a.deleted = false")
    Optional<Admin> findByNickName(String nickName);

    @Query("select a from Admin a where a.email = :email and a.name = :name and a.deleted = false")
    Optional<Admin> findByEmailWithName(String email, String name);

    @Query("select a from Admin a where a.email = :email and a.loginId = :loginId and a.deleted = false")
    Optional<Admin> findByEmailWithLoginId(String email, String loginId);
}
