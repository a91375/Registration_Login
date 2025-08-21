package tw.yen.spring.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.entity.ConfirmationTokens;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.payload.request.NewUserRequest;
import tw.yen.spring.security.enums.Role;

@Service
@RequiredArgsConstructor
public class NewUserService {
	private final PasswordEncoder passwordEncoder;
	private final UserInfoService userService;
	private final EmailService emailService;
	private final ConfirmationTokenService tokenService;
	
	
	@Transactional
	public String newUser(@RequestBody NewUserRequest request) {
		if (userService.userExists(request.getUEmail())) {
			throw new IllegalStateException("此信箱已使用");
		}
		
		UserInfo user = new UserInfo();
		user.setuEmail(request.getUEmail());
		user.setuAccount(request.getUAccount() );
		user.setuPassword(passwordEncoder.encode("0000"));  // 預設密碼
		user.setStatus(0);  // 信箱待驗證
		String roleStr = request.getRole();
		Role role = Role.valueOf(roleStr.toUpperCase());
		user.setRole(role);
		UserInfo savedUser = userService.save(user);
		
		// 發送驗證信
		String token = emailService.sendVerificationEmail(user.getuEmail(),request.getCId());
				
		// 記錄Token
		ConfirmationTokens cToken = new ConfirmationTokens();
		cToken.setToken(token);
		cToken.setCreatedAt(LocalDateTime.now());
		cToken.setExpiresAt(LocalDateTime.now().plusMinutes(60));  // 設定60分鐘後token過期
		cToken.setUserId(savedUser.getId());
		tokenService.saveToken(cToken);
		        
		return token;
	}
}
