package com.ninelives.insurance.payment.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.payment.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.ref.PolicyStatus;


@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired PolicyOrderMapper policyOrderMapper;
	
	@Value("${ninelives.order.policy-startdate-period:60}")
	int policyStartDatePeriod;
	
	@Value("${ninelives.order.policy-duedate-period:30}")
	int policyDueDatePeriod;
	
		
	public PolicyOrder fetchOrderByOrderId(final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectByOrderId(orderId);
		postRetrieval(policyOrder,LocalDate.now());
		return policyOrder;
	}
	
	
	protected void postRetrieval(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			mapPolicyOrderStatus(policyOrder,today);
		}		
	}
	
	protected void mapPolicyOrderStatus(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			if(PolicyStatus.SUBMITTED.equals(policyOrder.getStatus())){
				if(policyOrder.getOrderDate().plusDays(this.policyDueDatePeriod).isBefore(today)){
					policyOrder.setStatus(PolicyStatus.OVERDUE);
				}
			}else if(PolicyStatus.APPROVED.equals(policyOrder.getStatus())){
				if(!policyOrder.getPolicyStartDate().isAfter(today) && !policyOrder.getPolicyEndDate().isBefore(today)){
					policyOrder.setStatus(PolicyStatus.ACTIVE);
				}else if(policyOrder.getPolicyEndDate().isBefore(today)){
					policyOrder.setStatus(PolicyStatus.EXPIRED);
				}
			}		
		}
	}
}
