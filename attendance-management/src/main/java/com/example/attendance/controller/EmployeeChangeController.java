package com.example.attendance.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.form.EmployeeForm;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.example.attendance.service.EmployeeChangeService;
import com.ezample.attendance.dto.EmployeeDto;

@Controller
public class EmployeeChangeController {
	
	@Autowired
	private EmployeeChangeService employeeChangeService;
	
	// 社員検索/変更/削除トップページ
		@GetMapping("/employeechange")
		public ModelAndView employeeChange(ModelAndView mv, Principal principal) {
			mv.setViewName("employeechange");

			mv.addObject("form", new EmployeeForm());
			String loginId = principal.getName();
			mv.addObject("loginId", loginId);

			return mv;
		}
		
	// 社員検索
		@PostMapping("/employeechange")
		public ModelAndView employeeSearch(ModelAndView mv, Principal principal,
				@RequestParam(value = "userId", required = false) Integer userId,
				@RequestParam(value = "name", required = false) String name,
				@RequestParam(value = "department_id", required = false) String department_id,
				EmployeeForm form
				) {
			mv.setViewName("employeechange");
			
			List<EmployeeDto> empDto = employeeChangeService.search(form);
			mv.addObject("listEmployee", empDto);
			

			String loginId = principal.getName();
			mv.addObject("loginId", loginId);

			return mv;
		}

}
