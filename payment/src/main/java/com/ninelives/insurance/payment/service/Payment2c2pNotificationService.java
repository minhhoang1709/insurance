package com.ninelives.insurance.payment.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.model.PaymentNotificationLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.payment.NinelivesPaymentConfigProperties;
import com.ninelives.insurance.payment.exception.PaymentNotificationBadRequestException;
import com.ninelives.insurance.payment.exception.PaymentNotificationException;
import com.ninelives.insurance.payment.exception.PaymentNotificationNotAuthorizedException;
import com.ninelives.insurance.payment.mybatis.mapper.PaymentNotificationLogMapper;
import com.ninelives.insurance.payment.service.trx.PaymentNotificationServiceTrx;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.PaymentNotificationProcessStatus;
import com.ninelives.insurance.ref.PaymentStatus;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.route.EndPointRef;

@Service
public class Payment2c2pNotificationService {
	private static final Logger logger = LoggerFactory.getLogger(Payment2c2pNotificationService.class);
	
	@Autowired PaymentNotificationLogMapper paymentNotificationLogMapper;
	
	@Autowired NinelivesPaymentConfigProperties config;
	@Autowired OrderService orderService;
	@Autowired InviteService inviteService;
	@Autowired PaymentNotificationServiceTrx paymentNotificationServiceTrx;
	
	@Autowired FluentProducerTemplate producerTemplate;
	
	public boolean isPaymentSuccess(HttpServletRequest request){
		String paymentStatus = request.getParameter("payment_status");
		if(request!=null && (paymentStatus.equals("000"))){
			return true;
		}
		return false;
	
	}
	
	public void processNotification(HttpServletRequest request, HttpServletResponse response) throws PaymentNotificationException {
		LocalDateTime now = LocalDateTime.now();
		logger.info("Start process notification notif:<{}> ", request);
		
		String orderIdMap = request.getParameter("order_id");
		String statusCode =  String.valueOf(response.getStatus());
		String channelResponseCode = request.getParameter("channel_response_code");
		String paymentType = getPaymentType(request.getParameter("payment_channel"));
		String transactionStatus = getPaymentStatusResponseDescription(request.getParameter("payment_status"));
	    
		if(request==null || StringUtils.isEmpty(orderIdMap)){
			logger.error("Receive empty ResponsePayment 2c2p");
			throw new PaymentNotificationBadRequestException(ErrorCode.ERR8200_PAYMENT_NOTIF_GENERIC_ERROR, "Receive invalid data");
		}

		String input = request.getParameter("version")+request.getParameter("request_timestamp")+
				request.getParameter("merchant_id")+request.getParameter("order_id")+
				request.getParameter("invoice_no")+
				request.getParameter("currency")+request.getParameter("amount")+
				request.getParameter("transaction_ref")+request.getParameter("approval_code")+
				request.getParameter("eci")+request.getParameter("transaction_datetime")+
				request.getParameter("payment_channel")+request.getParameter("payment_status")+
				request.getParameter("channel_response_code")+request.getParameter("channel_response_desc")+
				request.getParameter("masked_pan")+request.getParameter("stored_card_unique_id")+
				request.getParameter("backend_invoice")+request.getParameter("paid_channel")+
				request.getParameter("paid_agent")+request.getParameter("recurring_unique_id")+
				request.getParameter("user_defined_1")+request.getParameter("user_defined_2")+
				request.getParameter("user_defined_3")+request.getParameter("user_defined_4")+
				request.getParameter("user_defined_5")+request.getParameter("browser_info")+
				request.getParameter("ippPeriod")+request.getParameter("ippInterestType")+
				request.getParameter("ippInterestRate")+request.getParameter("ippMerchantAbsorbRate")+
				request.getParameter("payment_scheme")+request.getParameter("process_by");
		
		String hash_value = request.getParameter("hash_value");
		
		if(!checkSignature(config.getPay2c2p().getServerKey(),hash_value,input)){
			logger.error("signature is <{}> comparing with dto <{}>","",hash_value);
			throw new PaymentNotificationNotAuthorizedException(ErrorCode.ERR8201_PAYMENT_NOTIF_SIGNATURE_INVALID, "Signature doesnot match");
		}
		
		final PolicyOrder order = orderService.fetchOrderByOrderIdMap(orderIdMap);		
		logger.debug("order is <{}>", order.toString());
		
		if(order==null){
			logger.error("Error process notification notif:<{}> with exception: order not found", request);
			throw new PaymentNotificationBadRequestException(ErrorCode.ERR8202_PAYMENT_NOTIF_ORDER_NOT_FOUND, "Order or payment not found");
		}


		PaymentNotificationLog notifLog = new PaymentNotificationLog();
		notifLog.setTransactionId(request.getParameter("transaction_ref"));
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime transactionDateTime = LocalDateTime.parse(request.getParameter("transaction_datetime"), formatter);
		notifLog.setTransactionTime(transactionDateTime);
		
		notifLog.setTransactionStatus(transactionStatus);
		notifLog.setStatusCode(statusCode);
		notifLog.setStatusMessage("2c2p payment notification");
		notifLog.setOrderId(order.getOrderId());
		notifLog.setPaymentType(paymentType);
		notifLog.setGrossAmount(getAmountString(request.getParameter("amount")));
		
		
		PolicyOrder orderUpdate = new PolicyOrder();
		orderUpdate.setOrderId(order.getOrderId());
		orderUpdate.setPayment(new PolicyPayment());
		orderUpdate.getPayment().setOrderId(order.getOrderId());
		orderUpdate.getPayment().setId(order.getPayment().getId());
		orderUpdate.getPayment().setPaymentType(paymentType);
		orderUpdate.getPayment().setProviderStatusCode(statusCode);
		orderUpdate.getPayment().setProviderTransactionId(request.getParameter("transaction_ref"));
		orderUpdate.getPayment().setProviderTransactionStatus(transactionStatus);
		orderUpdate.getPayment().setProviderTransactionTime(transactionDateTime);		

		boolean isValidForProcessing = true;
		if(channelResponseCode.equals("9020")){
			if(order.getPayment().getProviderTransactionStatus()!=null && order.getPayment().getProviderTransactionStatus().equals(transactionStatus)){
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error duplicate notif", request, order.getPayment());
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.DUPLICATE);
				isValidForProcessing = false;
			}
		}				

