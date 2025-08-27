package tw.yen.spring.service;


import org.springframework.stereotype.Service;

import tw.yen.spring.entity.CompanyInfo;
import tw.yen.spring.repository.CompanyInfoRespository;


@Service
public class CompanyInfoService {
	private final CompanyInfoRespository comRespository;
	
	public CompanyInfoService(CompanyInfoRespository comRespository) {
		this.comRespository = comRespository;
	}
	
	boolean taxIdExists(String taxId) {
		return comRespository.existsByTaxId(taxId);
	}
	
	public CompanyInfo save(CompanyInfo companyInfo) {
		return comRespository.save(companyInfo);
	}
	
	// 僅未改公司信箱前可用
	public Long  getCId(String uEmail) {
		return comRespository.findByREmail(uEmail).get().getId();
	}
	
}
