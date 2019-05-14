package com.ninelives.insurance.core.provider.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.ref.InsurerCode;

@Component
public class InsuranceProviderFactory {
	//public static final String INSURANCE_PROVIDER_CODE_PTI="PTI";
	//public static final String INSURANCE_PROVIDER_CODE_ASWATA="ASWATA";
	
	@Autowired AswataInsuranceProvider aswataInsuranceProvider;
	@Autowired PtiInsuranceProvider ptiInsuranceProvider;
	
	public InsuranceProvider getInsuranceProvider(InsurerCode code){
		if(code.equals(InsurerCode.PTI)){
			return ptiInsuranceProvider;
		}else if(code.equals(InsurerCode.ASWATA)){
			return aswataInsuranceProvider;
		}else{
			throw new IllegalArgumentException("Not found insurance provider "+code);
		}
	}
}
