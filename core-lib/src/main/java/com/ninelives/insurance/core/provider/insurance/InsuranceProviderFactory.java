package com.ninelives.insurance.core.provider.insurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InsuranceProviderFactory {
	public static final String INSURANCE_PROVIDER_CODE_PTI="PTI";
	public static final String INSURANCE_PROVIDER_CODE_ASWATA="ASWATA";
	
	@Autowired AswataInsuranceProvider aswataInsuranceProvider;
	@Autowired PtiInsuranceProvider ptiInsuranceProvider;
	
	public InsuranceProvider getInsuranceProvider(String providerCode){
		if(providerCode.equals(INSURANCE_PROVIDER_CODE_PTI)){
			return ptiInsuranceProvider;
		}else if(providerCode.equals(INSURANCE_PROVIDER_CODE_ASWATA)){
			return aswataInsuranceProvider;
		}else{
			throw new IllegalArgumentException("Not found insurance provider "+providerCode);
		}
	}
}
