// 休暇申請関連サービス
package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.entity.LeaveApplicationId;
import com.example.attendance.entity.Leave_application;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.LeaveInformationRepository;

import jakarta.transaction.Transactional;

@Service
public class LeaveService {

	@Autowired
	private LeaveInformationRepository leaveRepository;
	
	@Autowired
	private AttendanceInformationRepository attendanceInformationRepository;

	// 管理者承認処理
	@Transactional
	public boolean approve(Integer userId, Integer requestNo) {

		LeaveApplicationId leaveId = new LeaveApplicationId(userId, requestNo);
		// 対象の休暇申請取得
		Leave_application datail = leaveRepository.findById(leaveId).orElse(null);
		// ステータスを完了(2)にセット
		datail.setStatus((short) 2);
		
		// 有給日数増減
		int leaveSubtraction = leaveRepository.useLeave(userId);
		
		if(leaveSubtraction == 0) {
			throw new IllegalStateException("総有給日数が不足しています。");
		}
		
		// 勤怠情報更新
		AttendanceInformationId attendanceId =
			    new AttendanceInformationId(userId, datail.getChange_date());

			Attendance_information attendance =
			    attendanceInformationRepository.findById(attendanceId).orElse(null);
			//　ステータスを休日に変更
			attendance.setAttendanceStatus((short) 5);
	        
	        attendanceInformationRepository.save(attendance);

		return true;
	}

}
