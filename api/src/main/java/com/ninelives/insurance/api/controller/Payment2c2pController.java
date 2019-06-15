package com.ninelives.insurance.api.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.service.ApiOrderService;
import com.ninelives.insurance.api.service.AuthService;
import com.ninelives.insurance.api.util.Payment2c2pUtil;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.mybatis.mapper.PaymentChargeLogMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyPaymentMapper;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.model.PaymentChargeLog;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyPayment;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.PolicyStatus;

@Controller
@RequestMapping("/api")
public class Payment2c2pController {
	private static final Logger logger = LoggerFactory.getLogger(Payment2c2pController.class);
	
	@Autowired NinelivesConfigProperties config;
	
	//@Autowired ApiOrderService apiOrderService;
	
	@Autowired OrderService orderService;
	
	@Autowired AuthService authService;
	
	@Autowired PolicyPaymentMapper policyPaymentMapper;
	
	@Autowired PaymentChargeLogMapper paymentChargeLogMapper;
	
	private boolean isEnabled = true;
	
	private String merchantId;
	private String key;
	private String version;
	private String resultUrl1;
	private String resultUrl2;
	private String currency;	
	private String paymentUrl;
	
	@RequestMapping(value="/resultPayment/2c2p",method=RequestMethod.POST)
	@ResponseBody
	public String resultPayment2c2p(
			HttpServletRequest request, 
			HttpServletResponse response,
			Model model ) throws AppException{
		
		String baseString = Payment2c2pUtil.getBaseString(request); 
		String hash_value = request.getParameter("hash_value");
		String resultPay = Payment2c2pUtil.getPaymentStatusResponseDescription(request.getParameter("payment_status"));
		String orderId2c2p = request.getParameter("order_id");
		String contextPath = request.getContextPath();
		
		if(Payment2c2pUtil.checkSignature(key,hash_value,baseString)){
			if(resultPay.equals("success")){
				String successPage = Payment2c2pUtil.getSuccessPage(orderId2c2p,contextPath);
				return successPage;
			}else{
				String failedPage = Payment2c2pUtil.getFailedPage(orderId2c2p,contextPath,resultPay);
				return failedPage;
			}
		}
		
		String invalidHashPage = Payment2c2pUtil.getInvalidHashPage(orderId2c2p,contextPath);
		return invalidHashPage;
		
	}
	
	
	@RequestMapping(value="/orders/{orderId}/payfinish",method=RequestMethod.GET)
	@ResponseBody
	public String finish(
			@PathVariable("orderId") String orderId,
			@RequestParam(value="result",required=false) String result,
			Model model ) throws AppException{
		
		return "";
		
	
	}
	
	
	@RequestMapping(value="/orders/{orderId}/pay",method={RequestMethod.GET})	
	@ResponseBody
	public String paymentProvider(
			@PathVariable("orderId") String order_Id,
			@RequestParam(value="pg",required=false) String pg,
			@RequestParam(value="access_token",required=false) String accessToken)
			throws AppException{
		
		
		ApiSessionData sessionData;
		try {
			sessionData = authService.validateAuthToken(accessToken);
		} catch (AppNotAuthorizedException e) {
			throw new AppBadRequestException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication code not valid");
		}
			
		logger.debug("GET PolicyOrder userid is {} with orderid {}", sessionData.getUserId(), order_Id);
		StringBuffer sbHtml = new StringBuffer();
		LocalDateTime now = LocalDateTime.now();
		PolicyOrder order = orderService.fetchOrderByOrderId(sessionData.getUserId(), order_Id);
		
		if(order==null){
			logger.debug("Process charge for user <{}> result: order not found", accessToken);
			throw new AppBadRequestException(ErrorCode.ERR8003_CHARGE_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		if(!PolicyStatus.SUBMITTED.equals(order.getStatus())){
			logger.debug("Process 2c2p charge for user: <{}>, order-id: <{}>, exception: order not in submitted state <{}>", sessionData.getUserId(), order.getOrderId(), order.getStatus());
			throw new AppBadRequestException(ErrorCode.ERR8004_CHARGE_ORDER_NOT_VALID,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		String orderIdMap="";
		if(order.getOrderIdMap()!=null){
			orderIdMap = order.getOrderIdMap();
		}
		else{
			orderIdMap = orderService.generateOrderIdMap();
			order.setOrderIdMap(orderIdMap);
			orderService.updateOrderIdMap(order);
		}
		
		PolicyPayment payment = order.getPayment();
		boolean isFirstPayment = false; 
		if(payment==null){
			isFirstPayment = true;
			payment = new PolicyPayment();
			payment.setId(generatePolicyPaymentId());
			payment.setOrderId(order_Id);
			payment.setUserId(sessionData.getUserId());
			payment.setStartTime(now);
			payment.setChargeTime(now);
			payment.setTotalAmount(order.getTotalPremi());		
			payment.setPaymentSeq(1);
		}else{
			payment.setChargeTime(now);
			payment.setPaymentSeq(payment.getPaymentSeq()+1);
		}
		
		if(isFirstPayment){
			policyPaymentMapper.insertForStatusCharge(payment);
		}else{
			policyPaymentMapper.updateChargeResponseById(payment);
		}
		
		PaymentChargeLog chargeLog = new PaymentChargeLog();
		chargeLog.setChargeDate(LocalDate.now());
		chargeLog.setPolicyPaymentId(payment.getId());
		chargeLog.setPaymentSeq(payment.getPaymentSeq());
		chargeLog.setOrderId(payment.getOrderId());		
		chargeLog.setUserId(payment.getUserId());
		chargeLog.setTotalAmount(payment.getTotalAmount());
		
		paymentChargeLogMapper.insert(chargeLog);
		
		String amount = Payment2c2pUtil.zeroPad(11,String.valueOf(order.getTotalPremi()));
		String paymentDescription = "9Lives Payment Premi"; 
		String valueToDigest =version + merchantId + paymentDescription + orderIdMap +
				currency + amount + resultUrl1 + resultUrl2;
		
		logger.info("value to digest : "+valueToDigest);
		
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("version", version);
		sPara.put("merchant_id", merchantId);
		sPara.put("payment_description", paymentDescription);
		sPara.put("order_id", orderIdMap);
		sPara.put("currency", currency);
		sPara.put("amount", amount);
		sPara.put("result_url_1", resultUrl1);
		sPara.put("result_url_2", resultUrl2);
        
        String hash_value = Payment2c2pUtil.hashSignature(key, valueToDigest);
        sPara.put("hash_value", hash_value);
		List<String> keys = new ArrayList<String>(sPara.keySet());
        
        sbHtml.append("<style>"+
        ".loader {"+
          "border: 16px solid #f3f3f3;"+
          "border-radius: 50%;"+
          "border-top: 16px solid #3498db;"+
          "width: 120px;"+
          "height: 120px;"+
          "-webkit-animation: spin 2s linear infinite; /* Safari *//* "+
          "animation: spin 2s linear infinite;"+
        "}"+
        "@-webkit-keyframes spin {"+
         " 0% { -webkit-transform: rotate(0deg); }"+
          "100% { -webkit-transform: rotate(360deg); }"+
        "}"+

        "@keyframes spin {"+
          "0% { transform: rotate(0deg); }"+
          "100% { transform: rotate(360deg); }"+
        "}"+
        "</style>");
        
        sbHtml.append("<div class='loader' id='loader'></div>");
        sbHtml.append("<script>"+
        	"setTimeout(function unwait() {"+
            "document.getElementById('loader').style.display='none';"+
            "}, 7000);");
        sbHtml.append("</script>");

        sbHtml.append("<div id=\"payform\"><form id=\"paysubmit\" name=\"paysubmit\" action=\"" 
        			  + paymentUrl +
        		      "\" method=\"POST"
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
	    sbHtml.append("<input type=\"submit\" value=\"" + "submit" + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['paysubmit'].submit();</script></div>");
	
		logger.info("Html request : "+sbHtml.toString());
		
		return sbHtml.toString();
	
	}
	

	/*@RequestMapping(value="/pay/{orderId}",method={RequestMethod.GET})	
	@ResponseBody
	public String paymentProvider(
			@PathVariable("orderId") String order_Id
			,@RequestAttribute("authUserId") String authUserId)
			throws AppException{
			
		logger.debug("GET PolicyOrder userid is {} with orderid {}", authUserId, order_Id);
		StringBuffer sbHtml = new StringBuffer();
		LocalDateTime now = LocalDateTime.now();
		PolicyOrder order = orderService.fetchOrderByOrderId(authUserId, order_Id);
		
		if(order==null){
			logger.debug("Process charge for user <{}> result: order not found", authUserId);
			throw new AppBadRequestException(ErrorCode.ERR8003_CHARGE_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}
		
		String orderId="";
		if(Payment2c2pUtil.isStatusAllowedReuse(order.getStatus().name())){
			if(order.getOrderIdMap()!=null){
				orderId = order.getOrderIdMap();
			}
			else{
				orderId = String.valueOf(System.currentTimeMillis()/1000L);
				order.setOrderIdMap(orderId);
				apiOrderService.updatePolicyOrderId2c2p(order);
			}
			
			PolicyPayment payment = order.getPayment();
			boolean isFirstPayment = false; 
			if(payment==null){
				isFirstPayment = true;
				payment = new PolicyPayment();
				payment.setId(generatePolicyPaymentId());
				payment.setOrderId(order_Id);
				payment.setUserId(authUserId);
				payment.setStartTime(now);
				payment.setChargeTime(now);
				payment.setTotalAmount(order.getTotalPremi());		
				payment.setPaymentSeq(1);
			}else{
				payment.setChargeTime(now);
				payment.setPaymentSeq(payment.getPaymentSeq()+1);
			}
			
			if(isFirstPayment){
				policyPaymentMapper.insertForStatusCharge(payment);
			}else{
				policyPaymentMapper.updateChargeResponseById(payment);
			}
		
		}else{
			if(order.getStatus().name().equals("OVERDUE")){
				throw new AppBadRequestException(ErrorCode.ERR7011_CLAIM_EXPIRED_ORDER,
						"Permintaan tidak dapat diproses, order expired");
			}
			else{
				throw new AppBadRequestException(ErrorCode.ERR8003_CHARGE_ORDER_NOT_FOUND,
						"Permintaan tidak dapat diproses, already paid");
			}
			
			
		}
			
		String amount = Payment2c2pUtil.zeroPad(11,String.valueOf(order.getTotalPremi()));
		String paymentDescription = "9Lives Payment Premi"; 
		String valueToDigest =version + merchantId + paymentDescription + orderId +
				currency + amount + resultUrl1 + resultUrl2;
		
		logger.info("value to digest : "+valueToDigest);
		
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("version", version);
		sPara.put("merchant_id", merchantId);
		sPara.put("payment_description", paymentDescription);
		sPara.put("order_id", orderId);
		sPara.put("currency", currency);
		sPara.put("amount", amount);
		sPara.put("result_url_1", resultUrl1);
		sPara.put("result_url_2", resultUrl2);
        
        String hash_value = Payment2c2pUtil.hashSignature(key, valueToDigest);
        sPara.put("hash_value", hash_value);
		List<String> keys = new ArrayList<String>(sPara.keySet());
        
        sbHtml.append("<style>"+
        ".loader {"+
          "border: 16px solid #f3f3f3;"+
          "border-radius: 50%;"+
          "border-top: 16px solid #3498db;"+
          "width: 120px;"+
          "height: 120px;"+
          "-webkit-animation: spin 2s linear infinite; /* Safari */ /*"+
          "animation: spin 2s linear infinite;"+
        "}"+
        "@-webkit-keyframes spin {"+
         " 0% { -webkit-transform: rotate(0deg); }"+
          "100% { -webkit-transform: rotate(360deg); }"+
        "}"+

        "@keyframes spin {"+
          "0% { transform: rotate(0deg); }"+
          "100% { transform: rotate(360deg); }"+
        "}"+
        "</style>");
        
        sbHtml.append("<div class='loader' id='loader'></div>");
        sbHtml.append("<script>"+
        	"setTimeout(function unwait() {"+
            "document.getElementById('loader').style.display='none';"+
            "}, 7000);");
        sbHtml.append("</script>");

        sbHtml.append("<div id=\"payform\"><form id=\"paysubmit\" name=\"paysubmit\" action=\"" 
        			  + "https://demo2.2c2p.com/2C2PFrontEnd/RedirectV3/payment"+
        		      "\" method=\"POST"
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
	    sbHtml.append("<input type=\"submit\" value=\"" + "submit" + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['paysubmit'].submit();</script></div>");
	
	logger.info("Html request : "+sbHtml.toString());
	
	return sbHtml.toString();
	
	}*/
	
	private String generatePolicyPaymentId(){
		return UUID.randomUUID().toString();
	}
	
	
	
	@Override
	public String toString() {
		return "Payment2c2pController [isEnabled=" + isEnabled + ", merchantId=" + merchantId + ", key=" + key
				+ ", version=" + version + ", resultUrl1=" + resultUrl1 + ", resultUrl2=" + resultUrl2 + ", currency="
				+ currency + ", paymentUrl=" + paymentUrl + "]";
	}


	@PostConstruct
	public void init(){
		isEnabled = config.getPayment().getTwoc2pEnable();
		
		if(isEnabled) {
			merchantId = config.getPayment().getTwoc2pMerchantId();
			key = config.getPayment().getTwoc2pKey();
			version = config.getPayment().getTwoc2pVersion();
			resultUrl1 = config.getPayment().getTwoc2pResultUrl1();
			resultUrl2 = config.getPayment().getTwoc2pResultUrl2();
			currency = config.getPayment().getTwoc2pCurrency();
			paymentUrl = config.getPayment().getTwoc2pPaymentUrl();
		}
		
		
		logger.info("Init 2p2p payment-controller with parameter <{}>", toString());
	}
}
