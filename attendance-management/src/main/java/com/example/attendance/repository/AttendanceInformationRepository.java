package com.example.attendance.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.ezample.attendance.dto.SearchOverTimeUserDto;

@Repository
public interface AttendanceInformationRepository
		extends JpaRepository<Attendance_information, AttendanceInformationId> {

	// 1月分のレコードを検索
	@Query(value = "SELECT * FROM attendance_information " + "WHERE user_id = :userId "
			+ "AND TO_CHAR(date, 'YYYY-MM') = :yearMonth " +
            "ORDER BY date ASC",nativeQuery = true)
	List<Attendance_information> findByUserIdAndYearMonth(@Param("userId") int userId,
			@Param("yearMonth") String yearMonth);

	// 出勤登録
	@Modifying
	@Transactional
	@Query("UPDATE Attendance_information a " + "SET a.clock_in = :clock_in "
			+ "WHERE a.id.userId = :userId AND a.id.date = :date")
	int updateclockIn(@Param("userId") Integer userId, @Param("date") LocalDate date,
			@Param("clock_in") LocalTime clock_in);

	// 退勤登録
	@Modifying
	@Transactional
	@Query("UPDATE Attendance_information a " + "SET a.clock_out = :clock_out "
			+ "WHERE a.id.userId = :userId AND a.id.date = :date")
	int updateclockOut(@Param("userId") Integer userId, @Param("date") LocalDate date,
			@Param("clock_out") LocalTime clock_in);
	
	// 残業時間計算（一般ユーザダッシュボード用）
	@Query(value = "SELECT SUM(overtime_hours) AS total "
			+ "FROM attendance_information " +
            "WHERE user_id = :userId AND date BETWEEN :start AND :end;",nativeQuery = true)
	BigDecimal overtimeclculation(@Param("userId") Integer userId, @Param("start") LocalDate start,
			@Param("end") LocalDate end);
	
	// 全社員残業時間計算（管理者ダッシュボード用）
	@Query(value = "SELECT COUNT(*) AS over20 "
			+ "FROM(SELECT user_id,SUM(overtime_hours) AS total "
			+ "FROM attendance_information "
			+ "WHERE date BETWEEN :start AND :end "
			+ "GROUP BY user_id "
			+ "HAVING SUM(overtime_hours) > 20 "
			+ ")t",nativeQuery = true)
	int overtimesum(@Param("start") LocalDate start,
			@Param("end") LocalDate end);
	
	//管理者ダッシュボード用検索
	@Query(value = "SELECT u.user_id AS userId,u.name AS userName,SUM(a.overtime_hours) AS total_overtime "
			+ "FROM attendance_information a "
			+ "JOIN Employee u ON a.user_id = u.user_id "
			+ "WHERE a.date BETWEEN :start AND :end "
			+ "GROUP BY u.user_id,u.name "
			+ "HAVING SUM(overtime_hours) > 20 "
			+ "ORDER BY u.user_id DESC ",nativeQuery = true)
	List<SearchOverTimeUserDto> overtimeserch(@Param("start") LocalDate start,
			@Param("end") LocalDate end);

	
}