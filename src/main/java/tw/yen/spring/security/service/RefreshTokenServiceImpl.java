package tw.yen.spring.security.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.yen.spring.entity.RefreshToken;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.payload.request.RefreshTokenRequest;
import tw.yen.spring.payload.response.RefreshTokenResponse;
import tw.yen.spring.repository.RefreshTokenRepository;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.security.CustomUserDetails;
import tw.yen.spring.security.enums.TokenType;
import tw.yen.spring.security.handlers.TokenException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final UserInfoRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;
    
    // 建立新的 RefreshToken，存到 DB 並回傳
    @Override
    public RefreshToken createRefreshToken(Long userId) {
        UserInfo user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
    
    // 驗證 Refresh Token 是否過期
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if(token == null){
            log.error("Token is null");
            throw TokenException.invalid(null);
        }
        if(token.getExpiryDate().compareTo(Instant.now()) < 0 ){
            refreshTokenRepository.delete(token);
            throw TokenException.expired(token.getToken());
        }
        return token;
    }
    
    // 從 DB 查詢 refresh token
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    // 產生新的 Access Token
    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        UserInfo user = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> TokenException.invalid(request.getRefreshToken()));

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }
    
    // 建立 Refresh Token Cookie
    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")  // 全站可用
                .maxAge(refreshExpiration/1000) // 15 days in seconds
                .httpOnly(true)  // 前端 JS 不能讀取
                .secure(true)  // 只在 HTTPS 傳輸
                .sameSite("Strict")  // 防止跨站請求
                .build();
    }
    
    // 從 Cookie 取 Refresh Token
    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }
    
    // 刪除 Refresh Token
    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }
    
    // 建立空的 Refresh Token Cookie
    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
}
