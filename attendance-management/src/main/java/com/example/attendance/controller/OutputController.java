package com.example.attendance.controller;

import java.security.Principal;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.service.OutputService;
import com.ezample.attendance.dto.OutputSearchDto;

@Controller
public class OutputController {
	@Autowired
	private OutputService outputService;

	// データ出力
	@GetMapping("/output")
	public ModelAndView dataoutput(ModelAndView mv, Principal principal,
			@RequestParam(required = false) String yearMonth, @RequestParam(required = false) String username) {
		mv.setViewName("output");

		if (yearMonth == null) {
            yearMonth = YearMonth.now().toString();
        }
		
		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		 if (yearMonth != null && username != null) {
		List<OutputSearchDto> list = outputService.search(yearMonth, username);
		mv.addObject("listEmployee", list);
		 }
		mv.addObject("yearMonth", yearMonth);
		
		mv.addObject("username", username);

		return mv;
	}

}
