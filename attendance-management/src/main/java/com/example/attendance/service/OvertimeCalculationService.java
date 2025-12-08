package com.example.attendance.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.repository.AttendanceInformationRepository;

//残業時間を計算

@Service
public class OvertimeCalculationService {

	@Autowired
	private AttendanceInformationRepository attendanceInformationRepository;

	public BigDecimal getMonthOvertime(Integer userId) {
		LocalDate now = LocalDate.now();
		LocalDate start = now.withDayOfMonth(1);
		LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

		return attendanceInformationRepository.overtimeclculation(userId, start, end);

	}

}
