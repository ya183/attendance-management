package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;

@Repository
public interface AttendanceInformationRepository extends JpaRepository<Attendance_information,AttendanceInformationId> {
	
	@Query(value = "SELECT * FROM attendance_information "
            + "WHERE user_id = :userId "
            + "AND TO_CHAR(date, 'YYYY-MM') = :yearMonth", nativeQuery = true)
	List<Attendance_information> findByUserIdAndYearMonth(@Param("userId") int userId,@Param("yearMonth") String yearMonth);
}