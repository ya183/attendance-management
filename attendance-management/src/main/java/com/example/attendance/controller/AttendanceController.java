package com.example.attendance.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Account;
import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.service.AttendInfoService;
import com.example.attendance.service.LoginService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AttendanceController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private AttendanceRepository attendanceRepository;

	private final AttendanceInformationRepository attendanceInformationRepository;

	private final AttendInfoService attendInfoService;

	// テスト用（コンソールにて値出力）
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

	// 一般ユーザーホーム画面表示
	@GetMapping("/user/home")
	public ModelAndView showHome(ModelAndView mv) {
		mv.setViewName("userhome");
		mv.addObject("account", new Account());
		return mv;
	}

	// 管理者ユーザーホーム画面表示
	@GetMapping("/admin/home")
	public ModelAndView showadminHome(ModelAndView mv) {
		mv.setViewName("adminhome");
		mv.addObject("account", new Account());
		return mv;
	}

	// リダイレクト
	@GetMapping("/redirectHome")
	public String redirectByRole(Authentication auth) {

		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			return "redirect:/admin/home";
		} else {
			return "redirect:/user/home";
		}
	}

	// 勤怠情報
	@GetMapping("/attendanceinformation")
	public ModelAndView attendanceInformation(ModelAndView mv, Principal principal) {
		mv.setViewName("attendanceinformation");

		if (principal == null) {
			// 未ログインならログインページへリダイレクト
			mv.setViewName("redirect:/login");
			return mv;
		}

		Integer id;
		// userIdがinteger型かどうか判断
		try {
			id = Integer.valueOf(principal.getName());
		} catch (NumberFormatException e) {
			// Spring Securityに認証失敗として処理（/login/errorに遷移）
			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
		}

		// DBからユーザー情報取得
		Account account = attendanceRepository.findByUserId(id);
		System.out.println("アカウント" + account);

		mv.addObject("account", account);
		mv.addObject("attendanceinformation", new ArrayList<>());

		return mv;
	}

	// 勤怠情報表示ボタン押下後（フォーム送信後の処理）
	@PostMapping("/attendanceinformation/")
	public String attendanceInfoSearch(@RequestParam("inputDate") String yearMonth,
			@RequestParam(value = "userId", required = false) Integer userId, Model model) {

		// ログイン中のユーザー情報を取得
		if (userId == null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			userId = Integer.valueOf(auth.getName());
		}
		// URLパラメーターでuserIdとdateの情報を渡す
		return "redirect:/attendanceinformation/info?userId=" + userId + "&date=" + yearMonth;
	}

	// 勤怠情報表示ボタン押下後（画面表示用）
	@GetMapping("/attendanceinformation/info")
	public ModelAndView infoSearch(@RequestParam("userId") int userId, @RequestParam("date") String yearMonth,
			ModelAndView mv) {
		// HTMLファイルを指定
		mv.setViewName("attendanceinformation");

		// 日にちを追加し月初を挿入
		LocalDate date = LocalDate.parse(yearMonth + "-01");
		// 複合キーにてユーザ、月の情報を特定
		AttendanceInformationId attendId = new AttendanceInformationId(userId, date);

		// DBから検索した結果をリストに格納
		List<Attendance_information> attendanceList = attendInfoService.loadAttendInfoByUsername(attendId, yearMonth);

		// HTMLにデータを渡す
		mv.addObject("attendanceinformation", attendanceList);
		mv.addObject("yearMonth", yearMonth);

		return mv;
	}

}
