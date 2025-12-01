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

@Controller
public class EmployeeDeleteController {
	
	@Autowired
	private EmployeeChangeService employeeChangeService;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private EmployeeChengeRepository employeeChengeRepository;
	
	@GetMapping("/employeedelete")
	public ModelAndView employeeChange(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId) {
		mv.setViewName("employeedelete");

		EmployeeForm emp = employeeChangeService.employeedisplay(userId);

		mv.addObject("form", emp);
		String loginId = principal.getName();
		mv.addObject("loginId", loginId);

		// 部署一覧
		List<Department> depList = departmentRepository.findAll();
		mv.addObject("depList", depList);

		return mv;
	}
	
	@PostMapping("/employeedelete")
	public ModelAndView employeeChangeregister(ModelAndView mv, Principal principal,
			@RequestParam(value = "userId", required = false) Integer userId, EmployeeForm form) {
		

		 Employee employee = employeeChengeRepository.findByUserId(userId);

		    if (employee == null) {
		        throw new IllegalArgumentException("削除するユーザが見つかりません。");
		    }
		
		// form.setUserId(userId);
		employeeChengeRepository.deleteById(userId);

		//mv.addObject("form", form);

		String loginId = principal.getName();
		mv.addObject("loginId", loginId);
		
		mv.setViewName("redirect:/employeechange");
		
		return mv;
	}

}
