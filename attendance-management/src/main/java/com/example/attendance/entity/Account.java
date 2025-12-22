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
@Table(name = "account")
@Data
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "password")
	private String password;

	@Column(name = "authority")
	private Integer authority;
	
	@Column(name = "update_date")
	private LocalDate update_date;

	@Column(name = "update_by")
	private String update_by;

	@Column(name = "registration_date")
	private LocalDate registration_date;

	@Column(name = "created_by")
	private String created_by;

}
