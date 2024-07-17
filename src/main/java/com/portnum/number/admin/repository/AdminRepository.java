package com.portnum.number.admin.repository;

import com.portnum.number.admin.domain.AdminStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 팝업 JPA 저장소
 */
public interface AdminRepository extends JpaRepository<AdminStore, Integer> {

    List<AdminStore> findByName(String name);

    @Query(value = "select id, name, category, start_date, end_date, stat\n" +
            "from  store s\n" +
            "where s.name like %:name%\n" +
            "or   s.category like %:cate%\n" +
            "or   s.start_date like %:sDate%\n" +
            "or   s.end_date like %:eDate%\n" +
            "or   s.stat like %:stat%", nativeQuery = true)
    List<AdminStore> findByFilter(@Param("name") String name, @Param("cate") String category, @Param("sDate") String startDate, @Param("eDate") String endDate, @Param("stat") String stat);

    List<AdminStore> findByNameOrCategoryOrStartDateOrEndDateOrStat(String name, String category, String startDate, String endDate, String stat);

    @Transactional
    public void deleteById(Integer id);
}
