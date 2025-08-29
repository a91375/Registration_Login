package tw.yen.spring.service;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tw.yen.spring.entity.CompanyInfo;
import tw.yen.spring.entity.UserInfo;
import tw.yen.spring.exception.NotFoundException;
import tw.yen.spring.payload.request.UpdateCompanyInfoRequest;
import tw.yen.spring.payload.response.ApiResponse;
import tw.yen.spring.payload.response.CompanyInfoResponse;
import tw.yen.spring.repository.CompanyInfoRespository;
import tw.yen.spring.repository.UserInfoRepository;
import tw.yen.spring.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UpdateCompanyInfoService {
	private final UserInfoRepository userRepository;
	private final CompanyInfoRespository companyRepository;
	
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> companyUpdate(UpdateCompanyInfoRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		UserInfo user = userRepository.findByUEmail(userDetails.getUsername())
	            .orElseThrow(() -> new NotFoundException("找不到使用者"));
		CompanyInfo company =companyRepository.findById(user.getcId())
		        .orElseThrow(() -> new NotFoundException("找不到公司資料"));
		
		if (request.getCName() != null) company.setcName(request.getCName());
		if (request.getRName() != null) company.setrName(request.getRName());
		if (request.getRTel() != null) company.setrTel(request.getRTel());
		if (request.getREmail() != null) company.setrEmail(request.getREmail());
		companyRepository.save(company);
		        
		return ApiResponse.success("資料更新成功！");
	}
	
	@Transactional(readOnly = true)
    public ApiResponse<CompanyInfoResponse> getCompanyInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserInfo user = userRepository.findByUEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("找不到使用者"));
        CompanyInfo company = companyRepository.findById(user.getcId())
                .orElseThrow(() -> new NotFoundException("找不到公司資料"));
	
        CompanyInfoResponse response = new CompanyInfoResponse(
                company.getcName(),
                company.getTaxId(),
                company.getrName(),
                company.getrTel(),
                company.getrEmail()
        );
        
        return ApiResponse.success("公司資料取得成功", response);
	}
	
	
}	