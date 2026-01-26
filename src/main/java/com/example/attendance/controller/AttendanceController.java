/**
 * ログイン関係コントローラ
 * 一般ユーザ、管理者のページ切り替えを管理するクラス
 *
 */
package com.example.attendance.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.example.attendance.entity.Account;
import com.example.attendance.entity.Employee;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.example.attendance.service.AttendInfoService;
import com.example.attendance.service.LoginService;
import com.example.attendance.service.OvertimeCalculationService;
import com.ezample.attendance.dto.SearchOverTimeUserDto;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AttendanceController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private AttendanceRepository attendanceRepository;
	@Autowired
	private EmployeeChengeRepository employeeChengeRepository;

	private final AttendanceInformationRepository attendanceInformationRepository;

	private final AttendInfoService attendInfoService;

	private final OvertimeCalculationService overtimeCalculationService;

	// ログインページ表示
	@GetMapping("/login")
	public ModelAndView showLogin(ModelAndView mv) {
		mv.setViewName("login");
		return mv;
	}

	// 一般ユーザーホーム画面表示
	@GetMapping("/user/home")
	public ModelAndView showHome(ModelAndView mv, Principal principal) {
		mv.setViewName("userhome");

		Integer userId = Integer.valueOf(principal.getName());
		// BigDecimal overtime = overtimeCalculationService.getMonthOvertime(userId);
		BigDecimal overtime = overtimeCalculationService.getMonthOvertime(userId);

		mv.addObject("overtime", overtime);
		boolean overtimeAlert = overtime.compareTo(BigDecimal.valueOf(20)) > 0;
		mv.addObject("overtimeAlert", overtimeAlert);

		Employee emp = employeeChengeRepository.findById(userId).orElseThrow((null));
		mv.addObject("emp", emp);
		mv.addObject("account", new Account());
		return mv;
	}

	// 管理者ユーザーホーム画面表示
	@GetMapping("/admin/home")
	public ModelAndView showadminHome(ModelAndView mv) {
		mv.setViewName("adminhome");

//		int overuser = overtimeCalculationService.getAllUserOverTime();
//		mv.addObject("overuser", overuser);
//		boolean overuserAlert = overuser > 0;
//		mv.addObject("overuserAlert", overuserAlert);

//		mv.addObject("account", new Account());

		List<SearchOverTimeUserDto> overUsers = overtimeCalculationService
				.seachAllUserOverTime(YearMonth.now().toString());

		mv.addObject("overuser", overUsers.size());
		mv.addObject("overuserAlert", !overUsers.isEmpty());
		return mv;
	}

	// 管理者ユーザホーム画面検索結果
	@GetMapping("/admin/overtime")
	public ModelAndView overTimeAllUser(ModelAndView mv, @RequestParam(required = false) String yearMonth) {
		mv.setViewName("adminhome");

		List<SearchOverTimeUserDto> userList = overtimeCalculationService.seachAllUserOverTime(yearMonth);
		mv.addObject("userList", userList);

		mv.addObject("yearMonth", yearMonth);

		// 20時間越えアラート判定
		long overuser = 0;

		for (SearchOverTimeUserDto user : userList) {
			BigDecimal overtime = user.getTotalOvertime();

			if (overtime != null && overtime.compareTo(BigDecimal.valueOf(20)) > 0) {
				overuser++;
			}
		}

		boolean overuserAlert = overuser > 0;
		mv.addObject("overuserAlert", overuserAlert);

		// 20時間越えユーザ件数
		List<SearchOverTimeUserDto> overUsers = overtimeCalculationService
				.seachAllUserOverTime(YearMonth.now().toString());
		mv.addObject("overuser", overUsers.size());

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
}
