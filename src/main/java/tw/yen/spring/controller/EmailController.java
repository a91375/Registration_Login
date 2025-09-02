package tw.yen.spring.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.request.ResetPasswordRequest;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.service.ConfirmationTokenService;
import tw.yen.spring.service.EmailService;
import tw.yen.spring.service.UserInfoService;
import tw.yen.spring.service.UserUpdateService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {
	
	private final ConfirmationTokenService confirmationTokenService;
	private final UserInfoService userService;
	private final EmailService emailService;
	private final UserUpdateService userUpdateService;
	
	
	@GetMapping("/register/confirm/{token}")
	public String EmailVerfied(@PathVariable("token") String token) {
		int updateRows = confirmationTokenService.setConfirmedAt(token);
		
		if (updateRows > 0) {
			String uEmail = confirmationTokenService.getUEmail(token);
			userService.enableUser(uEmail); 
			 emailService.assignCompany(uEmail, token);
			
			return "驗證成功!";
		}else {
			 return "驗證失敗或連結已失效";
		}
	}
    
    // 忘記密碼
    @PostMapping("/v1/auth/forgot-password")
    public ResponseEntity<ApiResponse<?>> passwordReset(@RequestBody Map<String, String> body) {
    	String email = body.get("email");
    	return emailService.sendPasswordEmail(email);
    }      
	
	
	@PostMapping("/v1/auth/reset-password")
	public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetPasswordRequest request) {
	    return userUpdateService.passwordReset(request);
	}

}
