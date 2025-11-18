package com.example.attendance.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;

@Repository
public interface ChangeRequestRepository extends JpaRepository<Change_request,ChangeRequestId>{
	
	
	@Query(value = "SELECT * FROM change_request " +
            "WHERE user_id = :userId " +
            "AND TO_CHAR(application_date, 'YYYY-MM') = :yearMonth " +
            "ORDER BY request_no ASC",nativeQuery = true)
		List<Change_request> findByUserIdAndYearMonth(@Param("userId") int userId,
		                                                  @Param("yearMonth") String yearMonth);
	
	@Query("SELECT COALESCE(MAX(c.id.requestNo), 0) " +
	           "FROM Change_request c " +
	           "WHERE c.id.userId = :userId")
	    int getMaxRequestNo(@Param("userId") int userId);
	
	

}
