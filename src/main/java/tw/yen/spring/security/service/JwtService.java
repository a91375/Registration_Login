package tw.yen.spring.security.service;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;
import tw.yen.spring.security.CustomUserDetails;

public interface JwtService {
	String extractUserName(String token);
    String generateToken(CustomUserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    ResponseCookie generateJwtCookie(String jwt);
    String getJwtFromCookies(HttpServletRequest request);
    ResponseCookie getCleanJwtCookie();
}
