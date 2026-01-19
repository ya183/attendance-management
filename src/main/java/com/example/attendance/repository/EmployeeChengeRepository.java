package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.attendance.entity.Employee;

@Repository
public interface EmployeeChengeRepository extends JpaRepository<Employee,Integer>{
	
		List<Employee> findByNameLikeOrderByUserIdAsc(@Param("name") String name);
		
		Employee findByUserId(@Param("userId") Integer userId);
		
		//Employee findByLoginId(@Param("name") String userId);
		
		//void deleteById(@Param("userId") Integer userId);
}
