package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.attendance.entity.LeaveApplicationId;
import com.example.attendance.entity.Leave_application;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<Leave_application, LeaveApplicationId> {

	@Query(value = "SELECT * FROM Leave_application " + "WHERE user_id = :userId "
			+ "AND TO_CHAR(application_date, 'YYYY-MM') = :yearMonth " + "ORDER BY request_no ASC", nativeQuery = true)
	List<Leave_application> findByUserIdAndYearMonth(@Param("userId") int userId, @Param("yearMonth") String yearMonth);

	@Query("SELECT COALESCE(MAX(c.id.requestNo), 0) " + "FROM Leave_application c " + "WHERE c.id.userId = :userId")
	int getMaxRequestNo(@Param("userId") int userId);

}