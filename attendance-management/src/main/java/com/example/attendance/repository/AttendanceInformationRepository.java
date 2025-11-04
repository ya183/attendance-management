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

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;

@Repository
public interface AttendanceInformationRepository
		extends JpaRepository<Attendance_information, AttendanceInformationId> {

	// 1月分のレコードを検索
	@Query(value = "SELECT * FROM attendance_information " + "WHERE user_id = :userId "
			+ "AND TO_CHAR(date, 'YYYY-MM') = :yearMonth", nativeQuery = true)
	List<Attendance_information> findByUserIdAndYearMonth(@Param("userId") int userId,
			@Param("yearMonth") String yearMonth);

//	// 出勤登録
//	@Modifying
//	@Transactional
//	@Query("UPDATE Attendance_information a " + "SET a.clock_in = :clockin "
//			+ "WHERE a.user_id = :userId AND date = :date")
//	void updateclockIn(@Param("userId") Integer userId, @Param("date") LocalDate date,
//			@Param("clock_in") LocalTime clock_in);
}