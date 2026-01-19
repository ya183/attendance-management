package com.example.attendance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.form.AttendanceFormList;
import com.example.attendance.repository.AttendanceInformationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AttendInfoService {

	// 1ヶ月分の勤怠レコードのリポジトリ
	private final AttendanceInformationRepository attendanceInformationRepository;

	// ユーザの１ヶ月分の勤怠情報取得
	public List<Attendance_information> loadAttendInfoByUsername(AttendanceInformationId attendId, String yearMonth) {
		return attendanceInformationRepository.findByUserIdAndYearMonth(attendId.getUserId(), yearMonth);
	}

	// 入力された1ヶ月分の勤怠レコードをDB更新
	public void saveAllList(List<AttendanceForm> attendForm) {

		// ログイン中のユーザID取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 数値に変換
		int userId = Integer.parseInt(auth.getName());

		// 入力されたデータのリストをDBに更新できる形（entity）に変換
		List<Attendance_information> entityList = attendForm.stream().map(f -> {

			// entity生成
			Attendance_information entity = new Attendance_information();
			// 複合キーの生成
			AttendanceInformationId id = new AttendanceInformationId(userId, f.getDayForm());

			// 主キー(id)を設定
			entity.setId(id);
			entity.setBreak_time(f.getBreakTimeAttend());
			entity.setClock_in(f.getStartTime());
			entity.setClock_out(f.getEndTime());
			entity.setOvertime_hours(f.getOvertime());
			entity.setAttendanceStatus((short) f.getStatusAttend());
			entity.setWorking_hours(f.getWorking());
			return entity;

		}).collect(Collectors.toList());
		
		// repositoryのSpring Data JPA が自動で提供しているメソッド
		attendanceInformationRepository.saveAll(entityList);
	}

}
