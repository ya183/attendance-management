package com.example.attendance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.repository.AttendanceInformationRepository;
import com.ezample.attendance.dto.SearchOverTimeUserDto;

//残業時間を計算

@Service
public class OvertimeCalculationService {

	@Autowired
	private AttendanceInformationRepository attendanceInformationRepository;
	
	//(一般ユーザダッシュボード用)
	public BigDecimal getMonthOvertime(Integer userId) {
		LocalDate now = LocalDate.now();
		LocalDate start = now.withDayOfMonth(1);
		LocalDate end = now.withDayOfMonth(now.lengthOfMonth());

		return attendanceInformationRepository.overtimeclculation(userId, start, end);

	}
	
	//管理者ダッシュボード用
	
	// ダッシュボード残業時間20H越えユーザの数
	public int getAllUserOverTime() {
		LocalDate now = LocalDate.now();
		LocalDate start = now.withDayOfMonth(1);
		LocalDate end = now.withDayOfMonth(now.lengthOfMonth());
		
		return attendanceInformationRepository.overtimesum(start, end);
	}
	

	// 管理者ダッシュボード検索用
		public List<SearchOverTimeUserDto> seachAllUserOverTime(String yearMonth) {
			YearMonth selectmonth = YearMonth.parse(yearMonth);
			LocalDate start = selectmonth.atDay(1);
			LocalDate end = selectmonth.atEndOfMonth();
			
			return attendanceInformationRepository.overtimeserch(start, end);
		}

		
	
}
