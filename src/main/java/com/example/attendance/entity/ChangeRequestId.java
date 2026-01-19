package com.example.attendance.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ChangeRequestId implements Serializable{
	
	@Column(name = "user_id")
	private int userId;
	@Column(name = "request_no")
	private int requestNo;
	
	public ChangeRequestId() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public ChangeRequestId(int userId,int requestNo) {
		this.userId = userId;
		this.requestNo = requestNo;
		
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(int requestNo) {
		this.requestNo = requestNo;
	}
	
	

}
