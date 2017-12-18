package com.ninelives.insurance.payment.service;

import java.time.LocalDateTime;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.model.PaymentNotificationLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.payment.NinelivesPaymentConfigProperties;
import com.ninelives.insurance.payment.dto.MidtransNotificationDto;
import com.ninelives.insurance.payment.exception.PaymentNotificationBadRequestException;
import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.exception.PaymentNotificationNotAuthorizedException;
import com.ninelives.insurance.payment.mybatis.mapper.PaymentNotificationLogMapper;
import com.ninelives.insurance.payment.ref.MidtransFraudStatus;
import com.ninelives.insurance.payment.ref.MidtransTransactionStatus;
import com.ninelives.insurance.payment.service.trx.PaymentNotificationServiceTrx;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.PaymentNotificationProcessStatus;
import com.ninelives.insurance.ref.PaymentStatus;
import com.ninelives.insurance.ref.PolicyStatus;

@Service
public class MidtransPaymentNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(MidtransPaymentNotificationService.class);
	
	@Autowired PaymentNotificationLogMapper paymentNotificationLogMapper;
	
	@Autowired NinelivesPaymentConfigProperties config;
	@Autowired OrderService orderService;
	@Autowired PaymentNotificationServiceTrx paymentNotificationServiceTrx;
	
	private static final class MidtransSuccessCondition{
		public static final String statusCodeOk="200";
		public static final String transactionStatusCapture=MidtransTransactionStatus.capture;
		public static final String transactionStatusSettlement=MidtransTransactionStatus.settlement;
		public static final String fraudStatusAccept=MidtransFraudStatus.accept;
	}
	 
	public boolean isPaymentSuccess(MidtransNotificationDto notifDto){
		if(notifDto!=null 
				&& ( StringUtils.isEmpty(notifDto.getFraudStatus())
						|| notifDto.getFraudStatus().equals(MidtransSuccessCondition.fraudStatusAccept)
						)
				&& ( MidtransSuccessCondition.transactionStatusCapture.equals(notifDto.getTransactionStatus())
						|| MidtransSuccessCondition.transactionStatusSettlement.equals(notifDto.getTransactionStatus())
						)
				&& MidtransSuccessCondition.statusCodeOk.equals(notifDto.getStatusCode())
				){
			return true;
		}
		return false;
	}
	
	public void processNotification(MidtransNotificationDto notifDto) throws PaymentNotificationException {
		LocalDateTime now = LocalDateTime.now();

		
		logger.debug("Process notification notif:<{}> ", notifDto);
		
		if(notifDto==null || StringUtils.isEmpty(notifDto.getOrderId())){
			logger.error("Receive empty notifDto");
			throw new PaymentNotificationBadRequestException(ErrorCode.ERR8200_PAYMENT_NOTIF_GENERIC_ERROR, "Receive invalid data");
		}

		String input = notifDto.getOrderId()+notifDto.getStatusCode()+notifDto.getGrossAmount()+config.getMidtrans().getServerKey();
		String signature = DigestUtils.sha512Hex(input);
		//logger.debug("signature is <{}> comparing with dto <{}>",signature,notifDto.getSignatureKey());
		if(StringUtils.isEmpty(signature)||!signature.equals(notifDto.getSignatureKey())){
			logger.error("signature is <{}> comparing with dto <{}>",signature,notifDto.getSignatureKey());
			throw new PaymentNotificationNotAuthorizedException(ErrorCode.ERR8201_PAYMENT_NOTIF_SIGNATURE_INVALID, "Signature doesnot match");
		}
		
		final PolicyOrder order = orderService.fetchOrderByOrderId(notifDto.getOrderId());		
		//logger.debug("order is <{}>", order);
		
		if(order==null||order.getPayment()==null){
			logger.error("Process notification notif:<{}> with exception: order not found", notifDto);
			throw new PaymentNotificationBadRequestException(ErrorCode.ERR8202_PAYMENT_NOTIF_ORDER_NOT_FOUND, "Order or payment not found");
		}


		PaymentNotificationLog notifLog = new PaymentNotificationLog();
		notifLog.setTransactionId(notifDto.getTransactionId());
		notifLog.setTransactionTime(notifDto.getTransactionTime());
		notifLog.setTransactionStatus(notifDto.getTransactionStatus());
		notifLog.setStatusCode(notifDto.getStatusCode());
		notifLog.setStatusMessage(notifDto.getStatusMessage());
		notifLog.setOrderId(notifDto.getOrderId());
		notifLog.setPaymentSeq(notifDto.getPaymentSeq());
		notifLog.setPaymentType(notifDto.getPaymentType());
		notifLog.setPaymentCode(notifDto.getPaymentCode());
		notifLog.setGrossAmount(notifDto.getGrossAmount());
		notifLog.setFraudStatus(notifDto.getFraudStatus());
		notifLog.setOtherProperties(notifDto.hasUnknowProperties()?notifDto.getOther().toString():"");
		
//		PolicyPayment paymentUpdate = new PolicyPayment();
//		paymentUpdate.setId(order.getPayment().getId());
//		paymentUpdate.setNotifPaymentSeq(notifDto.getPaymentSeq());
		
		PolicyOrder orderUpdate = new PolicyOrder();
		orderUpdate.setOrderId(order.getOrderId());
		orderUpdate.setPayment(new PolicyPayment());
		orderUpdate.getPayment().setOrderId(order.getOrderId());
		orderUpdate.getPayment().setId(order.getPayment().getId());
		orderUpdate.getPayment().setNotifPaymentSeq(notifDto.getPaymentSeq());
		orderUpdate.getPayment().setPaymentType(notifDto.getPaymentType());
		orderUpdate.getPayment().setProviderStatusCode(notifDto.getStatusCode());
		orderUpdate.getPayment().setProviderTransactionId(notifDto.getTransactionId());
		orderUpdate.getPayment().setProviderTransactionStatus(notifDto.getTransactionStatus());
		orderUpdate.getPayment().setProviderTransactionTime(notifDto.getTransactionTime());		

		boolean isValidForProcessing = true;
		if(notifDto.getPaymentSeq() < order.getPayment().getPaymentSeq()){
			logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error late notification", notifDto, order.getPayment());	
			//TODO: insert this
			notifLog.setProcessingStatus(PaymentNotificationProcessStatus.LATE_NOTIF);
			isValidForProcessing = false;
		}else if(notifDto.getPaymentSeq() == order.getPayment().getPaymentSeq()){
			if(order.getPayment().getProviderTransactionStatus()!=null && order.getPayment().getProviderTransactionStatus().equals(notifDto.getTransactionStatus())){
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error duplicate notif", notifDto, order.getPayment());
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.DUPLICATE);
				isValidForProcessing = false;
			}
		}else if(order.getPayment().getStatus().equals(PaymentStatus.SUCCESS)){
			logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error success but receive another notif", notifDto, order.getPayment());
			notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
			isValidForProcessing = false;
		}				

		//(capture, settlement, pending, cancel, expired) + (deny)
		if(isValidForProcessing){
			if(isPaymentSuccess(notifDto)){
				orderUpdate.getPayment().setStatus(PaymentStatus.SUCCESS);
				orderUpdate.getPayment().setNotifSuccessTime(now);
				orderUpdate.setStatus(PolicyStatus.PAID);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: success", notifDto, order.getPayment());
			}else if(MidtransTransactionStatus.pending.equals(notifDto.getTransactionStatus())){			
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)){
					orderUpdate.getPayment().setStatus(PaymentStatus.PENDING);
					orderUpdate.getPayment().setNotifPendingTime(now);
					orderUpdate.setStatus(PolicyStatus.INPAYMENT);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: pending", notifDto, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", notifDto, order.getPayment());
				}
			}else if(MidtransTransactionStatus.deny.equals(notifDto.getTransactionStatus())){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)){
					orderUpdate.getPayment().setStatus(PaymentStatus.FAIL);
					orderUpdate.getPayment().setNotifFailedTime(now);
					orderUpdate.setStatus(PolicyStatus.SUBMITTED);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: fail charge", notifDto, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", notifDto, order.getPayment());
				}
			}else if(MidtransTransactionStatus.expire.equals(notifDto.getTransactionStatus())){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)
						||order.getPayment().getStatus().equals(PaymentStatus.FAIL)){
					orderUpdate.getPayment().setStatus(PaymentStatus.EXPIRE);
					orderUpdate.getPayment().setNotifExpiredTime(now);
					orderUpdate.setStatus(PolicyStatus.OVERDUE);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: expire", notifDto, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", notifDto, order.getPayment());
				}
			}else if(MidtransTransactionStatus.cancel.equals(notifDto.getTransactionStatus())){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)
						||order.getPayment().getStatus().equals(PaymentStatus.FAIL)){
					orderUpdate.getPayment().setStatus(PaymentStatus.EXPIRE);
					orderUpdate.getPayment().setNotifExpiredTime(now);
					orderUpdate.setStatus(PolicyStatus.OVERDUE);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: cancel/expire", notifDto, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", notifDto, order.getPayment());
				}
			}else if(MidtransTransactionStatus.capture.equals(notifDto.getTransactionStatus())
					&& MidtransFraudStatus.challenge.equals(notifDto.getFraudStatus())){
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.CHALLENGE);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: challenge", notifDto, order.getPayment());
			}else{
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.UNKNOWN);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: unknown", notifDto, order.getPayment());
			}			
		}
		
		paymentNotificationLogMapper.insert(notifLog);
		
		if(isValidForProcessing){
			if(orderUpdate.getStatus()!=null){
				if(!order.getStatus().equals(orderUpdate.getStatus())
						|| !order.getPayment().getStatus().equals(orderUpdate.getPayment().getStatus())){
					paymentNotificationServiceTrx.updateOrderAndPayment(orderUpdate);
				}				
			}
		}
		
		//TODO: send notif to queue to be picked up and send to aswata
		
	}
	
}
