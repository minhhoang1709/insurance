package com.ninelives.insurance.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.service.ApiOrderService;
import com.ninelives.insurance.api.util.Payment2c2pUtil;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class PaymentProviderController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentProviderController.class);
	
	@Autowired ApiOrderService apiOrderService;
	
	@Value("${ninelives.payment.2c2p-merchant-id}")
	private String merchantId;
	
	@Value("${ninelives.payment.2c2p-key}")
	private String key;
	
	@Value("${ninelives.payment.2c2p-version}")
	private String version;
	
	@Value("${ninelives.payment.2c2p-resultUrl1}")
	private String resultUrl1;
	
	@Value("${ninelives.payment.2c2p-resultUrl2}")
	private String resultUrl2;
	
	@Value("${ninelives.payment.2c2p-currency}")
	private String currency;
	
	
	@RequestMapping(value="/resultPayment/2c2p",method=RequestMethod.POST)
	@ResponseBody
	public String resultPayment2c2p(
			HttpServletRequest request, 
			HttpServletResponse response,
			Model model ) throws AppException{
		
		JSONObject obj = new JSONObject();
		try {
			obj.put("request_timestamp", request.getParameter("request_timestamp"));
			obj.put("merchant_id", request.getParameter("merchant_id"));
			obj.put("currency", request.getParameter("currency"));
			obj.put("order_id", request.getParameter("order_id"));
			obj.put("amount", request.getParameter("amount"));
			obj.put("invoice_no", request.getParameter("invoice_no"));
			obj.put("transaction_ref", request.getParameter("transaction_ref"));
			obj.put("approval_code", request.getParameter("approval_code"));
			obj.put("eci", request.getParameter("eci"));
			obj.put("transaction_datetime", request.getParameter("transaction_datetime"));
			obj.put("payment_channel", request.getParameter("payment_channel"));
			obj.put("payment_status", request.getParameter("payment_status"));
			obj.put("channel_response_code", request.getParameter("channel_response_code"));
			obj.put("channel_response_desc", request.getParameter("channel_response_desc"));
			obj.put("masked_pan", request.getParameter("masked_pan"));
			obj.put("payment_scheme", request.getParameter("payment_scheme"));
			obj.put("hash_value", request.getParameter("hash_value"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String baseString = request.getParameter("version")+request.getParameter("request_timestamp")+
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
		
		if(Payment2c2pUtil.checkSignature(key,hash_value,baseString)){
			model.addAttribute("responsePayment", obj );
			StringBuffer sbHtml = new StringBuffer();
			sbHtml.append("<html>");
			sbHtml.append("<head>");
			sbHtml.append("<title>payment2c2p</title>");
			sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
			sbHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
			sbHtml.append(Payment2c2pUtil.getButtonHtml());
			sbHtml.append("</head>");
			sbHtml.append("<body>");
			sbHtml.append("<h2>Payment Status : "+request.getParameter("payment_status")+"</h2>");
			sbHtml.append("Order Id : "+request.getParameter("order_id")+"<p>");
			sbHtml.append("<button class=\"button\" style=\"vertical-align:middle\"><span>Finish </span></button>");
			sbHtml.append("</body>");
			sbHtml.append("</html>");
			
			return sbHtml.toString();
		}
		
		model.addAttribute("messageError", "Invalid Hash Value" );
		return "resultpaymentfail";
	
	}
	
	

	@RequestMapping(value="/pay/{orderId}",method={RequestMethod.GET})	
	@ResponseBody
	public String paymentProvider(
			@PathVariable("orderId") String order_Id
			,@RequestAttribute("authUserId") String authUserId)
			throws AppException{
			
		logger.debug("GET PolicyOrder userid is {} with orderid {}", authUserId, order_Id);
		StringBuffer sbHtml = new StringBuffer();
		OrderDto orderDto = apiOrderService.getOrderDtoByOrderId(order_Id,authUserId);
		if(orderDto==null){
			throw new AppNotFoundException(ErrorCode.ERR5001_ORDER_NOT_FOUND,"Transaksi tidak ditemukan");
		}
		else{
			
			String orderId="";
			
			if(orderDto.getStatus().equals("PAID")){
				throw new AppNotFoundException(ErrorCode.ERR1001_GENERIC_ERROR,"Transaksi sudah terbayar");
			}else{
				if(Payment2c2pUtil.isStatusAllowedReuse(orderDto.getStatus().name())){
					if(orderDto.getOrderIdMap()!=null){
						orderId = orderDto.getOrderIdMap();
					}
					else{
						orderId = String.valueOf(System.currentTimeMillis()/1000L);
						
						PolicyOrder policyOrder = apiOrderService.getPolicyOrderByOrderId(order_Id, authUserId);
						policyOrder.setOrderIdMap(orderId);
						apiOrderService.updatePolicyOrderId2c2p(policyOrder);
					}
				}
				
			}
			
			String amount = Payment2c2pUtil.zeroPad(11,String.valueOf(orderDto.getTotalPremi()));
			String paymentDescription = "9Lives Payment Premi"; //
			String valueToDigest =version + merchantId + paymentDescription + orderId +
					currency + amount + resultUrl1 + resultUrl2;
			
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
	          "-webkit-animation: spin 2s linear infinite; /* Safari */"+
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

		}
		
		return sbHtml.toString();
	
	}
	
	
}
