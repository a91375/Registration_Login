package tw.yen.spring.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.payload.request.UpdateCompanyInfoRequest;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.service.UpdateCompanyInfoService;

@RestController
@RequestMapping("/api/company/update")
@RequiredArgsConstructor
public class CompanyUpdateController {
	private final UpdateCompanyInfoService updateService;
	
	@PostMapping
	public ResponseEntity<ApiResponse> updateCompany(@RequestBody UpdateCompanyInfoRequest request) {	
		return updateService.companyUpdate(request);
	}
	
}
