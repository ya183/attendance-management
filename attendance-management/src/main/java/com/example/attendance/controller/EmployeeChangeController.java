package com.example.attendance.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmployeeChangeController {
	
	// 社員検索/変更/削除トップページ
		@GetMapping("/employeechange")
		public ModelAndView register(ModelAndView mv, Principal principal,
				@RequestParam(value = "date", required = false) String date) {
			mv.setViewName("employeechange");

			String loginId = principal.getName();
			mv.addObject("loginId", loginId);

			return mv;
		}

}
