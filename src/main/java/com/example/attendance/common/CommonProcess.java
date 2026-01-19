package com.example.attendance.common;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeChengeRepository;

@ControllerAdvice
public class CommonProcess {
	@Autowired
	private EmployeeChengeRepository employeeRepository;
	
	@ModelAttribute
	public void userName(ModelAndView mv, Principal principal) {
		
		//ログインユーザが見つからない場合、これ以上このメソッドの中身を実行しないで抜ける
		if (principal == null) {
            return;
        }
		
		Integer loginId = Integer.valueOf(principal.getName());
		
		Employee emp = employeeRepository.findById(loginId).orElse(null);
		
		if(emp != null) {
			mv.addObject("userName", emp.getName());
		}
		
	}

}
