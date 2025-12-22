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
@Table(name = "change_request")
@Data
public class Change_request {
	
	//　複合主キー
	@EmbeddedId
	private ChangeRequestId id;
	
	@ManyToOne
	@JoinColumn(
			name = "user_id",
			referencedColumnName = "user_id",
			insertable = false,
			updatable = false
			)
	private Employee employee;
	
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
	private short status = 1;
	
	//外部キーを持つ側
	@ManyToOne
	@JoinColumn(
			//Change_request側のstatus
			name = "status",
			//Request_status_master側のstatus
			referencedColumnName = "status",
			insertable = false,
			updatable = false
			)
	private Request_status_master requestStatusMaster;
	
	@Column(name = "update_date")
	private LocalDate update_date;

	@Column(name = "update_by")
	private String update_by;

	@Column(name = "registration_date")
	private LocalDate registration_date;

	@Column(name = "created_by")
	private String created_by;

}
