package com.example.attendance.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AttendRegisterController {

	private final AttendanceInformationRepository attendanceInformationRepository;

	@Autowired
	private AttendanceRepository attendanceRepository;

	// 勤怠登録
	@GetMapping("/attendregister")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("attendregister");
		
		String loginId = principal.getName();
		mv.addObject("loginId",loginId);
		
		return mv;
	}

	// 出勤登録
	@PostMapping("/attendregister/clocin")
	public ModelAndView clockinregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("date") String date,
			@RequestParam("timeInput") String timeInput,RedirectAttributes redirectAttributes) {
		mv.setViewName("attendregister");

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

		// DBからユーザー情報取得
		Account account = attendanceRepository.findByUserId(id);

		// 1. LocalDate に変換
		LocalDate datestr = LocalDate.parse(date); // yyyy-MM-dd 形式で送信されること前提

		// 2. 時刻を LocalTime に変換（例: 9.5 → 9:30）
//	    int hours = (int) timeInput;
//	    int minutes = (int) ((timeInput - hours) * 60);
		LocalTime clockInTime = LocalTime.parse(timeInput);

		// デバッグ
//	    System.out.println("【受信】userId = " + id);
//	    System.out.println("【受信】date = " + date);
//	    System.out.println("【受信】timeInput = " + timeInput);
//
//	    LocalDate localDate = LocalDate.parse(date);
//	    LocalTime localTime = LocalTime.parse(timeInput);
//	    System.out.println("【変換】localDate = " + localDate + ", localTime = " + localTime);

		int result = attendanceInformationRepository.updateclockIn(id, datestr, clockInTime);
//		System.out.println("結果" + result);
		
		if(result > 0) {
			System.out.println("結果" + result);
			redirectAttributes.addFlashAttribute("clockInSuccess",true);
		}else {
			redirectAttributes.addFlashAttribute("clockInSuccess",false);
		}

		return mv;
	}

	@PostMapping("/attendregister/clocout")
	public ModelAndView clockoutregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId, @RequestParam("date") String date,
			@RequestParam("timeInput") String timeInput,RedirectAttributes redirectAttributes) {
		mv.setViewName("attendregister");

	    System.out.println("【受信】date = " + date);
	    System.out.println("【受信】timeInput = " + timeInput);
		
		
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

		// DBからユーザー情報取得
		//Account account = attendanceRepository.findByUserId(id);

		// 1. LocalDate に変換
		LocalDate datestr = LocalDate.parse(date); // yyyy-MM-dd 形式で送信されること前提

		// 2. 時刻を LocalTime に変換（例: 9.5 → 9:30）
//	    int hours = (int) timeInput;
//	    int minutes = (int) ((timeInput - hours) * 60);
		LocalTime clockInTime = LocalTime.parse(timeInput);

		// デバッグ
//	    System.out.println("【受信】userId = " + id);
//	    System.out.println("【受信】date = " + date);
//	    System.out.println("【受信】timeInput = " + timeInput);
//
//	    LocalDate localDate = LocalDate.parse(date);
//	    LocalTime localTime = LocalTime.parse(timeInput);
//	    System.out.println("【変換】localDate = " + localDate + ", localTime = " + localTime);

		int result = attendanceInformationRepository.updateclockOut(id, datestr, clockInTime);
//		System.out.println("結果" + result);
		
		if(result > 0) {
			redirectAttributes.addFlashAttribute("clockOutSuccess",true);
		}else {
			redirectAttributes.addFlashAttribute("clockOutSuccess",false);
		}

		return mv;
	}

}
