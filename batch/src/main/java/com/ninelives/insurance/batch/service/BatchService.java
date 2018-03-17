package com.ninelives.insurance.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.batch.mybatis.mapper.BatchLogMapper;
import com.ninelives.insurance.model.BatchLog;

@Service
public class BatchService {
	@Autowired 
	BatchLogMapper batchLogMapper;
	
	public int registerBatchLog(BatchLog batchLog){
		return batchLogMapper.insertSelective(batchLog);
	}
	public int updateBatchLog(BatchLog batchLog){
		return batchLogMapper.updateByPrimaryKeySelective(batchLog);
	}
}
