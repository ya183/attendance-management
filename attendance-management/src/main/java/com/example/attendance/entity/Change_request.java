package com.example.attendance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "change_request")
@Data
public class Change_request {
	
	//　複合主キー
	@EmbeddedId
	private ChangeRequestId id;
	
	@Column(name = "application_date")
	private LocalDate application_date;
	
	@Column(name = "change_date")
	private LocalDate change_date;
	
	@Column(name = "revised_clock_in")
	private LocalTime revised_clock_in;
	
	@Column(name = "revised_clock_out")
	private LocalTime revised_clock_out;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "status")
	private short status;

}
