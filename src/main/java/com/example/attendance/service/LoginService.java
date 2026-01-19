package com.example.attendance.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance.entity.Account;
import com.example.attendance.repository.AttendanceRepository;


@Service
public class LoginService implements UserDetailsService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// パスワードをハッシュ化
	public void registerUser(Account account) {
		String pass = passwordEncoder.encode(account.getPassword());
		account.setPassword(pass);
		attendanceRepository.save(account);
	}
	
	// Spring Securityがログイン時にユーザー情報を取得するメソッド
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		// Spring SecurityのloadUserByUsername(String userId) は、引数が文字列のためIntegerに変換
		Integer id;
		// userIdがinteger型かどうか判断
		try {
			id = Integer.valueOf(userId);
		} catch (NumberFormatException e) {
			// Spring Securityに認証失敗として処理（/login/errorに遷移）
			throw new UsernameNotFoundException("入力されたユーザーIDの形式が違います：" + userId);
		}

		// DBからユーザー情報取得
		Account account = attendanceRepository.findByUserId(id);
		if (account == null) {
			throw new UsernameNotFoundException("入力されたユーザーIDがありません：" + userId);
		}

		// 権限をセット
		String role;
		// 管理者
		if (account.getAuthority() == 1) {
			role = "ROLE_ADMIN";
			// 一般
		} else if (account.getAuthority() == 2) {
			role = "ROLE_USER";
			// 管理者、一般以外
		} else {
			role = "ROLE_UNKNOWN";
			throw new UsernameNotFoundException("設定された権限の形式が違います: " + account.getAuthority());
		}

		// ユーザーの権限
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

		// Spring SecurityにユーザーID,パスワード、権限を返す
		return new org.springframework.security.core.userdetails.User(
				// ユーザーID
				account.getUserId().toString(),
				// パスワード
				account.getPassword(),
				// 権限
				List.of(authority));
	}
}