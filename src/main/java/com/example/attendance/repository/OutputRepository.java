package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.attendance.entity.Employee;
import com.ezample.attendance.dto.OutputSearchDto;
import com.ezample.attendance.dto.SearchOverTimeUserDto;

public interface OutputRepository extends JpaRepository<Employee,Integer>{
	
	@Query(value = "SELECT u.user_id AS userId,u.name AS name,TO_CHAR(a.date,'YYYY/MM') AS yearMonth "
			+ "FROM attendance_information a "
			+ "JOIN employee u ON a.user_id = u.user_id "
			+ "WHERE a.date BETWEEN :start AND :end "
			+ "AND u.name LIKE CONCAT('%',:username,'%')"
			+ "GROUP BY u.user_id,u.name,yearMonth "
			+ "ORDER BY u.user_id DESC ",nativeQuery = true)
	List<OutputSearchDto> outputSearch(@Param("start") LocalDate start,
			@Param("end") LocalDate end,@Param("username") String username);


}
