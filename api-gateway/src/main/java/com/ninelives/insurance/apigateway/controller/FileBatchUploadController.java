package com.ninelives.insurance.apigateway.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ninelives.insurance.apigateway.dto.BatchFileUploadDto;
import com.ninelives.insurance.apigateway.dto.OrderDto;
import com.ninelives.insurance.apigateway.dto.RowUploadDto;
import com.ninelives.insurance.apigateway.model.RegisterUsersResult;
import com.ninelives.insurance.apigateway.service.ApiOrderService;
import com.ninelives.insurance.apigateway.service.ApiUserService;
import com.ninelives.insurance.apigateway.service.BatchFileUploadService;
import com.ninelives.insurance.apigateway.util.BatchFileUploadValidation;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.VoucherType;

@Controller
@RequestMapping("/api-gateway")
public class FileBatchUploadController {
	private static final Logger logger = LoggerFactory
			.getLogger(FileBatchUploadController.class);
			
	@Autowired 
	ApiOrderService apiOrderService;
	
	@Autowired 
	BatchFileUploadService fileUploadService;
	
	@Autowired 
	ApiUserService apiUserService;
	
	private BatchFileUploadValidation batchFileUploadValidation;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)	
	@ResponseBody
	public BatchFileUploadDto uploadBatchFile(HttpServletRequest request, 
			HttpServletResponse response) throws AppException{
		
		BatchFileUploadDto dt = new BatchFileUploadDto();
		//HttpSession session = request.getSession();
		String userName = request.getParameter("userName");
		String voucherCode = request.getParameter("b2bcode");
        Voucher voucher;
		
        if(voucherCode!=null){
			
        	voucher = apiOrderService.validateVoucherB2B(voucherCode, VoucherType.toEnum("B2B"));
			if(voucher!=null){
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Iterator<String> itr =  multipartRequest.getFileNames();
			    MultipartFile file = multipartRequest.getFile(itr.next());
			    if(file==null||file.getSize()<=0){
					throw new AppBadRequestException(ErrorCode
							.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, "
									+ "periksa kembali file yang akan Anda unggah");
				}
			    
			    String fileName = file.getOriginalFilename();
			    try {
					File conFile =convert(file);
					try (BufferedReader br = new BufferedReader(new FileReader(conFile))) {
			        	int lineNum=0;
			        	int rowValid=0;
			        	int rowInvalid=0;
			        	String batchNumber = new SimpleDateFormat("yyyyMMdd_HHmmss")
			        			.format(Calendar.getInstance().getTime());
			        	
			        	br.readLine(); 
			        	String lineToUpload=null;
			        	while ((lineToUpload = br.readLine()) != null){
			        		lineNum++;
	        				String validationLine = validation(lineNum,lineToUpload);
		        				if(validationLine==null){rowValid++;}
		        				else{rowInvalid++;}
		        				
	            				fileUploadService.save(lineToUpload,  
	        							batchNumber,validationLine, userName );
			        	}
			        	
			        	fileUploadService.saveHeader(lineNum, rowValid, 
			        			rowInvalid, batchNumber, fileName, userName);
			        	
			        	dt.setBatchNumber(batchNumber);
			        	dt.setRows(String.valueOf(lineNum));
			        	dt.setFileName(fileName);
			        	dt.setStatus("on process");
			        	
			        	new Thread() {
							public void run() {
								   List<BatchFileUpload> bfus = fileUploadService.selectByBatchNumber(batchNumber);
								   for (BatchFileUpload bfu : bfus) {
									   try{
										     RegisterUsersResult registerResult = new RegisterUsersResult();
										     registerResult = apiUserService.registerUserByWithoutEmailValidation(bfu);
										
										     System.out.println("===>"+registerResult.getUserDto().getUserId());
										     OrderDto orderDto = new OrderDto();
										   	 orderDto = apiOrderService.submitOrder(registerResult, voucherCode);
										   	 if(orderDto!=null){
										   		fileUploadService.updateBatchFileUpload(orderDto.getStatus().name(), 
										   				bfu.getFileId(), batchNumber,"");
										   	 }
										   	continue;
										   
									   }catch (AppException e){
										   fileUploadService.updateBatchFileUpload(e.getMessage(), bfu.getFileId(), 
												   batchNumber, e.getCode().toString());
										   logger.info(e.getMessage());
									   }//finally {
										 //  continue;
								       //}
									     
								   }  
								   
								   fileUploadService.updateBatchFileUploadHeader(batchNumber);  
									
							}
						}.start();
			        	
			        
					
					} catch (IOException e) {
			            e.printStackTrace();
			        }
			    
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else{
				dt.setCode("B2B Code Invalid");
			}
		}
	    
		logger.debug("POST Batch File Upload with request {} and validate-only {}");
		
		return dt;
		
	}
	
	
	@RequestMapping(value="/onprocess",	method=RequestMethod.GET)	
	@ResponseBody
	public List<BatchFileUploadDto> getOnProcessList(
			@RequestParam(value="uploadDate",required=false) String uploadDate){
		
		logger.debug("GET getOnProcessList uploadDate is {} ", uploadDate);
		return fileUploadService.getOnProcessList(uploadDate);
	}
	
	@RequestMapping(value="/download",	method=RequestMethod.GET)	
	@ResponseBody
	public List<RowUploadDto> download(@RequestParam(value="batchNumber",required=false) 
				String batchNumber){
		
		logger.debug("GET getResultBatchUploadList batchNumber is {} ", batchNumber);
		return fileUploadService.getResultBatchUploadList(batchNumber);
	}
	
	
	
	@SuppressWarnings("static-access")
	private String validation(int lineNum, String lineToUpload) {
		String rValue=null;
		String[] column = lineToUpload.split(",");
		HashMap<String, String> hm = new HashMap<String, String>();
		
		if(column[0].trim().length()>0){
			if(!batchFileUploadValidation.validateEmail(column[0].trim())){
				hm.put("invalid email", column[0]);
			}
		}
		
		if(!batchFileUploadValidation.validateSpecialCharacters(column[1].trim())){
			hm.put("invalid name format", column[1]);
		}
		if(!column[2].trim().equalsIgnoreCase("M")&&
				!column[2].trim().equalsIgnoreCase("F")){
			hm.put("invalid jenis kelamin", column[2]);
		}
		if(!batchFileUploadValidation.isValidDate(column[3].trim())){
			hm.put("invalid tanggal lahir format", column[3]);
		}
		else{
			
			int age = batchFileUploadValidation.getAge(column[3].trim());
			if(age<17){
				hm.put("invalid Age < 17 ( "+age +" years old )", column[3]);
			}
			if(age>60){
				hm.put("invalid Age > 60 ( "+age +" years old )", column[3]);
			}
			
		}
		
		if(column[4].trim()!=null && !column[4].isEmpty()){
			if(!batchFileUploadValidation.validateLetters(column[4].trim())){
				hm.put("invalid tempat lahir", column[4]);
			}
		}
		if(!batchFileUploadValidation.validateNumeric(column[5].trim())){
			hm.put("invalid no telpon", column[5]);
		}
		if(column[6].trim().length()!=16){
				hm.put("invalid ktp number length", column[6]);	
			
		}
		else{
			if(!batchFileUploadValidation.validateNumeric(column[6].trim())){
				hm.put("invalid ktp non numeric", column[6]);	
			}
		}
		
		if(!hm.isEmpty()){
			rValue=hm.toString().replace("{","").replace("}", "");
		}
		
		return rValue;
	
	}
	
	
	public File convert(MultipartFile file) throws IOException
	{    
	    File convFile = new File(file.getOriginalFilename());
	    convFile.createNewFile(); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    return convFile;
	}
	
	
}
