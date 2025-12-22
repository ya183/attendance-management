package com.example.attendance.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	// ****外部キー****
	// 多対１
	@ManyToOne
	@JoinColumn(name = "department_id")
	// LombokでJPAがゲッターセッター自動生成
	private Department department;

	@Column(name = "gender")
	private Short gender;

	@Column(name = "age")
	private Short age;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "paid_leave_total")
	private Short paidLeaveTotal;

	@Column(name = "paid_leave_used")
	private Short paid_leave_used;

	@Column(name = "paid_leave_remaining")
	private Short paid_leave_remaining;

	@Column(name = "update_date")
	private LocalDate update_date;

	@Column(name = "update_by")
	private String update_by;

	@Column(name = "registration_date")
	private LocalDate registration_date;

	@Column(name = "created_by")
	private String created_by;

}
