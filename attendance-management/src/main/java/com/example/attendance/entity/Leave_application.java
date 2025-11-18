package com.example.attendance.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "leave_application")
@Data
public class Leave_application {
	
	//　複合主キー
		@EmbeddedId
		private LeaveApplicationId id;
		
		@Column(name = "application_date")
		private LocalDate application_date;
		
		@Column(name = "change_date")
		private LocalDate change_date;
		
		@Column(name = "reason")
		private String reason;
		
		@Column(name = "status")
		private short status;

}
