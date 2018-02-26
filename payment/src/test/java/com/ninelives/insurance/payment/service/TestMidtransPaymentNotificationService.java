package com.ninelives.insurance.payment.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.ninelives.insurance.model.PaymentNotificationLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.payment.NinelivesPaymentConfigProperties;
import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.mybatis.mapper.PaymentNotificationLogMapper;
import com.ninelives.insurance.payment.service.trx.PaymentNotificationServiceTrx;
import com.ninelives.insurance.provider.payment.midtrans.dto.MidtransNotificationDto;
import com.ninelives.insurance.provider.payment.midtrans.ref.MidtransTransactionStatus;
import com.ninelives.insurance.ref.PaymentNotificationProcessStatus;
import com.ninelives.insurance.ref.PaymentStatus;
import com.ninelives.insurance.ref.PolicyStatus;

@RunWith(SpringRunner.class)
public class TestMidtransPaymentNotificationService {
	
	/**
	 * If notifDto.paymentSequence < retrievedNotif.paymentSequence, then mark it as late notif  
	 */
	@Test
	public void testLateNotif(){		
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(1);
		notifDto.setStatusCode(MidtransTransactionStatus.capture);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(2);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;

		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
		
		//PaymentNotificationLog expectedRecord = new PaymentNotificationLog();
		
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(PaymentNotificationProcessStatus.LATE_NOTIF));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(nullValue()));
	}
	
	/**
	 * If notifDto.paymentSequence = retrievedNotif.paymentSequence, 
	 *   and the notifDto.getProviderTransactionStatus == retrievedNotif.getProviderTransactionStatus
	 *   then mark it as duplicate   
	 */
	@Test
	public void testDuplicate(){		
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(1);
		notifDto.setStatusCode("200");
		notifDto.setTransactionStatus(MidtransTransactionStatus.capture);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(1);
		retrievedOrder.getPayment().setProviderTransactionStatus(MidtransTransactionStatus.capture);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;
		
		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
		
		//PaymentNotificationLog expectedRecord = new PaymentNotificationLog();
		
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(PaymentNotificationProcessStatus.DUPLICATE));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(nullValue()));
	}
	
	@Test
	public void testOutOfOrder(){
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(2);
		notifDto.setStatusCode("200");
		notifDto.setTransactionStatus(MidtransTransactionStatus.capture);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(1);
		retrievedOrder.getPayment().setProviderTransactionStatus(MidtransTransactionStatus.capture);
		retrievedOrder.getPayment().setStatus(PaymentStatus.SUCCESS);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;
		
		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
		
		
		//PaymentNotificationLog expectedRecord = new PaymentNotificationLog();
		
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(PaymentNotificationProcessStatus.OUT_OF_ORDER));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(nullValue()));
	}
	
	@Test
	public void testOutOfOrderSameSequence(){
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(2);
		notifDto.setStatusCode("200");
		notifDto.setTransactionStatus(MidtransTransactionStatus.settlement);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(1);
		retrievedOrder.getPayment().setProviderTransactionStatus(MidtransTransactionStatus.capture);
		retrievedOrder.getPayment().setStatus(PaymentStatus.SUCCESS);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;
		
		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
		
		
		//PaymentNotificationLog expectedRecord = new PaymentNotificationLog();
		
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(PaymentNotificationProcessStatus.OUT_OF_ORDER));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(nullValue()));
	}
	
	@Test
	public void testSuccessSettlement(){
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(1);
		notifDto.setStatusCode("200");
		notifDto.setTransactionStatus(MidtransTransactionStatus.capture);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(1);
		retrievedOrder.getPayment().setStatus(PaymentStatus.CHARGE);
		retrievedOrder.setStatus(PolicyStatus.SUBMITTED);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;
		
		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
				
		//PaymentNotificationLog expectedRecord = new PaymentNotificationLog();
		
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(nullValue()));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(notNullValue()));
		assertThat(paymentNotificationServiceTrxMock.order.getStatus(), is(PolicyStatus.PAID));
		assertThat(paymentNotificationServiceTrxMock.order.getPayment().getStatus(), is(PaymentStatus.SUCCESS));
	}
	
	@Test
	public void testExpired(){
		MidtransPaymentNotificationService paymentService = new MidtransPaymentNotificationService();
		
		NinelivesPaymentConfigProperties config = new NinelivesPaymentConfigProperties();
		config.setMidtrans(new NinelivesPaymentConfigProperties.Midtrans());
		config.getMidtrans().setServerKey("key");
		
		paymentService.config = config;
		
		MidtransNotificationDto notifDto = new MidtransNotificationDto();
		notifDto.setOrderId("test-001");
		notifDto.setPaymentSeq(1);
		notifDto.setStatusCode("200");
		notifDto.setTransactionStatus(MidtransTransactionStatus.expire);
		notifDto.setGrossAmount("10000");
		
		String toSign = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(toSign);
		notifDto.setSignatureKey(signature);
				
		PolicyOrder retrievedOrder = new PolicyOrder();
		retrievedOrder.setPayment(new PolicyPayment());
		retrievedOrder.getPayment().setPaymentSeq(1);
		retrievedOrder.getPayment().setStatus(PaymentStatus.CHARGE);
		retrievedOrder.setStatus(PolicyStatus.SUBMITTED);
				
		PaymentNotificationLogMapperMock paymentNotificationLogMapperMock = new PaymentNotificationLogMapperMock();
		paymentService.paymentNotificationLogMapper = paymentNotificationLogMapperMock;
		
		PaymentNotificationServiceTrxMock paymentNotificationServiceTrxMock = new PaymentNotificationServiceTrxMock();
		paymentService.paymentNotificationServiceTrx = paymentNotificationServiceTrxMock;
		
		paymentService.orderService = Mockito.mock(OrderService.class);
		when(paymentService.orderService.fetchOrderByOrderId(notifDto.getOrderId())).thenReturn(retrievedOrder);
				
		try {
			paymentService.processNotification(notifDto);
		} catch (PaymentNotificationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertThat(paymentNotificationLogMapperMock.record, is(notNullValue()));
		assertThat(paymentNotificationLogMapperMock.record.getProcessingStatus(), is(nullValue()));
		
		assertThat(paymentNotificationServiceTrxMock.order, is(notNullValue()));
		assertThat(paymentNotificationServiceTrxMock.order.getStatus(), is(PolicyStatus.OVERDUE));
		assertThat(paymentNotificationServiceTrxMock.order.getPayment().getStatus(), is(PaymentStatus.EXPIRE));
	}
	
	
	
	public static class PaymentNotificationLogMapperMock implements PaymentNotificationLogMapper{
		PaymentNotificationLog record;

		@Override
		public int insert(PaymentNotificationLog record) {
			this.record = record;
			return 0;
		}		
	}
	
	public static class PaymentNotificationServiceTrxMock extends PaymentNotificationServiceTrx{
		PolicyOrder order;
		
		@Override
		public void updateOrderAndPayment(PolicyOrder order){
			this.order=order;
		}
	}
}
