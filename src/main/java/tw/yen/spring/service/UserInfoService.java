package tw.yen.spring.service;

import org.springframework.stereotype.Service;

import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.utils.BCrypt;

@Service
public class UserInfoService{

	private final UserInfoRepository userRespository;
	// private String encodedPassword;
	
	public UserInfoService(UserInfoRepository userRespository) {
		this.userRespository = userRespository;
	}
	
	boolean userExists(String uEmail) {
		return userRespository.findByUEmail(uEmail).isPresent();
	}
	
	public static String encodePassowrd(String rawPassword) {
		String encodedPassword= BCrypt.hashpw(rawPassword, BCrypt.gensalt());
		return encodedPassword;
	}
	
	public UserInfo save(UserInfo userInfo) {
		return userRespository.save(userInfo);
	}
	public void enableUser(String email) {
		userRespository.enableUser(email);
	}
	
	public void updateCompanyId(String email, Long cId) {
		userRespository.updateCompanyId(email, cId);
	}
	
}
