package tw.yen.spring.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.request.UpdatePasswordRequest;
import tw.yen.spring.payload.request.UpdateUserByAdminRequest;
import tw.yen.spring.payload.request.UpdateUserRequest;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.service.UserUpdateService;

@RestController
@RequestMapping("/api/user/update")
@RequiredArgsConstructor
public class UserUpdateController {
	private final UserUpdateService updateService;
	
	@PostMapping("/me")
	public ResponseEntity<ApiResponse> updateOwnProfile(@RequestBody UpdateUserRequest request) {	
		return updateService.userUpdate(request);
	}
	
	@PostMapping("/byAdmin")
	public ResponseEntity<ApiResponse> updateUserByAdmin(@RequestBody UpdateUserByAdminRequest request) {	
		return updateService.userUpdateByAdmin(request);
	}
	
	@PostMapping("/me/password")
	public ResponseEntity<ApiResponse> updatePassword(@RequestBody UpdatePasswordRequest request) {	
		return updateService.passwordUpdate(request);
	}
	
}
