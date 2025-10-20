package com.example.attendance.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Account;
import com.example.attendance.service.LoginService;

@Controller
public class AttendanceController {

	@Autowired
    private LoginService loginService;
	
	//テスト用（コンソールにて値出力）
//	@GetMapping("/test")
//	public String testHash() {
//	    loginService.showHashedPassword("Passadm2");
//	    return "login";
//	}
	
	
	// ログインページ表示
	@GetMapping("/login")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}
	
	//一般ユーザーホーム画面表示
	@GetMapping("/user/home")
	public ModelAndView showHome(ModelAndView mv) {
		mv.setViewName("userhome");
		mv.addObject("account",new Account());
		return mv;
	}
	
	//管理者ユーザーホーム画面表示
	@GetMapping("/admin/home")
	public ModelAndView showadminHome(ModelAndView mv) {
		mv.setViewName("adminhome");
		mv.addObject("account",new Account());
		return mv;
	}
	
	
	//リダイレクト
	@GetMapping("/redirectHome")
	public String redirectByRole(Authentication auth) {
		
		if (auth.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
	        return "redirect:/admin/home";
	    } else {
	        return "redirect:/user/home";
	    }
	}
	

	// 勤怠情報
	@GetMapping("/attendanceinformation")
	public ModelAndView attendanceInformation(ModelAndView mv) {
		mv.setViewName("attendanceinformation");
		return mv;

	}
}
