package com.example.attendance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private short attendanceStatus;
	
	//外部キーを持つ側
		@ManyToOne
		@JoinColumn(
				//attendance_information側のstatus
				name = "status",
				//attendance_status_master側のstatus
				referencedColumnName = "status",
				insertable = false,
				updatable = false
				)
		private Attendance_status_master attendanceStatusMaster;
	
	
	@Column(name = "update_date")
	private LocalDate update_date;

	@Column(name = "update_by")
	private String update_by;

	@Column(name = "registration_date")
	private LocalDate registration_date;

	@Column(name = "created_by")
	private String created_by;

}
