package com.ninelives.insurance.apigateway.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ninelives.insurance.apigateway.dto.BatchFileUploadDto;
import com.ninelives.insurance.apigateway.dto.BatchFileUploadListDto;
import com.ninelives.insurance.apigateway.dto.BatchFileUploadWithListDto;
import com.ninelives.insurance.apigateway.dto.OrderDto;
import com.ninelives.insurance.apigateway.dto.RowFile;
import com.ninelives.insurance.apigateway.dto.RowUploadDto;
import com.ninelives.insurance.apigateway.dto.RowUploadListDto;
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
@MultipartConfig
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
	
	
	@RequestMapping(value="/onProcess",	method=RequestMethod.GET)	
	@ResponseBody
	public BatchFileUploadListDto getOnProcessList(
			@RequestParam(value="uploadDate",required=false) String uploadDate){
		
		logger.debug("GET getOnProcessList uploadDate is {} ", uploadDate);
		BatchFileUploadListDto batchFileUploadListDto = new BatchFileUploadListDto();
		List<BatchFileUploadDto> listBatchFileUpload = fileUploadService.getOnProcessList(uploadDate);
		batchFileUploadListDto.setiTotalRecords(listBatchFileUpload.size());
		batchFileUploadListDto.setiTotalDisplayRecords(11);
		batchFileUploadListDto.setsEcho(0);
		batchFileUploadListDto.setsColumns("");
		batchFileUploadListDto.setAaData(listBatchFileUpload);
		
		logger.info("listBatchFileUpload : "+ listBatchFileUpload.toString());
		return batchFileUploadListDto;
	}
	
	
	@RequestMapping(value="/downloadBatchFileUpload",	method=RequestMethod.GET)	
	@ResponseBody
	public RowUploadListDto download(@RequestParam(value="batchNumber",required=false) 
				String batchNumber){
		
		logger.debug("GET getResultBatchUploadList batchNumber is {} ", batchNumber);
		RowUploadListDto rowUploadListDto = new RowUploadListDto();
		List<RowUploadDto> listRowUploadDto = fileUploadService.getResultBatchUploadList(batchNumber);
		rowUploadListDto.setiTotalRecords(listRowUploadDto.size());
		rowUploadListDto.setiTotalDisplayRecords(11);
		rowUploadListDto.setsEcho(0);
		rowUploadListDto.setsColumns("");
		rowUploadListDto.setAaData(listRowUploadDto);
		
		return rowUploadListDto;
	}
	
	
	@SuppressWarnings({ "static-access", "finally" })
	@RequestMapping(value="/uploadBatchFile",method=RequestMethod.POST)
	@ResponseBody
	public BatchFileUploadDto uploadBatchFileNinelives(
			@RequestBody BatchFileUploadWithListDto batchFileUploadWithListDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("BatchFileUploadWithListDto with dto:<{}>", batchFileUploadWithListDto);
		BatchFileUploadDto dt = new BatchFileUploadDto();
		
		String userName = batchFileUploadWithListDto.getUserName();
		String voucherCode = batchFileUploadWithListDto.getCodeB2B();
        String batchNumber = batchFileUploadWithListDto.getBatchNumber();
		Voucher voucher;
		
		if(voucherCode!=null){
			
        	voucher = apiOrderService.validateVoucherB2B(voucherCode, VoucherType.toEnum("B2B"));
			if(voucher!=null){
				
				String validateCodeWithResponseMessage =  
						apiOrderService.validateCodeWithResponseMessage(voucherCode,voucher);
				if(validateCodeWithResponseMessage==null){
					ArrayList<String> listRow = (ArrayList<String>) batchFileUploadWithListDto.getListRow();
					logger.info(batchFileUploadWithListDto.getListRow().toString());
				    
				    if(listRow==null||listRow.isEmpty()){
				    	dt.setCode("File is Empty");
				    	return dt;
					}
				    
				    String fileName = batchFileUploadWithListDto.getFileName();
				    int rowValid=0;
			    	int rowInvalid=0;
			    	int lineNum=0;
				    
				    try {
				    	List<RowFile> validatedRows =  new ArrayList<>();
				    	
				    	for (String string : listRow) {
							if(batchFileUploadValidation.validateFormatRow(string)){
								lineNum++;
								HashMap<String, String> validationLine = validationRow(lineNum,string,voucher.getId());
								if(validationLine==null){rowValid++;
				        		}else{rowInvalid++;}
								
				        		RowFile validateRow =  new RowFile();
		        				setLineToRowFile(validateRow,string,validationLine);
		        				validatedRows.add(validateRow);
			        		}
				    	}
				    	
				    	if(validatedRows.size()==0){
			        		dt.setStatus("no record");
			        	}else{
			        		 checkingIsUniqueKtpNumber(validatedRows);
			        	}
				    	
				    	fileUploadService.save(validatedRows,batchNumber, userName );
	        			
				    	for (RowFile rowFile : validatedRows) {
							if(rowFile.getMessage()!=null){
								rowInvalid++;
								if(rowValid>0){
									rowValid--;
								}
							}
						}
				    	
				    	
				    	fileUploadService.saveHeader(lineNum, rowValid,rowInvalid, batchNumber, fileName, userName, voucher.getId());
				        	
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
											     
											     if(registerResult.getUserDto().getIdCardFile().getFileId()==null){
														logger.debug("Process order for <{}> with result: id card not found", registerResult.getUserDto().getUserId());
														throw new AppBadRequestException(ErrorCode.ERR4017_ORDER_IDCARD_NOTFOUND,
																"Permintaan tidak dapat diproses, unggah KTP Anda untuk melanjutkan pemesanan.");
												 }	
											
											     OrderDto orderDto = new OrderDto();
											   	 orderDto = apiOrderService.submitOrder(registerResult, voucherCode);
											   	 logger.info(orderDto.toString());
											   	 if(orderDto!=null){
											   		String orderId = orderDto.getOrderId();
											   		String userId = orderDto.getUser().getUserId();
											   		fileUploadService.updateBatchFileUpload(orderDto.getStatus().name(), 
											   				bfu.getFileId(), batchNumber,"",orderId,userId);
											   	 }
											     continue;
								 			   
										   }catch (AppException e){
											   fileUploadService.updateBatchFileUpload(e.getMessage(), bfu.getFileId(), 
													   batchNumber, e.getCode().toString(),"","");
											   logger.info(e.getMessage());
										   }finally {
						        				continue;
						        		 }
										     
									   }  
									   
									   fileUploadService.updateBatchFileUploadHeader(batchNumber);  
										
								}
							}.start();
							
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					dt.setCode(validateCodeWithResponseMessage);
				}
				
				
			    
			
			}
			else{
				dt.setCode("B2B Code Invalid");
			}
		}
	    
		logger.debug("POST Batch File Upload with request {} and validate-only {}");
		
		return dt;
		
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkingIsUniqueKtpNumber(List<RowFile> validatedRows) {
		RowFile rc =null;
        HashSet data = new HashSet();
        List<String> ktpId = new ArrayList<>();
        
        HashSet emailHash = new HashSet();
        List<String> email = new ArrayList<>();
        
        
		for (int i = 0; i < validatedRows.size(); i++) {
			rc = (RowFile) validatedRows.get(i);
			if (!data.add(rc.getKtpNo().trim())){
				ktpId.add(rc.getKtpNo().trim());
	        }
			if (!emailHash.add(rc.getEmail().trim())){
				email.add(rc.getEmail().trim());
	        }
			
		}
	
		for (int i = 0; i < ktpId.size(); i++) {
			for (RowFile rowFile : validatedRows) {
				if(rowFile.getKtpNo().trim().equals(ktpId.get(i).trim())){
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("ER017", "Multiple KTP number");
					rowFile.setMessage(hm);
				}
				
			}
			
		}
		
		for (int i = 0; i < email.size(); i++) {
			for (RowFile rowFile : validatedRows) {
				if(rowFile.getEmail().trim().equals(email.get(i).trim())){
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("ER017", "Multiple Email");
					rowFile.setMessage(hm);
				}
				
			}
			
		}
		
	}

	private void setLineToRowFile(RowFile validateRow, String lineToUpload, HashMap<String, String> validationLine) {
		String[] column = lineToUpload.split(",");
		validateRow.setEmail(column[0]);
		validateRow.setName(column[1]);
		validateRow.setGender(column[2]);
		validateRow.setBirthDate(column[3]);
		validateRow.setBirthPlace(column[4]);
		validateRow.setPhone(column[5]);
		validateRow.setKtpNo(column[6]);
		if(validationLine!=null){
			validateRow.setIsValid("1");
			validateRow.setMessage(validationLine);
		}
		
	}

	@SuppressWarnings("static-access")
	private HashMap<String, String> validationRow(int lineNum, String lineToUpload, int voucherId) {
		
		HashMap<String, String> rValue=null;
		String[] column = lineToUpload.split(",",-1);
		HashMap<String, String> hm = new HashMap<String, String>();
		
		if(column[0].trim().length()>0){
			if(!batchFileUploadValidation.validateEmail(column[0].trim())){
				hm.put("ER002", "Invalid email");
			}else{
				if(fileUploadService.isUserAlreadyPaid(column[6].trim(),column[0].trim(),voucherId)){
					hm.put("ER016","ERROR_ALREADY_PROCESS");
				}
			}
		}
		
		if(!batchFileUploadValidation.validateSpecialCharacters(column[1].trim())){
			hm.put("ER003", "Invalid name");
		}
		
		if(!column[2].trim().equalsIgnoreCase("M") && !column[2].trim().equalsIgnoreCase("F")){
			hm.put("ER014","Invalid gender (M/F) only");
		}
		
		if(!batchFileUploadValidation.isValidDate(column[3].trim())){
			hm.put("ER004", "Invalid date format (correct format yyyyMMdd)");
		}
		else{
			int age = batchFileUploadValidation.getAge(column[3].trim());
			if(age<17){
				hm.put("ER005","Invalid Age < 17 ( "+age +" years old )");
			}
			if(age>60){
				hm.put("ER005","Invalid Age > 60 ( "+age +" years old )");
			}
			
		}
		
		if(column[4].trim()!=null && !column[4].isEmpty()){
			if(!batchFileUploadValidation.validateLetters(column[4].trim())){
				hm.put("ER015","Invalid Birth Place");
			}
		}
		
		if(!batchFileUploadValidation.validateNumeric(column[5].trim())){
			hm.put("ER006","Invalid Phone Number");
		}
		
		if(column[6]!=null && !column[6].isEmpty()){
			if(column[6].trim().length()!=16){
				hm.put("ER007","Invalid KTP Number");	
			}
			else{
				if(!batchFileUploadValidation.validateNumeric(column[6].trim())){
					hm.put("ER007","Invalid KTP Number");	
				}
				else{
					if(fileUploadService.isUserAlreadyPaid(column[6].trim(),column[0].trim(),voucherId)){
						hm.put("ER016","ERROR_ALREADY_PROCESS");
					}
				}
			}
			
		}
		
		if(!hm.isEmpty()){
			rValue=hm;
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


/*package com.ninelives.insurance.apigateway.controller;

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
	
	@SuppressWarnings({ "static-access", "finally" })
	@RequestMapping(value="/upload", method=RequestMethod.POST)	
	@ResponseBody
	public BatchFileUploadDto uploadBatchFile(HttpServletRequest request, 
			HttpServletResponse response) throws AppException{
		
		BatchFileUploadDto dt = new BatchFileUploadDto();
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
			        	
			        	//br.readLine(); 
			        	String lineToUpload=null;
			        	while ((lineToUpload = br.readLine()) != null){
			        		
//			        		String lastChar = lineToUpload.substring(lineToUpload.length()-1);
//			        		if(lastChar.equals(",")){
//			        			lineToUpload = lineToUpload+null;
//			        		}
			        		
			        		if(batchFileUploadValidation.validateFormatRow(lineToUpload)){
			        			lineNum++;
		        				HashMap<String, String> validationLine = validationRow(lineNum,lineToUpload);
				        		if(validationLine==null){rowValid++;}
			        				else{rowInvalid++;}
			        			
				        		try{
				        			fileUploadService.save(lineToUpload,  
		        							batchNumber,validationLine, userName );
			        			}catch(Exception e){
			        				logger.info("Exception on processing row <{}> with message <{}>", lineNum, e.getMessage());
			        			}finally {
			        				continue;
			        			}
				        		
			        		}
			        		
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
									   }finally {
					        				continue;
					        		 }
									     
								   }  
								   
								   fileUploadService.updateBatchFileUploadHeader(batchNumber);  
									
							}
						}.start();
						
						
						
					
					} catch (IOException e) {
			            e.printStackTrace();
			        }
					
					conFile.delete();
			    
				} catch (IOException e) {
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
	private HashMap<String, String> validationRow(int lineNum, String lineToUpload) {
		HashMap<String, String> rValue=null;
		String[] column = lineToUpload.split(",",-1);
		HashMap<String, String> hm = new HashMap<String, String>();
		
		if(column[0].trim().length()>0){
			if(!batchFileUploadValidation.validateEmail(column[0].trim())){
				hm.put("ER002", "Invalid email");
			}
		}		
		if(!batchFileUploadValidation.validateSpecialCharacters(column[1].trim())){
			hm.put("ER003", "Invalid name");
		}		
		if(!column[2].trim().equalsIgnoreCase("M")&&
				!column[2].trim().equalsIgnoreCase("F")){
			hm.put("ER014","Invalid gender (M/F) only");
		}
		if(!batchFileUploadValidation.isValidDate(column[3].trim())){
			hm.put("ER004", "Invalid date format (correct format yyyyMMdd)");
		}
		else{
			
			int age = batchFileUploadValidation.getAge(column[3].trim());
			if(age<17){
				hm.put("ER005","Invalid Age < 17 ( "+age +" years old )");
			}
			if(age>60){
				hm.put("ER005","Invalid Age > 60 ( "+age +" years old )");
			}
			
		}
		
		if(column[4].trim()!=null && !column[4].isEmpty()){
			if(!batchFileUploadValidation.validateLetters(column[4].trim())){
				hm.put("ER015","Invalid Birth Place");
			}
		}
		if(!batchFileUploadValidation.validateNumeric(column[5].trim())){
			hm.put("ER006","Invalid Phone Number");
		}
		
		if(column[6]!=null && !column[6].isEmpty()){
			if(column[6].trim().length()!=16){
				hm.put("ER007","Invalid KTP Number");	
			
			}
			else{
				if(!batchFileUploadValidation.validateNumeric(column[6].trim())){
					hm.put("ER007","Invalid KTP Number");	
				}
			}
		}
		
		
		if(!hm.isEmpty()){
			rValue=hm;
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
	
	
}*/
