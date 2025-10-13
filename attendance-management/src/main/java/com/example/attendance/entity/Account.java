package com.example.attendance.entity;

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

}
