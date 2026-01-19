/**
 * 「勤怠登録」コントローラ
 * 出退勤を管理するクラス
 *
 */
package com.example.attendance.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.entity.Account;
import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.EmployeeChengeRepository;

import java.time.Duration;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AttendRegisterController {

	private final AttendanceInformationRepository attendanceInformationRepository;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private AttendanceInformationRepository attendInfoRepository;

	@Autowired
	private EmployeeChengeRepository employeeChengeRepository;

	// 勤怠登録
	@GetMapping("/attendregister")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("attendregister");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

	// 出勤登録
	@PostMapping("/attendregister")
	public ModelAndView clockinregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "startInput", required = false) LocalTime clockIn,
			@RequestParam(value = "endInput", required = false) LocalTime clockOut,
			@RequestParam(value = "breakInput", required = false) BigDecimal breakTime,
			RedirectAttributes redirectAttributes) {

		// ログイン中のユーザーNullチェック
		if (principal == null) {
			// 未ログインならログインページへリダイレクト
			mv.setViewName("redirect:/login");
			return mv;
		}

		Integer id;
		// userIdがinteger型かどうか判断
		try {
			// userIdを数値化して取得
			id = Integer.valueOf(principal.getName());
		} catch (NumberFormatException e) {
			// Spring Securityに認証失敗として処理（/login/errorに遷移）
			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
		}

		if (breakTime == null) {
			breakTime = BigDecimal.ZERO;
		}

		// 1. LocalDate に変換
		LocalDate datestr = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-M-d"));

		AttendanceInformationId pk = new AttendanceInformationId(id, datestr);
		Attendance_information attendance = attendInfoRepository.findById(pk).orElseThrow();

		mv.setViewName("redirect:/attendregister");
		
		// 出勤時間、退勤時間未入力の場合ステータスを未出勤
		if (clockIn == null && clockOut == null) {
			attendance.setAttendanceStatus((short) 1);
			try {
			attendInfoRepository.save(attendance);
			redirectAttributes.addFlashAttribute("clockInSuccess", true);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("clockInSuccess", false);
			}
			return mv;
		}

		// 出勤時間のみの場合ステータスを勤務中
		if (clockIn != null && clockOut == null) {
			attendance.setClock_in(clockIn);
			attendance.setBreak_time(breakTime);
			attendance.setAttendanceStatus((short) 2);
			try {
			attendInfoRepository.save(attendance);
			redirectAttributes.addFlashAttribute("clockInSuccess", true);
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("clockInSuccess", false);
			}
			return mv;
		}

		// 出退勤有の場合ステータスを退勤
		if (clockIn != null && clockOut != null) {
			attendance.setClock_in(clockIn);
			attendance.setClock_out(clockOut);
			attendance.setBreak_time(breakTime);
		}

		// ----------勤務時間計算--------------------

		// 時間
		BigDecimal breaTimeHours = breakTime;

		// 時間を分に変換
		long breakMinutes = breaTimeHours.multiply(BigDecimal.valueOf(60)).longValueExact();

		// 勤務時間をDurationで作成
		Duration workingDuration = Duration.between(clockIn, clockOut).minusMinutes(breakMinutes);

		// 実務時間の時間部分を取り出す
		long hours = workingDuration.toHours();
		// 実務時間の分を取り出す
		long minutes = workingDuration.toMinutesPart();

		// BigDecimal型に変換
		BigDecimal workingHours = BigDecimal.valueOf(hours)
				.add(BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));

		// ------------------------------------------

		attendance.setWorking_hours(workingHours);

		// ----------残業時間計算--------------------

		Employee employee = employeeChengeRepository.findById(attendance.getId().getUserId()).orElseThrow();

		// 実勤務時間
		Duration totalWorkingTime = Duration.between(clockIn, clockOut).minusMinutes(breakMinutes);

		System.out.println("実務時間" + totalWorkingTime);

		// 基準勤務時間
		BigDecimal standardWorkHours = employee.getStandard_work_hours();

		System.out.println("基準勤務時間" + standardWorkHours);

		// 基準勤務時間をDurationに変換
		Duration standardDuration = Duration
				.ofMinutes(standardWorkHours.multiply(BigDecimal.valueOf(60)).longValueExact());

		// 残業時間算出
		Duration overTime = totalWorkingTime.minus(standardDuration);

		if (overTime.isNegative()) {
			overTime = Duration.ZERO;
		}

		BigDecimal overtimeHours = BigDecimal.valueOf(overTime.toMinutes()).divide(BigDecimal.valueOf(60), 2,
				RoundingMode.DOWN);

		// ------------------------------------------

		attendance.setOvertime_hours(overtimeHours);

		// ステータス変更
//		if (clockOut != null) {
		// 退勤
		attendance.setAttendanceStatus((short) 3);
//		} else {
//		    // 勤務中
//			attendance.setAttendanceStatus((short) 2);
//		}

		try {
			attendInfoRepository.save(attendance);
			redirectAttributes.addFlashAttribute("clockInSuccess", true);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("clockInSuccess", false);
		}

		//mv.setViewName("redirect:/attendregister");

		return mv;
	}

//	@PostMapping("/attendregister/clocout")
//	public ModelAndView clockoutregister(ModelAndView mv, Principal principal,
//			@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("date") String date,
//			@RequestParam("timeInput") String timeInput,RedirectAttributes redirectAttributes) {
//		mv.setViewName("attendregister");
//
//	    System.out.println("【受信】date = " + date);
//	    System.out.println("【受信】timeInput = " + timeInput);
//		
//		
//		// ログイン中のユーザーNullチェック
//		if (principal == null) {
//			// 未ログインならログインページへリダイレクト
//			mv.setViewName("redirect:/login");
//			return mv;
//		}
//
//		Integer id;
//		// userIdがinteger型かどうか判断
//		try {
//			// userIdを数値化して取得
//			id = Integer.valueOf(principal.getName());
//		} catch (NumberFormatException e) {
//			// Spring Securityに認証失敗として処理（/login/errorに遷移）
//			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
//		}
//
//		// DBからユーザー情報取得
//		//Account account = attendanceRepository.findByUserId(id);
//
//		// 1. LocalDate に変換
//		LocalDate datestr = LocalDate.parse(date); // yyyy-MM-dd 形式で送信されること前提
//
//		// 2. 時刻を LocalTime に変換（例: 9.5 → 9:30）
////	    int hours = (int) timeInput;
////	    int minutes = (int) ((timeInput - hours) * 60);
//		LocalTime clockInTime = LocalTime.parse(timeInput);
//
//		// デバッグ
////	    System.out.println("【受信】userId = " + id);
////	    System.out.println("【受信】date = " + date);
////	    System.out.println("【受信】timeInput = " + timeInput);
////
////	    LocalDate localDate = LocalDate.parse(date);
////	    LocalTime localTime = LocalTime.parse(timeInput);
////	    System.out.println("【変換】localDate = " + localDate + ", localTime = " + localTime);
//
//		int result = attendanceInformationRepository.updateclockOut(id, datestr, clockInTime);
////		System.out.println("結果" + result);
//		
//		if(result > 0) {
//			redirectAttributes.addFlashAttribute("clockOutSuccess",true);
//		}else {
//			redirectAttributes.addFlashAttribute("clockOutSuccess",false);
//		}
//
//		return mv;
//	}

}
