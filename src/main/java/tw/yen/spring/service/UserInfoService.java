package tw.yen.spring.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.repository.CompanyInfoRespository;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.utils.BCrypt;

@Service
@AllArgsConstructor
public class UserInfoService{

	private final UserInfoRepository userRespository;
	// private String encodedPassword;
	
	public UserInfoService(UserInfoRepository userRespository) {
		this.userRespository = userRespository;
	}
	
	public static String encodePassowrd(String rawPassword) {
		String encodedPassword= BCrypt.hashpw(rawPassword, BCrypt.gensalt());
		return encodedPassword;
	}
	
	public String signUpUser(UserInfo userInfo) {
			boolean userExists = userRespository.findByUEmail(userInfo.getuEmail()).isPresent();

			if (userExists) {
				throw new IllegalStateException("此信箱已使用");
			}
			String encodedPassword = BCrypt.hashpw(userInfo.getuPassword(), BCrypt.gensalt());
			userInfo.setuPassword(encodedPassword);
			userRespository.save(userInfo);
			
			return "SingUp Ok";
			// token待寫
	}
	
	public UserInfo save(UserInfo userInfo) {
		return userRespository.save(userInfo);
	}
	
	public void enableUser(String email) {
		userRespository.enableUser(email);
	}
	
	
	
}
