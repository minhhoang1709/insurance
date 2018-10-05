package com.ninelives.insurance.core.provider.insurance;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mockito;

import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageOption;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.provider.insurance.aswata.ref.TravelType;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.CoverageOptionId;

public class AswataInsuranceProviderTest {
	/*
	 * Case:
	 *  - input medical asean, return type asean
	 *  - input medical international, return type worldwide
	 *  - input medical nz, return type nz
	 *  - input nonmedical nondomestic, return type worldwide
	 *  - input any domestic, return domestic
	 */
	@Test
	public void testGetProviderTravelTypeAsean(){
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("102002");
		cov1.setCoverageOptionId(CoverageOptionId.OPTION_ASEAN);
		cov1.setCoverageOption(new CoverageOption());
		cov1.getCoverageOption().setId(CoverageOptionId.OPTION_ASEAN);		

		Coverage cov2 = new Coverage();
		cov2.setCoverageId("102001");				

		//input test
		PolicyOrder po = new PolicyOrder();
		po.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		po.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(0).setCoverageId(cov1.getCoverageId());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(1).setCoverageId(cov2.getCoverageId());
		
		//setup class
		AswataInsuranceProvider provider = new AswataInsuranceProvider(null);
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(cov1.getCoverageId())).thenReturn(cov1);
		when(provider.productService.fetchCoverageByCoverageId(cov2.getCoverageId())).thenReturn(cov2);
		
		Integer result = provider.getProviderTravelType(po);
		assertThat(result, is(TravelType.TYPE_ASIA));
	}
	
	@Test
	public void testGetProviderTravelTypeNz(){
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("102001");				

		Coverage cov2 = new Coverage();
		cov2.setCoverageId("102002");
		cov2.setCoverageOptionId(CoverageOptionId.OPTION_EU_AU_NZ);
		cov2.setCoverageOption(new CoverageOption());
		cov2.getCoverageOption().setId(CoverageOptionId.OPTION_EU_AU_NZ);		

		//input test
		PolicyOrder po = new PolicyOrder();
		po.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		po.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(0).setCoverageId(cov1.getCoverageId());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(1).setCoverageId(cov2.getCoverageId());
		
		//setup class
		AswataInsuranceProvider provider = new AswataInsuranceProvider(null);
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(cov1.getCoverageId())).thenReturn(cov1);
		when(provider.productService.fetchCoverageByCoverageId(cov2.getCoverageId())).thenReturn(cov2);
		
		Integer result = provider.getProviderTravelType(po);
		assertThat(result, is(TravelType.TYPE_EU_AUS_NZ));
	}
	
	@Test
	public void testGetProviderTravelTypeWorldwide(){
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("102002");
		cov1.setCoverageOptionId(CoverageOptionId.OPTION_INTERNATIONAL);
		cov1.setCoverageOption(new CoverageOption());
		cov1.getCoverageOption().setId(CoverageOptionId.OPTION_INTERNATIONAL);		

		Coverage cov2 = new Coverage();
		cov2.setCoverageId("102001");			

		//input test
		PolicyOrder po = new PolicyOrder();
		po.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());
		po.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(0).setCoverageId(cov1.getCoverageId());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(1).setCoverageId(cov2.getCoverageId());
		
		//setup class
		AswataInsuranceProvider provider = new AswataInsuranceProvider(null);
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(cov1.getCoverageId())).thenReturn(cov1);
		when(provider.productService.fetchCoverageByCoverageId(cov2.getCoverageId())).thenReturn(cov2);
		
		Integer result = provider.getProviderTravelType(po);
		assertThat(result, is(TravelType.TYPE_WORLDWIDE));
	}
	
	@Test
	public void testGetProviderTravelTypeWorldwideNonMedical(){
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("102001");				

		Coverage cov2 = new Coverage();
		cov2.setCoverageId("102002");

		//input test
		PolicyOrder po = new PolicyOrder();
		po.setCoverageCategoryId(CoverageCategoryId.TRAVEL_INTERNATIONAL);
		po.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());		
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(0).setCoverageId(cov1.getCoverageId());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(1).setCoverageId(cov2.getCoverageId());
		
		//setup class
		AswataInsuranceProvider provider = new AswataInsuranceProvider(null);
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(cov1.getCoverageId())).thenReturn(cov1);
		when(provider.productService.fetchCoverageByCoverageId(cov2.getCoverageId())).thenReturn(cov2);
		
		Integer result = provider.getProviderTravelType(po);
		assertThat(result, is(TravelType.TYPE_WORLDWIDE));
	}
	
	@Test
	public void testGetProviderTravelTypeDomestic(){
		Coverage cov1 = new Coverage();
		cov1.setCoverageId("102001");

		Coverage cov2 = new Coverage();
		cov2.setCoverageId("102002");

		//input test
		PolicyOrder po = new PolicyOrder();
		po.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
		po.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(0).setCoverageId(cov1.getCoverageId());
		po.getPolicyOrderProducts().add(new PolicyOrderProduct());
		po.getPolicyOrderProducts().get(1).setCoverageId(cov2.getCoverageId());
		
		//setup class
		AswataInsuranceProvider provider = new AswataInsuranceProvider(null);
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(cov1.getCoverageId())).thenReturn(cov1);
		when(provider.productService.fetchCoverageByCoverageId(cov2.getCoverageId())).thenReturn(cov2);
		
		Integer result = provider.getProviderTravelType(po);
		assertThat(result, is(TravelType.TYPE_DOMESTIC));
	}
}
