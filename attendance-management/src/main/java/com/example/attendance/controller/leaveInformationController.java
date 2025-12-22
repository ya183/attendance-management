package com.example.attendance.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;
import com.example.attendance.entity.LeaveApplicationId;
import com.example.attendance.entity.Leave_application;
import com.example.attendance.repository.ChangeInformationRepository;
import com.example.attendance.repository.LeaveInformationRepository;
import com.example.attendance.service.LeaveService;

@Controller
public class leaveInformationController {
	
	@Autowired
	private LeaveInformationRepository leaveInformationRepository;
	@Autowired
	private LeaveService leaveService;

	// 休暇申請トップページ
	@GetMapping("/leaveinformation")
	public ModelAndView leaveinfo(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("leaveinformation");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

	// 休暇申請月単位で検索
	@PostMapping("/leaveinformation")
	public ModelAndView registerSearch(Principal principal,
			@RequestParam(value = "application_date", required = false) String monthForm,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "request_no", required = false) Integer requestNo, ModelAndView mv) {

		// DBからユーザー情報取得
		List<Leave_application> listRequest = leaveInformationRepository.findByYearMonth(monthForm);

		System.out.println("検索件数: " + listRequest.size());
		
		if (!listRequest.isEmpty()) {
			userId = listRequest.get(listRequest.size() - 1).getId().getRequestNo();
	}
		
		

		mv.addObject("listRequest", listRequest);
		mv.addObject("monthForm", monthForm);
		mv.addObject("userId", userId);

//		if (!listRequest.isEmpty()) {
//			requestNo = listRequest.get(listRequest.size() - 1).getId().getRequestNo();
//		}
//
//		mv.addObject("requestNo", requestNo);

		// URLパラメーターでuserIdとdateの情報を渡す
		// mv.setViewName("redirect:/changerequest/info?userId=" + id + "&date=" +
		// monthForm);
		mv.setViewName("leaveinformation");
		return mv;
	}
	
	// 休暇申請詳細
	@GetMapping("/leavedetail")
	public ModelAndView changeDetail(ModelAndView mv, Principal principal,
			@RequestParam("requestNo") Integer requestNo,
			@RequestParam("userId") Integer userId) {
		mv.setViewName("leavedetail");

		LeaveApplicationId leaveId = new LeaveApplicationId(userId,requestNo);
		Leave_application datail = leaveInformationRepository.findById(leaveId).orElse(null);
		
		mv.addObject("loginId", leaveId.getUserId());
		mv.addObject("requestNo", leaveId.getRequestNo());
		mv.addObject("applicationDate", datail.getApplication_date());
		mv.addObject("changeDate", datail.getChange_date());
		mv.addObject("reason", datail.getReason());
		 System.out.println(requestNo);

		return mv;
	}
	
	// 休暇申請承認
	@PostMapping("/leavedetail")
	public ModelAndView changeapprove(ModelAndView mv, Principal principal,
			@RequestParam("requestNo") Integer requestNo,
			@RequestParam("userId") Integer userId) {
		

		boolean leaveSave = leaveService.approve (userId,requestNo);
		
		
		if(leaveSave == true) {
			mv.addObject("registerSuccess",true);
		}else {
			mv.addObject("registerSuccess",false);
		}

		mv.setViewName("leavedetail");
		return mv;
	}
	

}
