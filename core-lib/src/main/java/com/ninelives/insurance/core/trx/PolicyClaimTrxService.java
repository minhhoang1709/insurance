package com.ninelives.insurance.core.trx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimBankAccountMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimCoverageMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimDetailAccidentMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimDocumentMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimFamilyMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;

@Service
public class PolicyClaimTrxService {
	@Autowired PolicyClaimMapper policyClaimMapper;
	@Autowired PolicyClaimDetailAccidentMapper policyClaimDetailAccidentMapper;
	@Autowired PolicyClaimBankAccountMapper policyClaimBankAccountMapper;
	@Autowired PolicyClaimDocumentMapper policyClaimDocumentMapper;
	@Autowired PolicyClaimCoverageMapper policyClaimCoverageMapper;
	@Autowired PolicyClaimFamilyMapper policyClaimFamilyMapper;
	
	@Transactional
	public void registerPolicyClaim(PolicyClaim<?> policyClaim){
		policyClaimMapper.insert(policyClaim);
		policyClaimBankAccountMapper.insert(policyClaim.getPolicyClaimBankAccount());
		policyClaimDocumentMapper.insertList(policyClaim.getPolicyClaimDocuments());
		policyClaimCoverageMapper.insertList(policyClaim.getPolicyClaimCoverages());
		if(policyClaim.getPolicyClaimDetail() instanceof PolicyClaimDetailAccident){
			policyClaimDetailAccidentMapper.insert((PolicyClaimDetailAccident)policyClaim.getPolicyClaimDetail());
		}
		if(policyClaim.getHasFamily()!=null && policyClaim.getHasFamily().equals(true)){
			policyClaimFamilyMapper.insertList(policyClaim.getPolicyClaimFamilies());
		}
	}
}
