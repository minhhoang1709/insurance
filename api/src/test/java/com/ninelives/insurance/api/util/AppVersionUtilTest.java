package com.ninelives.insurance.api.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ninelives.insurance.api.model.Version;

public class AppVersionUtilTest {
	
	@Test
	public void testCompare(){
		Version v = new Version(5,4,3);
		
		String verify1 = null;		
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify1));
		
		String verify2 = "1.0";
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify2));
		
		String verify3 = "abc";
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify3));
		
		String verify4 = "5.4.2";
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify4));
		
		String verify5 = "5.3.4";
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify5));
		
		String verify6 = "4.5.4";
		assertEquals(true, AppVersionUtil.isUpgradeRequired(v, verify6));
		
		String verify7 = "5.4.3";
		assertEquals(false, AppVersionUtil.isUpgradeRequired(v, verify7));
		
		String verify8 = "5.4.4";
		assertEquals(false, AppVersionUtil.isUpgradeRequired(v, verify8));
		
		String verify9 = "5.5.2";
		assertEquals(false, AppVersionUtil.isUpgradeRequired(v, verify9));
		
		String verify10 = "6.3.2";
		assertEquals(false, AppVersionUtil.isUpgradeRequired(v, verify10));
	}
}
