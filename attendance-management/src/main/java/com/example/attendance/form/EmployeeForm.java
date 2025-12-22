package com.example.attendance.form;

public class EmployeeForm {

	private Integer userId;

	private String name;

	private String departmentName;
	
	private Short departmentId;

	private Short gender;

	private Short age;

	private String email;

	private String address;
	
	private Short paidLeaveTotal;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	

	public Short getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Short departmentId) {
		this.departmentId = departmentId;
	}

	public Short getGender() {
		return gender;
	}

	public void setGender(Short gender) {
		this.gender = gender;
	}

	public Short getAge() {
		return age;
	}

	public void setAge(Short age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Short getPaidLeaveTotal() {
		return paidLeaveTotal;
	}

	public void setPaidLeaveTotal(Short paidLeaveTotal) {
		this.paidLeaveTotal = paidLeaveTotal;
	}
	
	

}
