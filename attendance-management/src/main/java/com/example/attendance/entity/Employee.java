package com.example.attendance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "name")
	private String name;

	@Column(name = "department_id")
	private Integer department_id;
	
	

}
