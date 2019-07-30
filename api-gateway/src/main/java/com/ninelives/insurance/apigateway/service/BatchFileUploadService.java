package com.ninelives.insurance.apigateway.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.apigateway.adapter.ModelMapperAdapter;
import com.ninelives.insurance.apigateway.dto.BatchFileUploadDto;
import com.ninelives.insurance.apigateway.dto.RowFile;
import com.ninelives.insurance.apigateway.dto.RowUploadDto;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.BatchFileUploadMapper;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.BatchFileUploadHeader;


@Service
public class BatchFileUploadService {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchFileUploadService.class);
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired BatchFileUploadMapper batchFileUploadMapper;
	
	public List<BatchFileUpload> selectByBatchNumber(String batchNumber){
			return batchFileUploadMapper.selectByBatchNumber(batchNumber,"1");
	}
	
	
	public void save(List<RowFile> validatedRows, String batchNumber, String userName) throws AppException{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Timestamp dt = Timestamp.valueOf(dateFormat.format(date));
		String isValidate="";
		String valCode = "";
		String valMsg = "";
		
		for (RowFile rowFile : validatedRows) {
			try{
				BatchFileUpload batchFileUpload = new BatchFileUpload();
				batchFileUpload.setBatchNumber(batchNumber);
				batchFileUpload.setEmail(rowFile.getEmail());
				batchFileUpload.setNama(rowFile.getName());
				batchFileUpload.setJenisKelamin(rowFile.getGender());
				batchFileUpload.setTanggalLahir(StringUtils.left(rowFile.getBirthDate(),8));
				
				String birthPlace = (rowFile.getBirthPlace().isEmpty()?"Indonesia":rowFile.getBirthPlace());
				batchFileUpload.setTempatLahir(birthPlace);
				
				batchFileUpload.setNoTelpon(rowFile.getPhone());
				batchFileUpload.setKtpNumber(rowFile.getKtpNo());
				batchFileUpload.setCreatedDate(dt);
				
				if(rowFile.getMessage()!=null){
					ArrayList<Entry<String, String>> array = new ArrayList<>();
			        array.addAll(rowFile.getMessage().entrySet()); 	
			        Optional<Entry<String, String>> firstElement = array.stream().findFirst();
			        valCode = firstElement.get().getKey();
			        valMsg = firstElement.get().getValue();
			        if(!valCode.isEmpty() && !valMsg.isEmpty()){
			        	batchFileUpload.setErrorCode(valCode);
			        	batchFileUpload.setResponseMessage(valMsg);
			        }
			        batchFileUpload.setIsValidate("0");
				}else{
					batchFileUpload.setErrorCode("");
		        	batchFileUpload.setResponseMessage("");
		        	batchFileUpload.setIsValidate("1");
				}
				batchFileUpload.setCreatedBy(userName);
				
				batchFileUploadMapper.insert(batchFileUpload);
			
			}catch(Exception e){
				logger.info(e.getMessage());
			}finally {
				continue;
			}
		}
		
		
	}

		
	public BatchFileUpload save(String record, String batchNumber
				, HashMap<String, String> validationLine, String userName)
				throws AppException{
			
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			BatchFileUpload batchFileUpload = new BatchFileUpload();
			Timestamp dt = Timestamp.valueOf(dateFormat.format(date));
			String isValidate="";
			String valCode = "";
			String valMsg = "";
			
			if(validationLine!=null){
				isValidate="0";
				ArrayList<Entry<String, String>> array = new ArrayList<>();
		        array.addAll(validationLine.entrySet()); 	
		        Optional<Entry<String, String>> firstElement = array.stream().findFirst();
		        valCode = firstElement.get().getKey();
		        valMsg = firstElement.get().getValue();
			}else{
				isValidate="1";
			}
		    
			String[] column = record.split(",", -1);
				batchFileUpload.setBatchNumber(batchNumber);
				batchFileUpload.setEmail(column[0]);
				batchFileUpload.setNama(column[1]);
				batchFileUpload.setJenisKelamin(column[2]);
				batchFileUpload.setTanggalLahir(StringUtils.left(column[3],8));
				batchFileUpload.setTempatLahir(column[4]);
				batchFileUpload.setNoTelpon(column[5]);
				batchFileUpload.setKtpNumber(column[6]);
				batchFileUpload.setCreatedDate(dt);
				batchFileUpload.setIsValidate(isValidate);
				if(!valCode.isEmpty() && !valMsg.isEmpty()){
					batchFileUpload.setErrorCode(valCode);
					batchFileUpload.setResponseMessage(valMsg);
				}
				batchFileUpload.setCreatedBy(userName);
			batchFileUploadMapper.insert(batchFileUpload);
			return batchFileUpload;
	}
	
	
	public BatchFileUploadHeader saveHeader(int totalRow, int rowValid, 
			int rowInvalid, String batchNumber, String fileName, String userName, int voucherId)
			throws AppException{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		BatchFileUploadHeader bfuh = new BatchFileUploadHeader();
		Timestamp dt = Timestamp.valueOf(dateFormat.format(date));
		bfuh.setBatchNumber(batchNumber);
		bfuh.setFileName(fileName);
		bfuh.setRowValid(String.valueOf(rowValid));
		bfuh.setRowInvalid(String.valueOf(rowInvalid));
		bfuh.setTotalRow(String.valueOf(totalRow));
		bfuh.setVoucherId(voucherId);
		bfuh.setStatus("on process");
		bfuh.setUploadBegin(dt);
		bfuh.setCreatedDate(dt);
		bfuh.setCreatedBy(userName);
		batchFileUploadMapper.insertHeader(bfuh);
		return bfuh;
	}

	
	public int updateBatchFileUpload(String responseMessage, Long id, String batchNumber,
			String errorCode, String orderId, String userId){
		
		String msg= ((responseMessage.contains(",")) ? responseMessage.replace(",", "") : responseMessage);
		return batchFileUploadMapper.
				updateBatchFileUploadByBatchNumberAndId(msg, id, batchNumber, errorCode, orderId, userId);
	}

	public boolean isUserAlreadyPaid(String ktpNumber,String email, int voucherId){
		boolean rValue = false;
		int countPaid = 0;
		
		if(email==null || email.isEmpty()){
			countPaid = batchFileUploadMapper.selectCountUserPaid(voucherId, ktpNumber);
		}else{
			countPaid = batchFileUploadMapper.selectCountUserPaidByEmail(voucherId, email);
		}
		
		if(countPaid>0){
			rValue = true;
		}
		
		return rValue;
	}

	
	public BatchFileUploadHeader saveHeader(int totalRow, int rowValid, 
			int rowInvalid, String batchNumber, String fileName, String userName)
			throws AppException{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		BatchFileUploadHeader bfuh = new BatchFileUploadHeader();
		Timestamp dt = Timestamp.valueOf(dateFormat.format(date));
		bfuh.setBatchNumber(batchNumber);
		bfuh.setFileName(fileName);
		bfuh.setRowValid(String.valueOf(rowValid));
		bfuh.setRowInvalid(String.valueOf(rowInvalid));
		bfuh.setTotalRow(String.valueOf(totalRow));
		bfuh.setStatus("on process");
		bfuh.setUploadBegin(dt);
		bfuh.setCreatedDate(dt);
		bfuh.setCreatedBy(userName);
		batchFileUploadMapper.insertHeader(bfuh);
		return bfuh;
}
	
	
	/*public int updateBatchFileUpload(String responseMessage, Long id, String batchNumber,
			String errorCode){
		
		String msg= ((responseMessage.contains(",")) ? responseMessage.replace(",", "") : responseMessage);
		return batchFileUploadMapper.
				updateBatchFileUploadByBatchNumberAndId(msg, id , batchNumber, errorCode);
	}
	*/
	
	
	
	public int updateBatchFileUploadHeader(String batchNumber){
		return batchFileUploadMapper.
				updateBatchFileUploadHeaderByBatchNumber(batchNumber);
	}
	
	
	public List<BatchFileUploadDto> getOnProcessList(String uploadDate){
		ArrayList<BatchFileUploadDto> onProcessList = new ArrayList<>();
		List<BatchFileUploadHeader> process = getProcessHeader(uploadDate);
		if(process!=null){
			for(BatchFileUploadHeader po: process){
				BatchFileUploadDto bfu = new BatchFileUploadDto();
				bfu.setBatchNumber(po.getBatchNumber());
				bfu.setFileName(po.getFileName());
				bfu.setRows(po.getTotalRow());
				bfu.setStatus(po.getStatus());
				onProcessList.add(bfu);
			}
		}
		return onProcessList;		
	}
	
	public List<RowUploadDto> getResultBatchUploadList(String batchNumber){
		ArrayList<RowUploadDto> onProcessList = new ArrayList<>();
		List<BatchFileUpload> resultList = 
				batchFileUploadMapper.selectResultByBatchNumber(batchNumber);
			
		if(resultList!=null){
			for(BatchFileUpload po: resultList){
				RowUploadDto bfu = new RowUploadDto();
				bfu = modelMapperAdapter.toDto(po);
				onProcessList.add(bfu);
			}
		}
		return onProcessList;		
	}
	
	public List<BatchFileUploadHeader> getProcessHeader(String uploadDate){
		return batchFileUploadMapper.selectByUploadDate(uploadDate);
	}

	public static String formatTime(String time) {
			long t = Long.valueOf(time);
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(t);
			return formatter.format(calendar.getTime());
	}
}
