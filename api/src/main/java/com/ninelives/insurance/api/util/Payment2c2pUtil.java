package com.ninelives.insurance.api.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

public class Payment2c2pUtil {
	
	public Payment2c2pUtil(){
		
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
	
	public static String zeroPad(int length, String field){
        String rSpasi;
        String spasi="0";
        for(int i=1;i<=length-field.length();i++){            
            spasi = spasi +"0";            
        }
        rSpasi = spasi+field;
        return rSpasi;
    }
	
	
	public static String getButtonHtml(){
		String rValue="";
		
		rValue="<style> "+
				".button {display: inline-block;border-radius: 4px;background-color: purple;border: none;color: #FFFFFF;"+
				"text-align: center;font-size: 28px;padding: 20px;width: 200px;transition: all 0.5s;cursor: pointer;margin: 5px;}"+
				".button span {cursor: pointer;display: inline-block;position: relative;transition: 0.5s;}"+
				".button span:after {content: '';position: absolute;opacity: 0;top: 0;right: -20px;transition: 0.5s;}"+
				".button:hover span {padding-right: 25px;}"+
				".button:hover span:after {opacity: 1;right: 0;}"+
				"</style>";
		
		return rValue;
	
	}
	
	public static boolean isStatusAllowedReuse(String status){ 
		boolean rValue=false;
	
		if(status.equals("INPAYMENT")){
     		rValue=true;
		}
		if(status.equals("SUBMITTED")){
     		rValue=true;
		}
	    
		return rValue;
	  
	} 
	
	public static String getPaymentStatusResponseDescription(String paymentCode){
		String rValue="";
		if(paymentCode.equals("000")){
			rValue="success";
		}else 
		if(paymentCode.equals("001")){
			rValue="Payment Pending";
		}else 
		if(paymentCode.equals("002")){
			rValue="Payment Rejected";
		}else 
		if(paymentCode.equals("003")){
			rValue="Payment was canceled by user";
		}else 
		if(paymentCode.equals("999")){
			rValue="Payment Failed";
		}		
		
		return rValue;
	}

	public static String getBaseString(HttpServletRequest request) {
		String rValue="";
		
		rValue = request.getParameter("version")+request.getParameter("request_timestamp")+
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
		
		return rValue;
	}

	public static String getSuccessPage(String orderId2c2p, String contextPath) {
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<title>Payment Success 2C2P</title>");
		sbHtml.append("<meta charset=\"UTF-8\">");
		sbHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		sbHtml.append("<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>");
		sbHtml.append("<style>");
		sbHtml.append(".center {height: 200px;position: relative;}");
		sbHtml.append(".center p {margin: 0;position: absolute;top: 170%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: white;font-size: 18px;font-family: 'Roboto';}");
		sbHtml.append(".center h1 {margin: 0;position: absolute;top: 180%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: #333333;font-size: 14px;font-family: 'Roboto';}");
		sbHtml.append(".center button {margin: 0;position: absolute;top: 250%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);border-radius: 8px;}");
		sbHtml.append(".center img {margin: 0;position: absolute;top: 120%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);}");
		sbHtml.append(".block {display: block;width: 50%;border: none;background-color: #FFFFFF;color: #2fae65;");
		sbHtml.append("padding: 14px 28px;font-size: 16px;cursor: pointer;text-align: center;font-weight: bold;}");
		sbHtml.append(".block:hover {background-color: #ddd;color: black;font-weight: bold;}");
		sbHtml.append("</style>");
		sbHtml.append("</head>");
		sbHtml.append("<body style=\"background-color:#2fae65;\">");
		sbHtml.append("<div class=\"center\">");
		sbHtml.append("<img src='"+contextPath+"/image/icon_success.png' alt='image' class=\"icon\"><br>");
		sbHtml.append(" <p>Payment Successful</p>");
		sbHtml.append(" <h1>ORDER ID : "+orderId2c2p+"</h1>");
		sbHtml.append("<button class=\"block\" onclick=\"location.href='/api/orders/"+orderId2c2p+"/payfinish?result=success'\">OK</button>");
		sbHtml.append("</div>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		
		return sbHtml.toString();
	
	}

	public static String getFailedPage(String orderId2c2p, String contextPath, String resultPay) {
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<title>Payment Failed 2C2P</title>");
		sbHtml.append("<meta charset=\"UTF-8\">");
		sbHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		sbHtml.append("<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>");
		sbHtml.append("<style>");
		sbHtml.append(".center {height: 200px;position: relative;}");
		sbHtml.append(".center p {margin: 0;position: absolute;top: 170%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: white;font-size: 18px;font-family: 'Roboto';}");
		sbHtml.append(".center h1 {margin: 0;position: absolute;top: 180%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: #333333;font-size: 14px;font-family: 'Roboto';}");
		sbHtml.append(".center button {margin: 0;position: absolute;top: 250%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);border-radius: 8px;}");
		sbHtml.append(".center img {margin: 0;position: absolute;top: 120%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);}");
		sbHtml.append(".block {display: block;width: 50%;border: none;background-color: #FFFFFF;color: #dc626a;");
		sbHtml.append("padding: 14px 28px;font-size: 16px;cursor: pointer;text-align: center;font-weight: bold;}");
		sbHtml.append(".block:hover {background-color: #ddd;color: black;font-weight: bold;}");
		sbHtml.append("</style>");
		sbHtml.append("</head>");
		sbHtml.append("<body style=\"background-color:#e3636b;\">");
		sbHtml.append("<div class=\"center\">");
		sbHtml.append("<img src='"+contextPath+"/image/icon_fail.png' alt='image' class=\"icon\"><br>");
		sbHtml.append(" <p>"+resultPay+"</p>");
		sbHtml.append(" <h1>ORDER ID : "+orderId2c2p+"</h1>");
		sbHtml.append("<button class=\"block\" onclick=\"location.href='/api/orders/"+orderId2c2p+"/payfinish?result=failed'\">TRY AGAIN</button>");
		sbHtml.append("</div>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
	
		return sbHtml.toString();
	}

	public static String getInvalidHashPage(String orderId2c2p, String contextPath) {
		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<title>Payment Failed 2C2P</title>");
		sbHtml.append("<meta charset=\"UTF-8\">");
		sbHtml.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
		sbHtml.append("<link href='https://fonts.googleapis.com/css?family=Roboto' rel='stylesheet'>");
		sbHtml.append("<style>");
		sbHtml.append(".center {height: 200px;position: relative;}");
		sbHtml.append(".center p {margin: 0;position: absolute;top: 170%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: white;font-size: 18px;font-family: 'Roboto';}");
		sbHtml.append(".center h1 {margin: 0;position: absolute;top: 180%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);color: #333333;font-size: 14px;font-family: 'Roboto';}");
		sbHtml.append(".center button {margin: 0;position: absolute;top: 250%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);border-radius: 8px;}");
		sbHtml.append(".center img {margin: 0;position: absolute;top: 120%;left: 50%;-ms-transform: translate(-50%, -50%);");
		sbHtml.append("transform: translate(-50%, -50%);}");
		sbHtml.append(".block {display: block;width: 50%;border: none;background-color: #FFFFFF;color: #dc626a;");
		sbHtml.append("padding: 14px 28px;font-size: 16px;cursor: pointer;text-align: center;font-weight: bold;}");
		sbHtml.append(".block:hover {background-color: #ddd;color: black;font-weight: bold;}");
		sbHtml.append("</style>");
		sbHtml.append("</head>");
		sbHtml.append("<body style=\"background-color:#e3636b;\">");
		sbHtml.append("<div class=\"center\">");
		sbHtml.append("<img src='"+contextPath+"/image/icon_fail.png' alt='image' class=\"icon\"><br>");
		sbHtml.append(" <p>Invalid Hash Value</p>");
		sbHtml.append(" <h1>ORDER ID : "+orderId2c2p+"</h1>");
		sbHtml.append("<button class=\"block\" onclick=\"location.href='/api/orders/"+orderId2c2p+"/payfinish?result=failed'\">TRY AGAIN</button>");
		sbHtml.append("</div>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		return sbHtml.toString();
	
	}
	
	
}