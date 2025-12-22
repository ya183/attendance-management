package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.attendance.entity.LeaveApplicationId;
import com.example.attendance.entity.Leave_application;

@Repository
public interface LeaveInformationRepository extends JpaRepository<Leave_application, LeaveApplicationId> {
	@Query(value = "SELECT * FROM Leave_application " + "WHERE TO_CHAR(application_date, 'YYYY-MM') = :yearMonth "
			+ "ORDER BY request_no ASC", nativeQuery = true)
	List<Leave_application> findByYearMonth(@Param("yearMonth") String yearMonth);

	// 休暇申請管理者承認処理
	@Modifying
	@Query("""
			    UPDATE Employee e
			       SET e.paid_leave_used      = e.paid_leave_used + 1,
			           e.paid_leave_remaining = e.paidLeaveTotal - (e.paid_leave_used + 1),
			           e.update_date          = CURRENT_DATE
			     WHERE e.userId = :userId
			       AND e.paid_leave_remaining >= 1
			""")
	int useLeave(@Param("userId") Integer userId);

}