		//(capture, settlement, pending, cancel, expired) + (deny)
		boolean isPaymentSuccess = false;
		if(isValidForProcessing){
			isPaymentSuccess = isPaymentSuccess(request);
			if(isPaymentSuccess){
				orderUpdate.getPayment().setStatus(PaymentStatus.SUCCESS);
				orderUpdate.getPayment().setNotifSuccessTime(now);
				orderUpdate.setStatus(PolicyStatus.PAID);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: success", request, order.getPayment());
			}else if(transactionStatus.equals("Pending")){			
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)){
					orderUpdate.getPayment().setStatus(PaymentStatus.PENDING);
					orderUpdate.getPayment().setNotifPendingTime(now);
					orderUpdate.setStatus(PolicyStatus.INPAYMENT);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: pending", request, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", request, order.getPayment());
				}
			}else if(transactionStatus.equals("Rejected")){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)){
					orderUpdate.getPayment().setStatus(PaymentStatus.FAIL);
					orderUpdate.getPayment().setNotifFailedTime(now);
					orderUpdate.setStatus(PolicyStatus.SUBMITTED);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: fail charge", request, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", request, order.getPayment());
				}
			}else if(channelResponseCode.equals("9022")){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)
						||order.getPayment().getStatus().equals(PaymentStatus.FAIL)){
					orderUpdate.getPayment().setStatus(PaymentStatus.EXPIRE);
					orderUpdate.getPayment().setNotifExpiredTime(now);
					orderUpdate.setStatus(PolicyStatus.OVERDUE);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: expire", request, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", request, order.getPayment());
				}
			}else if(transactionStatus.equals("Cancel")){
				if(order.getPayment().getStatus().equals(PaymentStatus.CHARGE)
						||order.getPayment().getStatus().equals(PaymentStatus.PENDING)
						||order.getPayment().getStatus().equals(PaymentStatus.FAIL)){
					orderUpdate.getPayment().setStatus(PaymentStatus.EXPIRE);
					orderUpdate.getPayment().setNotifExpiredTime(now);
					orderUpdate.setStatus(PolicyStatus.OVERDUE);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: cancel/expire", request, order.getPayment());
				}else{
					notifLog.setProcessingStatus(PaymentNotificationProcessStatus.OUT_OF_ORDER);
					logger.info("Process notification notif:<{}> with retrieved payment <{}> result: error out of order", request, order.getPayment());
				}
			}else if(channelResponseCode.equals("0034")){
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.CHALLENGE);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: challenge", request, order.getPayment());
			}else{
				notifLog.setProcessingStatus(PaymentNotificationProcessStatus.UNKNOWN);
				logger.info("Process notification notif:<{}> with retrieved payment <{}> result: unknown", request, order.getPayment());
			}			
		}
		
		paymentNotificationLogMapper.insert(notifLog);
		
		if(isValidForProcessing){
			if(orderUpdate.getStatus()!=null){
				if(!order.getStatus().equals(orderUpdate.getStatus())
						|| !order.getPayment().getStatus().equals(orderUpdate.getPayment().getStatus())){
					paymentNotificationServiceTrx.updateOrderAndPayment(orderUpdate);
				}
				
				if (isPaymentSuccess) {
					try{
						//send payment confirm to aswata			
						PolicyOrder successPaymentOrder = new PolicyOrder();
						successPaymentOrder.setOrderId(order.getOrderId());
						successPaymentOrder.setUserId(order.getUserId());
						successPaymentOrder.setOrderTime(order.getOrderTime());
						successPaymentOrder.setCoverageCategoryId(order.getCoverageCategoryId());
						successPaymentOrder.setProviderOrderNumber(order.getProviderOrderNumber());
						successPaymentOrder.setTotalPremi(order.getTotalPremi());
						successPaymentOrder.setPayment(new PolicyPayment());
						successPaymentOrder.getPayment().setProviderTransactionId(orderUpdate.getPayment().getProviderTransactionId());
						
						logger.debug("Send success payment order to insurance provider <{}>", successPaymentOrder);
						//producerTemplate.to(EndPointRef.QUEUE_SUCCESS_PAYMENT_TO_INSURER).withBodyAs(successPaymentOrder,PolicyOrder.class).send();
						producerTemplate.to(EndPointRef.QUEUE_SUCCESS_PAYMENT_TO_INSURER_BY_ORDERID).withBodyAs(order.getOrderId(),String.class).send();
						
						
								
						//update spend total for invite voucher
						inviteService.updateUserAggStatOnSuccessPayment(order);
					}catch(Exception e){
						logger.error("Generic exception on updating invite",e);
					}
				}
			}			
		}
		
		//TODO: maybe move agg stat to aswata process (after queue)
		
	}
	
	
	private String getAmountString(String amount) {
		String rValue="";
		int rAmount = Integer.parseInt(amount);
		rValue = String.valueOf(rAmount);
		return rValue;
	}

	public static boolean checkSignature( final String queryKey, 
			final String subject, String baseString ){ 
		boolean rValue=false;
		try 
	    { 
	     String hashParam =hashSignature(queryKey, baseString);
	     	if(hashParam.equals(subject)){
	     		rValue=true;
	     	}
	    }catch (Exception e){ 
	     System.out.println(e.toString());
	    } 
	    
	    return rValue;
	  
	} 
	
	public static String hashSignature(String secretKey, String arrayString) {

        String hashedSignature = "";
        try {

            Mac sha1HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1");
            sha1HMAC.init(secretKeySpec);
            hashedSignature = bytesToHex(sha1HMAC.doFinal(arrayString.getBytes("UTF-8"))).toUpperCase();
        } catch (Exception e) { e.printStackTrace(); }

        return hashedSignature;
    }
	
	private static String bytesToHex(byte[] bytes) {

        final char[] hexArray = "0123456789abcdef".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
	
	
	private String getPaymentStatusResponseDescription(String paymentCode){
		String rValue="";
		if(paymentCode.equals("000")){
			rValue="Success";
		}else 
		if(paymentCode.equals("001")){
			rValue="Pending";
		}else 
		if(paymentCode.equals("002")){
			rValue="Rejected";
		}else 
		if(paymentCode.equals("003")){
			rValue="Cancel";
		}else 
		if(paymentCode.equals("999")){
			rValue="Failed";
		}		
		
		return rValue;
	}
	
	
	private String getPaymentType(String paymentChannel){
		String rValue="";
		if(paymentChannel.equals("001")){
			rValue="Credit and debit cards";
		}else 
		if(paymentChannel.equals("002")){
			rValue="Cash payment channel";
		}else 
		if(paymentChannel.equals("003")){
			rValue="Direct debit";
		}else 
		if(paymentChannel.equals("004")){
			rValue="Others";
		}else 
		if(paymentChannel.equals("005")){
			rValue="IPP transaction";
		}		
		
		return rValue;
	}
	
}
