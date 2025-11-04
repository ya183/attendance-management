package com.example.attendance.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AttendRegisterController {

	// 勤怠登録
	@GetMapping("/attendregister")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("attendregister");
		return mv;
	}

	// 出勤登録
	@PostMapping("/attendregister/clocin")
	public ModelAndView clockinregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("attendregister");
		return mv;
	}
}
