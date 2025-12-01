package com.example.attendance.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "department")
@Data
public class Department {
	
	@Id
	@Column(name = "department_id")
	private Short departmentId;
	
	@Column(name = "department_name")
	private String department_name;
	
	@Column(name = "update_date")
	private LocalDate update_date;
	
	@Column(name = "update_by")
	private String update_by;
	
	@Column(name = "registration_date")
	private LocalDate registration_date;
	
	@Column(name = "created_by")
	private String created_by;

}
