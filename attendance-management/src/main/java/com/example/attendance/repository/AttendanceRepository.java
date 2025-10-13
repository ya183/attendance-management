package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Account;

@Repository
public interface AttendanceRepository extends JpaRepository<Account,Integer> {
	// ログインID（user_id）で検索するメソッド
	Account findByUserId(Integer userId);
}
