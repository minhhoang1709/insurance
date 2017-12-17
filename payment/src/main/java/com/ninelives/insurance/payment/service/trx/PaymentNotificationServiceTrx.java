package com.ninelives.insurance.payment.service.trx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.payment.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.payment.mybatis.mapper.PolicyPaymentMapper;

@Service
public class PaymentNotificationServiceTrx {
	@Autowired PolicyOrderMapper policyOrderMapper; 
	@Autowired PolicyPaymentMapper policyPaymentMapper; 
	
	@Transactional
	public void updateOrderAndPayment(PolicyOrder order){
		if(order.getStatus()!=null){
			policyOrderMapper.updateStatusByOrderId(order);
		}
		if(order.getPayment().getStatus()!=null){
			policyPaymentMapper.updateSelectiveByOrderIdAndPaymentId(order.getPayment());
		}		
	}
}
