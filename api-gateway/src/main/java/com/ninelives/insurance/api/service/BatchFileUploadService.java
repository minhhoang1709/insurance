package com.ninelives.insurance.api.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.BatchFileUploadDto;
import com.ninelives.insurance.api.dto.RowUploadDto;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.BatchFileUploadMapper;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.BatchFileUploadHeader;


@Service
public class BatchFileUploadService {
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired BatchFileUploadMapper batchFileUploadMapper;
	
	public List<BatchFileUpload> selectByBatchNumber(String batchNumber){
			return batchFileUploadMapper.selectByBatchNumber(batchNumber,"1");
	}
		
	public BatchFileUpload save(String record, String batchNumber
				, String validationLine, String userName)
				throws AppException{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			BatchFileUpload batchFileUpload = new BatchFileUpload();
			Timestamp dt = Timestamp.valueOf(dateFormat.format(date));
			
			String isValidate= ((validationLine == null) ? "1" : "0");
			String[] column = record.split(",");
				batchFileUpload.setBatchNumber(batchNumber);
				batchFileUpload.setEmail(column[0]);
				batchFileUpload.setNama(column[1]);
				batchFileUpload.setJenisKelamin(column[2]);
				batchFileUpload.setTanggalLahir(column[3]);
				batchFileUpload.setTempatLahir(column[4]);
				batchFileUpload.setNoTelpon(column[5]);
				batchFileUpload.setKtpNumber(column[6]);
				batchFileUpload.setCreatedDate(dt);
				batchFileUpload.setIsValidate(isValidate);
				batchFileUpload.setValidationStatus(validationLine);
				batchFileUpload.setCreatedBy(userName);
			batchFileUploadMapper.insert(batchFileUpload);
			return batchFileUpload;
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
	
	
	public int updateBatchFileUpload(String responseMessage, Long id, String batchNumber,
			String errorCode){
		return batchFileUploadMapper.
				updateBatchFileUploadByBatchNumberAndId(responseMessage, id , batchNumber, errorCode);
	}
	
	
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
