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

import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.LeaveApplicationId;
import com.example.attendance.entity.Leave_application;
import com.example.attendance.repository.ChangeRequestRepository;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.example.attendance.repository.LeaveApplicationRepository;
import com.example.attendance.service.ChangeRequestService;
import com.example.attendance.service.LoginService;

@Controller
public class LeaveApplicationController {

	// private final LoginService loginService;

	@Autowired
	private LeaveApplicationRepository leaveApplicationRepository;

	@Autowired
	private ChangeRequestService changeRequestService;
	
	@Autowired
	EmployeeChengeRepository employeeChengeRepository;

	// 休暇申請トップページ
	@GetMapping("/leaveapplication")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("leaveapplication");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}
	
	// 休暇申請ユーザ月単位で検索
		@PostMapping("/leaveapplication")
		public ModelAndView registerSearch(Principal principal,@RequestParam(value="application_date",required = false) String monthForm,
				@RequestParam(value = "userId", required = false) Integer userId, 
				@RequestParam(value = "status", required = false) Integer status,
				@RequestParam(value = "request_no", required = false) Integer requestNo,
				ModelAndView mv) {
			
			// ログイン中のユーザーNullチェック
			if (principal == null) {
				// 未ログインならログインページへリダイレクト
				mv.setViewName("redirect:/login");
				return mv;
			}

			Integer id;
			// userIdがinteger型かどうか判断
			try {
				//　userIdを数値化して取得
				id = Integer.valueOf(principal.getName());
			} catch (NumberFormatException e) {
				// Spring Securityに認証失敗として処理（/login/errorに遷移）
				throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
			}
			
			// DBからユーザー情報取得
			List<Leave_application> listLeave = leaveApplicationRepository.findByUserIdAndYearMonth(id,monthForm);
			
			//System.out.println("検索件数: " + listRequest.size());
			
			mv.addObject("listRequest", listLeave);
			mv.addObject("status", status);
			
			if (!listLeave.isEmpty()) {
			    requestNo = listLeave.get(listLeave.size() - 1).getId().getRequestNo();
			}
			
			mv.addObject("requestNo", requestNo);
			mv.addObject("monthForm", monthForm);

			// URLパラメーターでuserIdとdateの情報を渡す
			//mv.setViewName("redirect:/changerequest/info?userId=" + id + "&date=" + monthForm);
			mv.setViewName("leaveapplication");
			return mv;
		}
	

	// 休暇申請新規登録
	@GetMapping("/leavenew")
	public ModelAndView changeNew(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		
		if (principal == null) {
	        return new ModelAndView("redirect:/login");
	    }

		Integer id;
		// userIdがinteger型かどうか判断
		try {
			id = Integer.valueOf(principal.getName());
		} catch (NumberFormatException e) {
			// Spring Securityに認証失敗として処理（/login/errorに遷移）
			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
		}
		
		Employee employee = employeeChengeRepository.findByUserId(id);
		
		
		mv.setViewName("leavenew");
		mv.addObject("userName", employee.getName());

//		String loginId = principal.getName();
//		mv.addObject("loginId", loginId);

		return mv;
	}

	// 休暇申請新規登録
	@PostMapping("/leavenew/register")
	public ModelAndView changeRegister(ModelAndView mv, Principal principal,
			@RequestParam(value = "request_no", required = false) Integer requestNo,
			@RequestParam(value = "application_date", required = false) String applicationDate,
			@RequestParam(value = "change_date", required = false) String changeDate,
			@RequestParam(value = "reason", required = false) String reason) {
		mv.setViewName("leavenew");

		Integer id;
		// userIdがinteger型かどうか判断
		try {
			// userIdを数値化して取得
			id = Integer.valueOf(principal.getName());
		} catch (NumberFormatException e) {
			// Spring Securityに認証失敗として処理（/login/errorに遷移）
			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
		}

		LocalDate dateapp = LocalDate.parse(applicationDate);
		LocalDate datecha = LocalDate.parse(changeDate);

		Leave_application leaveApplication = new Leave_application();

		leaveApplication.setApplication_date(dateapp);
		leaveApplication.setChange_date(datecha);
		leaveApplication.setReason(reason);
		
		LeaveApplicationId leaveApplicationId;
		if (requestNo == null) {
			// request_no を自動採番
			int max = leaveApplicationRepository.getMaxRequestNo(id); // MAX+1 で採番
			int next = max + 1;
			leaveApplicationId = new LeaveApplicationId(id, next);
			// changeRequest.setId(id);

		} else {
			// 更新処理
			leaveApplicationId = new LeaveApplicationId(id, requestNo);
		}
		leaveApplication.setId(leaveApplicationId);

		leaveApplicationRepository.save(leaveApplication);
		
		mv.addObject("registerSuccess",true);

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}
	
	//　休暇申請修正
		@GetMapping("/leavecorrect")
		public ModelAndView changeCorrect(ModelAndView mv, Principal principal,
				@RequestParam("requestNo") Integer requestNo) {
			mv.setViewName("leavecorrect");

			String loginId = principal.getName();
			mv.addObject("loginId", loginId);
			
			Integer id;
			// userIdがinteger型かどうか判断
			try {
				// userIdを数値化して取得
				id = Integer.valueOf(principal.getName());
			} catch (NumberFormatException e) {
				// Spring Securityに認証失敗として処理（/login/errorに遷移）
				throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
			}
			
			LeaveApplicationId leaveApplicationId = new LeaveApplicationId(id,requestNo);
			
			requestNo = leaveApplicationId.getRequestNo();
			
			Leave_application leaveApplication = leaveApplicationRepository.findById(leaveApplicationId).orElseThrow();
			mv.addObject("leaveApplication", leaveApplication);
			
			mv.addObject("requestNo", requestNo);
			 System.out.println(requestNo);
			

			return mv;
		}
		
		@PostMapping("/leavecorrect")
		public ModelAndView changeCorrectRegist(ModelAndView mv, Principal principal,
				@RequestParam(value = "request_no", required = false) Integer requestNo,
				@RequestParam(value = "application_date", required = false) String applicationDate,
				@RequestParam(value = "change_date", required = false) String changeDate,
				@RequestParam(value = "reason", required = false) String reason) {
			mv.setViewName("leavecorrect");
			
			Integer id;
			// userIdがinteger型かどうか判断
			try {
				// userIdを数値化して取得
				id = Integer.valueOf(principal.getName());
			} catch (NumberFormatException e) {
				// Spring Securityに認証失敗として処理（/login/errorに遷移）
				throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
			}
			
//			LocalDate dateapp = LocalDate.parse(applicationDate);
//			LocalDate datecha = LocalDate.parse(changeDate);
//
//			Leave_application leaveApplication = new Leave_application();
//
//			leaveApplication.setApplication_date(dateapp);
//			leaveApplication.setChange_date(datecha);
//			leaveApplication.setReason(reason);
//			
//			LeaveApplicationId leaveApplicationId;
			
			LeaveApplicationId leaveApplicationId = new LeaveApplicationId(id, requestNo);
		    
			Leave_application entity = leaveApplicationRepository.findById(leaveApplicationId).orElseThrow();;
			
			// ---- 日付 ----
		    if (applicationDate != null && !applicationDate.isBlank()) {
		        entity.setApplication_date(LocalDate.parse(applicationDate));
		    }

		    if (changeDate != null && !changeDate.isBlank()) {
		        entity.setChange_date(LocalDate.parse(changeDate));
		    }
		    // ---- 理由 ----
		    if (reason != null && !reason.isBlank()) {
		        entity.setReason(reason);
		    }
			
		    boolean registerSuccess;
			try {
			leaveApplicationRepository.save(entity);
			registerSuccess = true;
			} catch (Exception e) {
				registerSuccess = false;
			}
			mv.addObject("registerSuccess", registerSuccess);
			mv.addObject("leaveApplication", entity);

			String loginId = principal.getName();
			mv.addObject("loginId", loginId);

			return mv;
		}

}
