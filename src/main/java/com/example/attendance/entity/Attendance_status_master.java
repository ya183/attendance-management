package com.example.attendance.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "attendance_status_master")
@Data
public class Attendance_status_master {
	@Id
	@Column(name = "status")
	private short attendanceStatus;
	
	@Column(name = "status_name")
	private String status_name;
	
	@Column(name = "update_date")
	private LocalDate update_date;

	@Column(name = "update_by")
	private String update_by;

	@Column(name = "registration_date")
	private LocalDate registration_date;

	@Column(name = "created_by")
	private String created_by;
	
	@Column(name = "display_flag")
	private Boolean displayFlag;

}
