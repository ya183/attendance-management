package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;

@Repository
public interface ChangeInformationRepository extends JpaRepository<Change_request,ChangeRequestId>{
	
	@Query(value = "SELECT * FROM change_request " +
            "WHERE TO_CHAR(application_date, 'YYYY-MM') = :yearMonth " +
            "ORDER BY request_no ASC",nativeQuery = true)
		List<Change_request> findByYearMonth(@Param("yearMonth") String yearMonth);

}
