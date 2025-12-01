package com.example.attendance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.Department;
import com.example.attendance.entity.Employee;
import com.example.attendance.form.EmployeeForm;
import com.example.attendance.repository.DepartmentRepository;
import com.example.attendance.repository.EmployeeChengeRepository;
import com.ezample.attendance.dto.EmployeeChangeDto;
import com.ezample.attendance.dto.EmployeeDto;

@Service
public class EmployeeChangeService {

	@Autowired
	private EmployeeChengeRepository employeeChengeRepository;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	public List<EmployeeDto> search(EmployeeForm form) {

		// 部分検索
		String nameword = "%" + form.getName() + "%";

		List<Employee> employeeList = employeeChengeRepository.findByNameLike(nameword);

		List<EmployeeDto> dtoList = new ArrayList<>();

		for(Employee e : employeeList) {
			EmployeeDto dto = new EmployeeDto();
			dto.setUserId(e.getUserId());
			dto.setName(e.getName());
			dto.setDepartment_name(e.getDepartment().getDepartment_name());
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	public EmployeeForm employeedisplay(Integer userId) {

		Employee empchange = employeeChengeRepository.findByUserId(userId);
		
		EmployeeForm empForm = new EmployeeForm();
		empForm.setUserId(empchange.getUserId());
		empForm.setName(empchange.getName());
		empForm.setDepartmentId(empchange.getDepartment().getDepartmentId());
		empForm.setDepartmentName(empchange.getDepartment().getDepartment_name());
		
		empForm.setGender(empchange.getGender());
		empForm.setAge(empchange.getAge());
		empForm.setEmail(empchange.getEmail());
		empForm.setAddress(empchange.getAddress());
		

		return empForm;
	}
	
	// 更新処理
	public void updateEmp(EmployeeForm form) {
		
		// DBから更新するレコードを取得
		Employee employee = employeeChengeRepository.findByUserId(form.getUserId());
		
		if (employee == null) {
	        throw new IllegalArgumentException("該当の社員が見つかりません");
	    }
		
		employee.setUserId(form.getUserId());
		employee.setName(form.getName());
		
		Department dep = departmentRepository.findById(form.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("部署が存在しません"));
		employee.setDepartment(dep);
		
		employee.setGender(form.getGender());
		employee.setAge(form.getAge());
		employee.setEmail(form.getEmail());
		employee.setAddress(form.getAddress());
		employee.setUpdate_date(LocalDate.now());
		employee.setUpdate_by(form.getName());
		
		
		employeeChengeRepository.save(employee);
		
	}
	
}
