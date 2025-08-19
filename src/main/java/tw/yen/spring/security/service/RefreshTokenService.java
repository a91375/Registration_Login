package tw.yen.spring.security.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;
import tw.yen.spring.entity.RefreshToken;
import tw.yen.spring.payload.request.RefreshTokenRequest;
import tw.yen.spring.payload.response.RefreshTokenResponse;

public interface RefreshTokenService {
	RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
