package com.ninelives.insurance.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.CorporateClientMapper;
import com.ninelives.insurance.model.CorporateClient;

@Service
public class CorporateClientService {
	
	@Autowired
	CorporateClientMapper corporateClientMapper;

	public int insertCorporateClient(CorporateClient corporateClient){
		return corporateClientMapper.insertSelective(corporateClient);
	}
	
	public CorporateClient selectByCompanyIdAndCorporateId(String companyName, String corporateId){
		return corporateClientMapper.selectByCompanyIdAndCorporateId(companyName,corporateId);
	}

	public List<CorporateClient> fetchAllCorporateClient() {
		return corporateClientMapper.selectAllCorporateClient();
	}

	public CorporateClient getCorporateClientById(String ccId) {
		int id = Integer.parseInt(ccId);
		return corporateClientMapper.selectCorporateClientById(id);
	}

	public int updateCorporateClient(CorporateClient cc) {
		return corporateClientMapper.updateCorporateClientSelective(cc);
	}
}
