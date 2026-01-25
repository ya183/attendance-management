package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.entity.Attendance_status_master;


public interface AttendanceStatusMasterRepository extends JpaRepository<Attendance_status_master, Short>{
	
	List<Attendance_status_master> findByDisplayFlagTrue();

}
