package com.example.attendance.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.ChangeRequestId;
import com.example.attendance.entity.Change_request;
import com.example.attendance.entity.Department;
import com.example.attendance.entity.Employee;
import com.example.attendance.form.EmployeeForm;
import com.example.attendance.repository.DepartmentRepository;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.example.attendance.service.EmployeeChangeService;

@Controller
public class EmployeeRegistrationController {

	@Autowired
	private EmployeeChangeService employeeChangeService;

	@Autowired
	private EmployeeChengeRepository employeeChengeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@GetMapping("/employeeregistration")
	public ModelAndView employeeChange(ModelAndView mv, Principal principal) {
		mv.setViewName("employeeregistration");

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		Integer userId = Integer.parseInt(principal.getName());

		EmployeeForm emp = employeeChangeService.employeedisplay(userId);

		mv.addObject("form", new EmployeeForm());

		// 部署一覧
		List<Department> depList = departmentRepository.findAll();
		mv.addObject("depList", depList);
		

		return mv;
	}

	// 社員新規登録
	@PostMapping("/employeeregistration")
	public ModelAndView changeRegister(ModelAndView mv, Principal principal,
			@ModelAttribute("form") EmployeeForm form) {
		mv.setViewName("employeeregistration");
		
		employeeChangeService.registerEmployee(form);
		mv.setViewName("employeeregistration");
		mv.addObject("registerSuccess",true);

		
		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		return mv;
	}

}
