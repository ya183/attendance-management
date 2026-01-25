package com.example.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.ChangeInformationRepository;
import com.example.attendance.repository.ChangeRequestRepository;

@Service
public class ChangeRequestService {

	@Autowired
	private ChangeInformationRepository changeInformationRepository;

	@Autowired
	private AttendanceInformationRepository attendanceInformationRepository;

	@Transactional
	public void approve(Integer userId, Integer requestNo) {

		// 変更申請取得
		ChangeRequestId changeId = new ChangeRequestId(userId, requestNo);
		Change_request datail = changeInformationRepository.findById(changeId).orElse(null);

		// 勤怠情報更新
		AttendanceInformationId attendanceId = new AttendanceInformationId(userId, datail.getChange_date());

		Attendance_information attendance = attendanceInformationRepository.findById(attendanceId).orElse(null);

		attendance.setClock_in(datail.getRevised_clock_in());
		attendance.setClock_out(datail.getRevised_clock_out());

		attendanceInformationRepository.save(attendance);

		// ステータス更新
		datail.setStatus((short) 2);
		Change_request changeSave = changeInformationRepository.save(datail);

	}

//    public List<Change_request> getRequestsByUser(int userId) {
//        return changeRequestRepository.findByIdUserIdOrderByApplication_dateDesc(userId);
//    }

}
