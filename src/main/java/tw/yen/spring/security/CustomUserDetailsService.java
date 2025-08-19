package tw.yen.spring.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tw.yen.spring.repository.UserInfoRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserInfoRepository userRepository;
	
	public CustomUserDetailsService(UserInfoRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
    public UserDetails loadUserByUsername(String email) {
		return userRepository.findByUEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

    }
	
}
