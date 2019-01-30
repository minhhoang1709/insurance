package com.ninelives.insurance.core.provider.insurance;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.ninelives.insurance.core.mybatis.mapper.InsurerOrderLogMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.InsurerOrderLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyOrderVoucher;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.provider.insurance.aswata.ref.PackageType;
import com.ninelives.insurance.provider.insurance.aswata.ref.ProductCode;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.VoucherType;

public class AswataInsuranceProviderPackageTypeOrderTest {
	AswataInsuranceProvider provider; 
	PolicyOrder order;
	@Spy FakeInsurerOrderLogMapper insurerOrderLogMapper;
	
	@Test
	public void testPa() {
		order.setCoverageCategoryId(CoverageCategoryId.PERSONAL_ACCIDENT);
		
		try {
			provider.orderPolicy(order);
		} catch (IOException | StorageException | InsuranceProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(PackageType.TYPE_PA_NORMAL, insurerOrderLogMapper.record.getPackageType());
		assertEquals(ProductCode.PA, insurerOrderLogMapper.record.getProductCode());
	}
	
	@Test
	public void testPaB2b() {
		order.setCoverageCategoryId(CoverageCategoryId.PERSONAL_ACCIDENT);
		order.setPolicyOrderVoucher(new PolicyOrderVoucher());
		order.getPolicyOrderVoucher().setVoucher(new Voucher());
		order.getPolicyOrderVoucher().getVoucher().setVoucherType(VoucherType.B2B);
		try {
			provider.orderPolicy(order);
		} catch (IOException | StorageException | InsuranceProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(PackageType.TYPE_PA_B2B, insurerOrderLogMapper.record.getPackageType());
		assertEquals(ProductCode.PA, insurerOrderLogMapper.record.getProductCode());
	}
	
	@Test
	public void testTravel() {
		order.setCoverageCategoryId(CoverageCategoryId.TRAVEL_DOMESTIC);
	
		try {
			provider.orderPolicy(order);
		} catch (IOException | StorageException | InsuranceProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(PackageType.TYPE_TRAVEL, insurerOrderLogMapper.record.getPackageType());
		assertEquals(ProductCode.TRAVEL, insurerOrderLogMapper.record.getProductCode());
	}
	
	@Test
	public void testSelfie() {
		order.setCoverageCategoryId(CoverageCategoryId.SELFIE);
	
		try {
			provider.orderPolicy(order);
		} catch (IOException | StorageException | InsuranceProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(PackageType.TYPE_SELFIE, insurerOrderLogMapper.record.getPackageType());
		assertEquals(ProductCode.SELFIE, insurerOrderLogMapper.record.getProductCode());
	}
	
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		 
		provider = new AswataInsuranceProvider(null);
		provider.setEnableConnection(true);
		
		Coverage covDummy = new Coverage();
		covDummy.setProviderCode("providerCode");
		
		provider.productService = Mockito.mock(ProductService.class);
		when(provider.productService.fetchCoverageByCoverageId(any(String.class))).thenReturn(covDummy);		
		
		order = new PolicyOrder();
		order.setUserId("userId");
		order.setOrderTime(LocalDateTime.now());
		order.setPolicyOrderUsers(new PolicyOrderUsers());
		order.getPolicyOrderUsers().setName("userName");
		order.getPolicyOrderUsers().setBirthDate(LocalDate.now());
		order.getPolicyOrderUsers().setGender(Gender.MALE);
		order.getPolicyOrderUsers().setPhone("6281818131");
		order.getPolicyOrderUsers().setEmail("email");
		order.setPolicyStartDate(LocalDate.now());
		order.setPolicyEndDate(LocalDate.now());
		order.setPolicyOrderProducts(new ArrayList<PolicyOrderProduct>());
		order.setIsFamily(false);
		
		insurerOrderLogMapper.record=null;
		provider.insurerOrderLogMapper = insurerOrderLogMapper;
	}
	
	abstract class FakeInsurerOrderLogMapper implements InsurerOrderLogMapper{
		InsurerOrderLog record;
		
		@Override
		public int insertSelective (InsurerOrderLog record) {
			this.record = record;
			return 1;
		}

	}
}
