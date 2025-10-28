package com.example.attendance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "attendance_information")
@Data
public class Attendance_information {
	
	//　複合主キー
	@EmbeddedId
	private AttendanceInformationId id;
	
//	@Id
//	@Column(name = "user_id")
//	private Integer userId;
//	
//	@Id
//	@Column(name = "date")
//	private LocalDate date;
	
	@Column(name = "clock_in")
	private LocalTime clock_in;
	
	@Column(name = "clock_out")
	private LocalTime clock_out;
	
	@Column(name = "break_time")
	private BigDecimal break_time;
	
	@Column(name = "working_hours")
	private BigDecimal working_hours;
	
	@Column(name = "overtime_hours")
	private BigDecimal overtime_hours;
	
	@Column(name = "status")
	private short status;

}
