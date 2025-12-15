package com.example.attendance.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Account;
import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.form.AttendanceFormList;
import com.example.attendance.repository.AttendanceInformationRepository;
import com.example.attendance.repository.AttendanceRepository;
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

	private final AttendanceInformationRepository attendanceInformationRepository;

	private final AttendInfoService attendInfoService;

	private final OvertimeCalculationService overtimeCalculationService;

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
	public ModelAndView showHome(ModelAndView mv, Principal principal) {
		mv.setViewName("userhome");
		
		Integer userId = Integer.valueOf(principal.getName());
		BigDecimal overtime = overtimeCalculationService.getMonthOvertime(userId);
		mv.addObject("overtime", overtime);
		boolean overtimeAlert = overtime.compareTo(BigDecimal.valueOf(20)) > 0;
        mv.addObject("overtimeAlert", overtimeAlert);
		
		mv.addObject("account", new Account());
		return mv;
	}

	// 管理者ユーザーホーム画面表示
	@GetMapping("/admin/home")
	public ModelAndView showadminHome(ModelAndView mv) {
		mv.setViewName("adminhome");
		
		int overuser = overtimeCalculationService.getAllUserOverTime();
		mv.addObject("overuser",  overuser);
		boolean overuserAlert = overuser > 0;
		mv.addObject("overuserAlert",  overuserAlert);
		
		mv.addObject("account", new Account());
		return mv;
	}
	
	//管理者ユーザホーム画面検索結果
	@GetMapping("/admin/overtime")
	public ModelAndView overTimeAllUser(ModelAndView mv,@RequestParam(required = false) String yearMonth) {
		mv.setViewName("adminhome");
		
		List<SearchOverTimeUserDto> userList = overtimeCalculationService.seachAllUserOverTime(yearMonth);
		mv.addObject("userList", userList);
		
		mv.addObject("yearMonth", yearMonth);
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
	public ModelAndView attendanceInformation(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
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

		mv.addObject("account", null);
		mv.addObject("yearMonth", null);
		mv.addObject("attendanceinformation", null);

		return mv;
	}

	// 勤怠情報表示ボタン押下後（フォーム送信後の処理）
	@PostMapping("/attendanceinformation/")
	public ModelAndView attendanceInfoSearch(Principal principal, @RequestParam("inputDate") String yearMonth,
			@RequestParam(value = "userId", required = false) Integer userId, ModelAndView mv) {

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

		// ログイン中のユーザの月単位の勤怠情報を取得
		List<Attendance_information> attendanceList = attendanceInformationRepository
				.findByUserIdAndYearMonth(account.getUserId(), yearMonth);

		mv.addObject("account", account);
		mv.addObject("yearMonth", yearMonth);
		mv.addObject("attendanceinformation", attendanceList);

		// URLパラメーターでuserIdとdateの情報を渡す
		mv.setViewName("redirect:/attendanceinformation/info?userId=" + id + "&date=" + yearMonth);
		return mv;
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

		// nullの場合は空
		if (attendanceList == null) {
			attendanceList = new ArrayList<>();
		}

		// HTMLにデータを渡す
		mv.addObject("attendanceinformation", attendanceList);
		mv.addObject("yearMonth", yearMonth);

		return mv;
	}

	// １ヶ月分の登録
	@GetMapping("/attendanceinformation/new")
	public ModelAndView attendnew(@RequestParam("date") String yearMonth, ModelAndView mv) {

		// HTML名をセット
		mv.setViewName("attendnew");

		// 登録データを格納するリストを作成
		AttendanceFormList listform = new AttendanceFormList();

		// 入力フォーム
		List<AttendanceForm> attendForm = new ArrayList<>();

		// YearMonth型に変換
		YearMonth yearMonthstr = YearMonth.parse(yearMonth);

		// 月の日数を取得
		int dayLength = yearMonthstr.lengthOfMonth();

		// 日数分作成
		for (int i = 1; i <= dayLength; i++) {
			AttendanceForm form = new AttendanceForm();
			form.setDayForm(LocalDate.of(yearMonthstr.getYear(), yearMonthstr.getMonth(), i));
			attendForm.add(form);
		}

		// 入力された１ヶ月分のデータをセット
		listform.setAttendForm(attendForm);

		// 年月をHTMLに渡す
		mv.addObject("yearMonth", yearMonth);
		// 入力された１ヶ月分のデータをHTMLに渡す
		mv.addObject("attendFormList", listform);

		return mv;
	}

	// １ヶ月分の登録を受け取りDB更新
	@PostMapping("/attendanceinformation/new")
	public ModelAndView attendNewPost(@ModelAttribute("attendFormList") AttendanceFormList listform, ModelAndView mv) {

		// HTML名をセット
		mv.setViewName("attendnew");

		// 1ヶ月分のデータを入手
		List<AttendanceForm> form = listform.getAttendForm();

		// DB更新
		attendInfoService.SaveAllList(form);

		// 入力された１ヶ月分のデータをHTMLに渡す
		mv.addObject("attendFormList", listform);

		return mv;

	}

}
