package com.example.attendance.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AttendanceInformationId implements Serializable {

	@Column(name = "user_id")
	private int userId;
	@Column(name = "date")
	private LocalDate date;
	
	public AttendanceInformationId() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public AttendanceInformationId(int userId,LocalDate date) {
		this.userId = userId;
		this.date = date;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}

}
