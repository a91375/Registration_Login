package tw.yen.spring.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.response.AllUserResponse;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.security.CustomUserDetails;
import tw.yen.spring.service.UserManageService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReadUserController {
	final UserManageService userService;
	
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<?>> getMe(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("account", userDetails.getAccount());
        data.put("email", userDetails.getUsername());
        data.put("role", userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .toList());

        return ResponseEntity.ok(ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("使用者資訊取得成功")
                .timestamp(Instant.now())
                .data(data)
                .build());
    }
	
	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<AllUserResponse>>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<ApiResponse<AllUserResponse>> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}
             
}
