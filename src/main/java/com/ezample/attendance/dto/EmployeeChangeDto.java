package com.ezample.attendance.dto;

import lombok.Data;

@Data
public class EmployeeChangeDto {
	
	private Integer userId;

	private String name;

	private String department_name;

	private Short gender;

	private Short age;

	private String email;

	private String address;
	
	private Short paidLeaveTotal;
	
	

}
