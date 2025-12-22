/**
 * 「変更申請」コントローラ
 * 変更申請を管理するクラス
 *
 */
package com.example.attendance.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import com.example.attendance.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Account;
import com.example.attendance.entity.AttendanceInformationId;
import com.example.attendance.entity.Attendance_information;
import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;
import com.example.attendance.repository.ChangeRequestRepository;
import com.example.attendance.service.ChangeRequestService;

@Controller
public class ChangeRequestController {

    private final LoginService loginService;

	@Autowired
	private ChangeRequestRepository changeRequestRepository;

	@Autowired
    private ChangeRequestService changeRequestService;

    ChangeRequestController(LoginService loginService) {
        this.loginService = loginService;
    }
	
	// 変更申請トップページ
	@GetMapping("/changerequest")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("changerequest");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}
	
	// 変更申請ユーザ月単位で検索
	@PostMapping("/changerequest")
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
		
//		if (monthForm == null || monthForm.isEmpty()) {
//		    monthForm = YearMonth.now().toString(); 
//		}
		
		// DBからユーザー情報取得
		List<Change_request> listRequest = changeRequestRepository.findByUserIdAndYearMonth(id,monthForm);
		
		System.out.println("検索件数: " + listRequest.size());
		
		mv.addObject("listRequest", listRequest);
		mv.addObject("status", status);
		
		if (!listRequest.isEmpty()) {
		    requestNo = listRequest.get(listRequest.size() - 1).getId().getRequestNo();
		}
		
		mv.addObject("requestNo", requestNo);
		mv.addObject("monthForm", monthForm);

		// URLパラメーターでuserIdとdateの情報を渡す
		//mv.setViewName("redirect:/changerequest/info?userId=" + id + "&date=" + monthForm);
		mv.setViewName("changerequest");
		return mv;
	}
	

	// 変更申請新規登録
	@GetMapping("/changenew")
	public ModelAndView changeNew(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("changenew");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

	// 変更申請新規登録
	@PostMapping("/changenew/register")
	public ModelAndView changeRegister(ModelAndView mv, Principal principal,
			@RequestParam(value = "request_no", required = false) Integer requestNo,
			@RequestParam(value = "application_date", required = false) String applicationDate,
			@RequestParam(value = "change_date", required = false) String changeDate,
			@RequestParam(value = "revised_clock_in", required = false) String revisedClockIn,
			@RequestParam(value = "revised_clock_out", required = false) String revisedClockOut,
			@RequestParam(value = "reason", required = false) String reason) {
		mv.setViewName("changenew");

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
		LocalTime timein = LocalTime.parse(revisedClockIn);
		LocalTime timeout = LocalTime.parse(revisedClockOut);
		
		//ChangeRequestId changeRequestId = new ChangeRequestId(id, requestNo);
		Change_request changeRequest = new Change_request();
		//changeRequest.setId(changeRequestId);
		changeRequest.setApplication_date(dateapp);
		changeRequest.setChange_date(datecha);
		changeRequest.setRevised_clock_in(timein);
		changeRequest.setRevised_clock_out(timeout);
		changeRequest.setReason(reason);
		//changeRequestRepository.save(changeRequest);
		
		ChangeRequestId changeRequestId;
		if (requestNo == null) {
	        // request_no を自動採番
	        int max = changeRequestRepository.getMaxRequestNo(id); // MAX+1 で採番
	        int next = max + 1;
	        changeRequestId = new ChangeRequestId(id, next);
	        //changeRequest.setId(id);

	    } else {
	        // 更新処理
	        changeRequestId = new ChangeRequestId(id, requestNo);
	    }
		changeRequest.setId(changeRequestId);
		
		 changeRequestRepository.save(changeRequest);
		

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}
	
	//　変更申請修正
	@GetMapping("/changecorrect")
	public ModelAndView changeCorrect(ModelAndView mv, Principal principal,
			@RequestParam("requestNo") Integer requestNo) {
		mv.setViewName("changecorrect");

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
		
		ChangeRequestId changeId = new ChangeRequestId(id,requestNo);
		
		requestNo = changeId.getRequestNo();
		
		mv.addObject("requestNo", requestNo);
		 System.out.println(requestNo);
		

		return mv;
	}
	
	@PostMapping("/changecorrect")
	public ModelAndView changeCorrectRegist(ModelAndView mv, Principal principal,
			@RequestParam(value = "request_no", required = false) Integer requestNo,
			@RequestParam(value = "application_date", required = false) String applicationDate,
			@RequestParam(value = "change_date", required = false) String changeDate,
			@RequestParam(value = "revised_clock_in", required = false) String revisedClockIn,
			@RequestParam(value = "revised_clock_out", required = false) String revisedClockOut,
			@RequestParam(value = "reason", required = false) String reason) {
		mv.setViewName("changecorrect");
		
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
		LocalTime timein = LocalTime.parse(revisedClockIn);
		LocalTime timeout = LocalTime.parse(revisedClockOut);
		
		//ChangeRequestId changeRequestId = new ChangeRequestId(id, requestNo);
		Change_request changeRequest = new Change_request();
		//changeRequest.setId(changeRequestId);
		changeRequest.setApplication_date(dateapp);
		changeRequest.setChange_date(datecha);
		changeRequest.setRevised_clock_in(timein);
		changeRequest.setRevised_clock_out(timeout);
		changeRequest.setReason(reason);
		//changeRequestRepository.save(changeRequest);
		
		ChangeRequestId changeRequestId;
		
	        // 更新処理
	        changeRequestId = new ChangeRequestId(id, requestNo);
	    
		changeRequest.setId(changeRequestId);
		
		 changeRequestRepository.save(changeRequest);

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

}
