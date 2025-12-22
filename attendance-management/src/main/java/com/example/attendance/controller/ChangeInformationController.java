/**
 * 管理者側「変更申請一覧」コントローラ
 * 変更申請を管理するクラス
 *
 */
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
import com.example.attendance.repository.ChangeInformationRepository;

@Controller
public class ChangeInformationController {

	@Autowired
	private ChangeInformationRepository changeInformationRepository;

	// 変更申請トップページ
	@GetMapping("/changeinformation")
	public ModelAndView register(ModelAndView mv, Principal principal,
			@RequestParam(value = "date", required = false) String date) {
		mv.setViewName("changeinformation");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

	// 変更申請月単位で検索
	@PostMapping("/changeinformation")
	public ModelAndView registerSearch(Principal principal,
			@RequestParam(value = "application_date", required = false) String monthForm,
			@RequestParam(value = "userId", required = false) Integer userId,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "request_no", required = false) Integer requestNo, ModelAndView mv) {

//		// ログイン中のユーザーNullチェック
//		if (principal == null) {
//			// 未ログインならログインページへリダイレクト
//			mv.setViewName("redirect:/login");
//			return mv;
//		}
//
//		Integer id;
//		// userIdがinteger型かどうか判断
//		try {
//			// userIdを数値化して取得
//			id = Integer.valueOf(principal.getName());
//		} catch (NumberFormatException e) {
//			// Spring Securityに認証失敗として処理（/login/errorに遷移）
//			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + principal.getName());
//		}

//			if (monthForm == null || monthForm.isEmpty()) {
//			    monthForm = YearMonth.now().toString(); 
//			}

		// DBからユーザー情報取得
		List<Change_request> listRequest = changeInformationRepository.findByYearMonth(monthForm);

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
		mv.setViewName("changeinformation");
		return mv;
	}

	// 変更申請詳細
	@GetMapping("/changedetail")
	public ModelAndView changeDetail(ModelAndView mv, Principal principal, @RequestParam("requestNo") Integer requestNo,
			@RequestParam("userId") Integer userId) {
		mv.setViewName("changedetail");

		ChangeRequestId changeId = new ChangeRequestId(userId, requestNo);
		Change_request datail = changeInformationRepository.findById(changeId).orElse(null);

		mv.addObject("loginId", changeId.getUserId());
		mv.addObject("requestNo", changeId.getRequestNo());

		mv.addObject("applicationDate", datail.getApplication_date());
		mv.addObject("changeDate", datail.getChange_date());
		mv.addObject("revisedClockIn", datail.getRevised_clock_in());
		mv.addObject("revisedClockOut", datail.getRevised_clock_out());
		mv.addObject("reason", datail.getReason());
		System.out.println(requestNo);

		return mv;
	}

	// 変更申請承認
	@PostMapping("/changedetail")
	public ModelAndView changeapprove(ModelAndView mv, Principal principal,
			@RequestParam("requestNo") Integer requestNo, @RequestParam("userId") Integer userId) {

		ChangeRequestId changeId = new ChangeRequestId(userId, requestNo);
		Change_request datail = changeInformationRepository.findById(changeId).orElse(null);

		datail.setStatus((short) 2);
		Change_request changeSave = changeInformationRepository.save(datail);

		if (changeSave != null) {
			mv.addObject("registerSuccess", true);
		} else {
			mv.addObject("registerSuccess", false);
		}

		mv.setViewName("changedetail");
		return mv;
	}
}
