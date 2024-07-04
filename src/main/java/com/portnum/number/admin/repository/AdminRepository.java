package com.portnum.number.admin.repository;

import com.portnum.number.admin.domain.AdminStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 팝업 JPA 저장소
 */
public interface AdminRepository extends JpaRepository<AdminStore, Integer> {

    List<AdminStore> findByName(String name);

    @Query(value = "select name, category, start_date, end_date, stat" +
            "from  store s" +
            "where s.name like %:name%" +
            "and   s.category like %:cate%" +
            "and   s.start_date like %:sDate%" +
            "and   s.end_date like %:eDate%" +
            "and   s.stat like %:stat%", nativeQuery = true)
    AdminStore findByFilter1(@Param("name") String name, @Param("cate") String category, @Param("sDate") String startDate, @Param("eDate") String endDate, @Param("stat") String stat);
}
