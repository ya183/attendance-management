package com.example.attendance.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// 1ヶ月分のレコード生成の際のクラス
public class AttendanceForm {

	// 日付
	private LocalDate dayForm;
	// 勤怠状況
	private int statusAttend;
	// 出勤時刻
	private LocalTime startTime;
	// 退勤時刻
	private LocalTime endTime;
	// 休憩
	private BigDecimal breakTimeAttend;
	// 勤務時間
	private BigDecimal working;
	// 残業
	private BigDecimal overtime;

	// setter getter
	
	public int getStatusAttend() {
		return statusAttend;
	}

	public LocalDate getDayForm() {
		return dayForm;
	}

	public void setDayForm(LocalDate dayForm) {
		this.dayForm = dayForm;
	}

	public void setStatusAttend(int statusAttend) {
		this.statusAttend = statusAttend;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getBreakTimeAttend() {
		return breakTimeAttend;
	}

	public void setBreakTimeAttend(BigDecimal breakTimeAttend) {
		this.breakTimeAttend = breakTimeAttend;
	}

	public BigDecimal getWorking() {
		return working;
	}

	public void setWorking(BigDecimal working) {
		this.working = working;
	}

	public BigDecimal getOvertime() {
		return overtime;
	}

	public void setOvertime(BigDecimal overtime) {
		this.overtime = overtime;
	}

}
