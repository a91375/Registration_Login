package tw.yen.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.request.AddUserRequest;
import tw.yen.spring.service.AddUserService;

@RestController
@RequestMapping("/api/user/create")
@RequiredArgsConstructor
public class AddUserController {
	private final AddUserService addUserService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> EmailVerified(@RequestBody AddUserRequest request) {	
		
		addUserService.newUser(request);
				
		Map<String, String> res = new HashMap<>();
		res.put("status", "0");
		res.put("message", "帳號新增成功，請查收驗證信。");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
}
