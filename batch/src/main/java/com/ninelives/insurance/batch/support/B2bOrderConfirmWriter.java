package com.ninelives.insurance.batch.support;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.ninelives.insurance.batch.model.B2bOrderConfirmData;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.ref.ErrorCode;
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
			try {
				logger.debug("Processing for item:<{}>", item);
				orderService.orderConfirm(item.getOrderId());				
			} catch (AppException e) {
				if(ErrorCode.ERR4501_ORDERCONFIRM_INVALID_ORDER_STATUS.equals(e.getCode())) {
					logger.debug("Skip processing, status not PAID, for item:<{}>", item);
				}else {
					throw e;
				}
			}
		}
	}
}
