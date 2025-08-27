package tw.yen.spring.service;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.exception.NotFoundException;
import tw.yen.spring.exception.PasswordUpdateException;
import tw.yen.spring.payload.request.UpdatePasswordRequest;
import tw.yen.spring.payload.request.UpdateUserByAdminRequest;
import tw.yen.spring.payload.request.UpdateUserRequest;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.security.CustomUserDetails;
import tw.yen.spring.security.enums.Role;

@Service
@RequiredArgsConstructor
public class UserUpdateService {
	private final UserInfoService userService;
	private final UserInfoRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	@Transactional
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> userUpdate(UpdateUserRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (CustomUserDetails) authentication.getPrincipal();
		String uEmail = user.getUsername();
		
		UserInfo newUser = userRepository.findByUEmail(uEmail)
                .orElseThrow(() -> new NotFoundException("找不到目標使用者：" + uEmail));
		newUser.setuAccount(request.getUAccount() );
		newUser.setStatus(request.getStatus());
		
		UserInfo savedUser = userService.save(newUser);
		        
		return ResponseEntity.ok(ApiResponse.success("資料更新成功！"));
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> userUpdateByAdmin(UpdateUserByAdminRequest request) {
		
		UserInfo newUser = userRepository.findByUEmail(request.getTEmail())
                .orElseThrow(() -> new NotFoundException("找不到目標使用者：" + request.getTEmail()));
		newUser.setuAccount(request.getUAccount() );
		String roleStr = request.getRole();
		Role role = Role.valueOf(roleStr.toUpperCase());
		newUser.setRole(role);
		newUser.setStatus(request.getStatus());
		
		UserInfo savedUser = userService.save(newUser);
		        
		return ResponseEntity.ok(ApiResponse.success("資料更新成功！"));
	}
	
	@Transactional
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<ApiResponse> passwordUpdate(UpdatePasswordRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails user = (CustomUserDetails) authentication.getPrincipal();
		String uEmail = user.getUsername();
		UserInfo newUser = userRepository.findByUEmail(uEmail)
	            .orElseThrow(() -> new NotFoundException("找不到使用者"));
		
		String oldPassword = request.getOldPassword();
		String checkPassword = request.getCheckPassword();
		String newPassword = request.getPassword();
		
		if (!passwordEncoder.matches(oldPassword, newUser.getuPassword())) {
			throw new PasswordUpdateException("舊密碼錯誤");
		}
		if (!newPassword.equals(oldPassword)) {
			throw new PasswordUpdateException("新密碼不可與舊密碼重複");
		}
		if(!checkPassword.equals(newPassword)) {
			throw new PasswordUpdateException("兩次密碼不一致");
		}
		newUser.setuPassword(passwordEncoder.encode(newPassword));
		
		UserInfo savedUser = userService.save(newUser);
		        
		return ResponseEntity.ok(ApiResponse.success("密碼更新成功！"));
	}
	
}
