package tw.yen.spring.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.request.RegistrationRequest;
import tw.yen.spring.service.CaptchaService;
import tw.yen.spring.service.RegistrationService;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

	private final RegistrationService registrationService;
	private final CaptchaService captchaService;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> EmailVerified(@RequestBody RegistrationRequest request, HttpServletRequest httpReq) {	
		// 先機器人驗證
		 boolean ok = captchaService.verify(request.getCaptchaToken(), httpReq.getRemoteAddr());
	        if (!ok) {
	            return ResponseEntity.badRequest()
	                    .body(Map.of("message", "機器人驗證未通過"));
	        }
	  
		
		registrationService.register(request);
		
		return ResponseEntity.ok(Map.of("message", "註冊成功，請查收驗證信"));
	}
	
}
