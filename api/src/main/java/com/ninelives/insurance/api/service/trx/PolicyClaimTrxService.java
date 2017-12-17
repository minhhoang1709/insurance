package com.ninelives.insurance.api.service.trx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimBankAccountMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimCoverageMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimDetailAccidentMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimDocumentMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;

@Service
public class PolicyClaimTrxService {
	@Autowired PolicyClaimMapper policyClaimMapper;
	@Autowired PolicyClaimDetailAccidentMapper policyClaimDetailAccidentMapper;
	@Autowired PolicyClaimBankAccountMapper policyClaimBankAccountMapper;
	@Autowired PolicyClaimDocumentMapper policyClaimDocumentMapper;
	@Autowired PolicyClaimCoverageMapper policyClaimCoverageMapper;
	
	@Transactional
	public void registerPolicyClaim(PolicyClaim<?> policyClaim){
		policyClaimMapper.insert(policyClaim);
		policyClaimBankAccountMapper.insert(policyClaim.getPolicyClaimBankAccount());
		policyClaimDocumentMapper.insertList(policyClaim.getPolicyClaimDocuments());
		policyClaimCoverageMapper.insertList(policyClaim.getPolicyClaimCoverages());
		if(policyClaim.getPolicyClaimDetail() instanceof PolicyClaimDetailAccident){
			policyClaimDetailAccidentMapper.insert((PolicyClaimDetailAccident)policyClaim.getPolicyClaimDetail());
		}
	}
}
