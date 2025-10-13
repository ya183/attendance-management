package com.example.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Bean 間の依存関係の注入
@Configuration
//セキュリティ設定を適用
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	//リクエストごとのセキュリティ処理を制御するための設定クラス
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
        //ページごとのアクセス制御
        .authorizeHttpRequests(auth -> auth
        	// CSS,JavaScriptファイルへのアクセスを許可
            .requestMatchers("/css/**", "/js/**").permitAll()
            // ログインしていなくてもアクセス可能なURL
            .requestMatchers("/login", "/error").permitAll()
            // 上記URL以外は要ログイン
            .anyRequest().authenticated()
        )
        //　ログイン設定
        .formLogin(form -> form
        	// ログインページ
            .loginPage("/login")
            // POST先
            .loginProcessingUrl("/login")
            // ログイン成功後の遷移先
            .defaultSuccessUrl("/home", true)
            // ログイン失敗時
            .failureUrl("/login?error")
            // 未ログインユーザーでもログイン画面にアクセス許可
            .permitAll()
        )
        ;
	// セキュリティ設定をビルド
    return http.build();
    }

}
