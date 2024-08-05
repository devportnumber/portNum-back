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

//    @Query(value = "SELECT id, name, category, start_date, end_date, stat, address\n" +
    @Query(value = "SELECT *\n" +
                    " FROM store s\n" +
                    "WHERE (:name is null or s.name = :name)\n" +
                    "  AND (:category is null or s.category = :category)\n" +
                    "  AND (:startDate is null or s.start_date = :startDate)\n" +
                    "  AND (:endDate is null or s.end_date = :endDate)\n" +
                    "  AND (:stat is null or s.stat = :stat)" , nativeQuery = true)
    List<AdminStore> findByfilter(@Param("name") String name, @Param("category") String category, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("stat") String stat);

//    @Query(value = "delete from store where id = :id", nativeQuery = true)
//    void deleteById(@Param("id") int id);
}
