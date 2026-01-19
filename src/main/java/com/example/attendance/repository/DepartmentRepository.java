package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Department;
import com.example.attendance.entity.Employee;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Short>{

}
