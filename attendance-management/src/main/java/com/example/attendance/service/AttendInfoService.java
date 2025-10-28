package com.example.attendance.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.repository.AttendanceInformationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AttendInfoService {
	
	private final AttendanceInformationRepository attendanceInformationRepository;
	
	public List<Attendance_information> loadAttendInfoByUsername(AttendanceInformationId attendId,String yearMonth) {
		return attendanceInformationRepository.findByUserIdAndYearMonth(attendId.getUserId(),yearMonth);
	}

}
