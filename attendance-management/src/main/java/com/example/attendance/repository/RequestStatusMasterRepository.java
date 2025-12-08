package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.entity.Request_status_master;

public interface RequestStatusMasterRepository extends JpaRepository<Request_status_master, Short>{
	
	

}
