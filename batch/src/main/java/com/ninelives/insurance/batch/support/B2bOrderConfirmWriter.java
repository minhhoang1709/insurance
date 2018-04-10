package com.ninelives.insurance.batch.support;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.ninelives.insurance.batch.model.B2bOrderConfirmData;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.PolicyStatus;

public class B2bOrderConfirmWriter implements ItemWriter<B2bOrderConfirmData> {
	private static final Logger logger = LoggerFactory.getLogger(B2bOrderConfirmWriter.class);
	
	private OrderService orderService;
	
	public B2bOrderConfirmWriter (OrderService orderService){
		this.orderService = orderService;
	}
	
	@Override
	public void write(List<? extends B2bOrderConfirmData> items) throws Exception{
		for (B2bOrderConfirmData item: items){
			PolicyOrder order = orderService.fetchOrderByOrderId(item.getUserId(), item.getOrderId());
			
			//logger.debug("Processing for item:<{}>, order:<{}>", item, order);
			
			if(PolicyStatus.PAID.equals(order.getStatus())){
				logger.debug("Processing for item:<{}>, order:<{}>", item, order);
				orderService.orderConfirm(order);
			}else{
				logger.debug("Skip processing, status not PAID, for item:<{}>, order:<{}>", item, order);
			}
		}
	}
}
