package tw.yen.spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import tw.yen.spring.dto.RegistrationRequest;
import tw.yen.spring.entity.CompanyInfo;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.service.CompanyInfoService;
import tw.yen.spring.service.UserInfoService;



@RestController
@RequestMapping("/api/register")
@AllArgsConstructor
public class RegistrationController {
	
	private final CompanyInfoService companyService;
	private final UserInfoService userService;
	
	public RegistrationController(CompanyInfoService companyService, UserInfoService userService) {
		this.companyService = companyService;
		this.userService = userService;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
		// 新增公司
		CompanyInfo company = new CompanyInfo();
		company.setcName(request.getcName());
		company.setTaxId(request.getTaxId());
		company.setrName(request.getrName());
		company.setrTel(request.getrTel());
		company.setrEmail(request.getuEmail());

		CompanyInfo savedCompany = companyService.save(company); ;

		
		// 新增User
		UserInfo user = new UserInfo();
		user.setuAccount(request.getuAccount() );
		user.setuEmail(request.getuEmail());
		user.setuPassword(UserInfoService.encodePassowrd(request.getPassword()));
		user.setStatus(request.getStatus());
		user.setRole(request.getRole());
		user.setCompanyId(savedCompany);  //companyId
		userService.save(user);
		
		return ResponseEntity.ok("註冊成功");
	}
	
}
