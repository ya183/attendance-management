package com.example.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "request_status_master")
@Data
public class Request_status_master {
	
	@Id
	@Column(name = "status")
	private short status;
	
	@Column(name = "status_name")
	private String status_name;

}
