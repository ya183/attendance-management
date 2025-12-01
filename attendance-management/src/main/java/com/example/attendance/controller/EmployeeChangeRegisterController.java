package com.example.attendance.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Department;
import com.example.attendance.entity.Employee;
import com.example.attendance.form.EmployeeForm;
import com.example.attendance.repository.DepartmentRepository;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.example.attendance.service.EmployeeChangeService;
import com.ezample.attendance.dto.EmployeeChangeDto;

@Controller
public class EmployeeChangeRegisterController {

	@Autowired
	private EmployeeChangeService employeeChangeService;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	

	@GetMapping("/employeechangeregister")
	public ModelAndView employeeChange(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId) {
		mv.setViewName("employeechangeregister");

		EmployeeForm emp = employeeChangeService.employeedisplay(userId);

		mv.addObject("form", emp);
		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		// 部署一覧
		List<Department> depList = departmentRepository.findAll();
		mv.addObject("depList", depList);

		return mv;
	}

	@PostMapping("/employeechangeregister")
	public ModelAndView employeeChangeregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId, EmployeeForm form) {
		mv.setViewName("employeechangeregister");

		// form.setUserId(userId);
		employeeChangeService.updateEmp(form);

		mv.addObject("form", form);

		// 部署一覧
		List<Department> depList = departmentRepository.findAll();
		mv.addObject("depList", depList);

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);
		

		return mv;
	}

}
