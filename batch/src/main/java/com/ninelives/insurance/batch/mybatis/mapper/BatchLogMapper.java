package com.ninelives.insurance.batch.mybatis.mapper;

import com.ninelives.insurance.model.BatchLog;

public interface BatchLogMapper {
    int insertSelective(BatchLog record);

    int updateByPrimaryKeySelective(BatchLog record);
}