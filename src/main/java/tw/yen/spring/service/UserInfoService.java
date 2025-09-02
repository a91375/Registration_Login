package tw.yen.spring.service;


import org.springframework.stereotype.Service;

import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.repository.UserInfoRepository;

@Service
public class UserInfoService {

	private final UserInfoRepository userRespository;
	
	public UserInfoService(UserInfoRepository userRespository) {
		this.userRespository = userRespository;
	}
	
	public boolean userExists(String uEmail) {
		return userRespository.findByUEmail(uEmail).isPresent();
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
