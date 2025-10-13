package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AttendanceController {

	// ログインページ表示
	@GetMapping("/login")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}

	// ホーム画面表示
	@GetMapping("/home")
	public ModelAndView showHome(ModelAndView mv) {
		mv.setViewName("home");
		return mv;
	}

	// 勤怠情報
	@GetMapping("/attendanceinformation")
	public ModelAndView attendanceInformation(ModelAndView mv) {
		mv.setViewName("attendanceinformation");
		return mv;

	}
}
