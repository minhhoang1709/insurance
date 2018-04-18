package com.ninelives.insurance.core.service;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.VoucherType;

public class VoucherServiceTest {
	@Test
	public void testUsable(){
		VoucherService service = new VoucherService();
		
		Voucher voucher1 = null;
		assertEquals(false, service.isUsable(voucher1));
		
		Voucher voucher2 = new Voucher();
		assertEquals(false, service.isUsable(voucher2));
		
		Voucher voucher3 = new Voucher();
		voucher3.setVoucherType(VoucherType.INVITE);
		assertEquals(true, service.isUsable(voucher3));
		
		Voucher voucher4 = new Voucher();
		voucher4.setVoucherType(VoucherType.B2B);
		voucher4.setUseStartDate(LocalDate.now().minusDays(1));
		voucher4.setUseEndDate(LocalDate.now().minusDays(1));
		assertEquals(false, service.isUsable(voucher4));
		
		Voucher voucher5 = new Voucher();
		voucher5.setVoucherType(VoucherType.B2B);
		voucher5.setUseStartDate(LocalDate.now().minusDays(1));
		voucher5.setUseEndDate(LocalDate.now());
		assertEquals(true, service.isUsable(voucher5));
		
		Voucher voucher6 = new Voucher();
		voucher6.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
		voucher6.setUseStartDate(LocalDate.now().minusDays(1));
		voucher6.setUseEndDate(LocalDate.now().minusDays(1));
		assertEquals(false, service.isUsable(voucher6));
		
		Voucher voucher7 = new Voucher();
		voucher7.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
		voucher7.setUseStartDate(LocalDate.now().minusDays(1));
		voucher7.setUseEndDate(LocalDate.now());
		assertEquals(true, service.isUsable(voucher7));
		
		Voucher voucher8 = new Voucher();
		voucher8.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
		voucher8.setUseStartDate(LocalDate.now().minusDays(1));
		voucher8.setUseEndDate(LocalDate.now());
		voucher8.setMaxUse(10);
		voucher8.setApproveCnt(10);
		assertEquals(false, service.isUsable(voucher8));
		
		Voucher voucher9 = new Voucher();
		voucher9.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
		voucher9.setUseStartDate(LocalDate.now().minusDays(1));
		voucher9.setUseEndDate(LocalDate.now());
		voucher9.setMaxUse(10);
		voucher9.setApproveCnt(11);
		assertEquals(false, service.isUsable(voucher9));
		
		Voucher voucher10 = new Voucher();
		voucher10.setVoucherType(VoucherType.FREE_PROMO_NEW_USER);
		voucher10.setUseStartDate(LocalDate.now().minusDays(1));
		voucher10.setUseEndDate(LocalDate.now());
		voucher10.setMaxUse(10);
		voucher10.setApproveCnt(9);
		assertEquals(true, service.isUsable(voucher10));
		
	}
}
