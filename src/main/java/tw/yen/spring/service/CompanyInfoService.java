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
	
	public CompanyInfo save(CompanyInfo companyInfo) {
		return comRespository.save(companyInfo);
	}
	
}
