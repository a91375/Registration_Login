package tw.yen.spring.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.repository.UserInfoRepository;

@Service 
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserInfoRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	
	@Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//log.info("Login attempt: {}, rawPassword from request = {}", email);
		
		UserInfo user = userRepository.findByUEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
		
		return new CustomUserDetails(user);
		
    }
	
}
