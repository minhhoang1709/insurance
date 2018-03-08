package com.ninelives.insurance.core.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.ref.PeriodUnit;

public class OrderServicePolicyEndDateTest {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Test
	public void testEndDate(){
		
		OrderService service = new OrderService();
		
		LocalDate now = LocalDate.parse("2017-02-01",formatter);
		
		Period daily = new Period();
		daily.setUnit(PeriodUnit.DAY);
		daily.setValue(1);
		Period weekly = new Period();
		weekly.setUnit(PeriodUnit.WEEK);
		weekly.setValue(1);
		Period monthly = new Period();
		monthly.setUnit(PeriodUnit.MONTH);
		monthly.setValue(1);
		Period sixmonthly = new Period();
		sixmonthly.setUnit(PeriodUnit.MONTH);
		sixmonthly.setValue(6);
		Period yearly = new Period();
		yearly.setUnit(PeriodUnit.YEAR);
		yearly.setValue(1);
		
		assertEquals(LocalDate.parse("2017-02-01",formatter), service.calculatePolicyEndDate(now, daily));
		assertEquals(LocalDate.parse("2017-02-07",formatter), service.calculatePolicyEndDate(now, weekly));
		assertEquals(LocalDate.parse("2017-02-28",formatter), service.calculatePolicyEndDate(now, monthly));
		assertEquals(LocalDate.parse("2017-07-31",formatter), service.calculatePolicyEndDate(now, sixmonthly));
		assertEquals(LocalDate.parse("2018-01-31",formatter), service.calculatePolicyEndDate(now, yearly));
		
		now = LocalDate.parse("2017-01-30",formatter);
		
		assertEquals(LocalDate.parse("2017-01-30",formatter), service.calculatePolicyEndDate(now, daily));
		assertEquals(LocalDate.parse("2017-02-05",formatter), service.calculatePolicyEndDate(now, weekly));
		assertEquals(LocalDate.parse("2017-02-27",formatter), service.calculatePolicyEndDate(now, monthly));
		assertEquals(LocalDate.parse("2017-07-29",formatter), service.calculatePolicyEndDate(now, sixmonthly));
		assertEquals(LocalDate.parse("2018-01-29",formatter), service.calculatePolicyEndDate(now, yearly));
	}
}
