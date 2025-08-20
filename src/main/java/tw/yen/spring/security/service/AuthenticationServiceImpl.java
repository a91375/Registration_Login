package tw.yen.spring.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.payload.request.AuthenticationRequest;
import tw.yen.spring.payload.request.RegistrationRequest;
import tw.yen.spring.payload.response.AuthenticationResponse;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.security.CustomUserDetails;
import tw.yen.spring.security.enums.TokenType;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
	 	private final PasswordEncoder passwordEncoder;
	    private final JwtService jwtService;
	    private final UserInfoRepository userRepository;
	    private final AuthenticationManager authenticationManager;
	    private final RefreshTokenService refreshTokenService;
	    
	    // 產生 JWT access token & Refresh Token
	    @Override
	    public AuthenticationResponse authenticate(AuthenticationRequest request) {
	    	// Spring Security 驗證帳號密碼
	    	authenticationManager.authenticate(
	        		new UsernamePasswordAuthenticationToken(
	        				request.getEmail(),
	        				request.getPassword()
	        ));
	    	// 查詢使用者
	        var user = userRepository.findByUEmail(request.getEmail()).orElseThrow(() -> 
	        						new IllegalArgumentException("Invalid email or password."));
	        // 檢查帳號狀態 (避免未啟用帳號登入)
	        if (user.getStatus() == null || user.getStatus() == 0) {
	            throw new IllegalStateException("帳號尚未啟用，請先完成 Email 驗證");
	        };
	        // 產生 JWT 與 Refresh Token
	        CustomUserDetails userDetails = new CustomUserDetails(user);
	        var jwt = jwtService.generateToken(userDetails);
	        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
	        var authorities = user.getRole().getAuthorities()
	        		.stream()
	                .map(GrantedAuthority::getAuthority)
	                .toList();
	        
	        return AuthenticationResponse.builder()
	                .accessToken(jwt)
	                .email(user.getuEmail())
	                .id(user.getId())
	                .roles(authorities)
	                .refreshToken(refreshToken.getToken())
	                .tokenType( TokenType.BEARER.name())
	                .build();
	    }

}
