package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.BatchFileUploadHeader;

@Mapper
public interface BatchFileUploadMapper {
    
	@Insert({
        "insert into public.batch_file_upload (batch_number, ",
        "email, nama, ",
        "jenis_kelamin, tanggal_lahir, ",
        "tempat_lahir, no_telpon, ",
        "ktp_number, status, ",
        "response_message, validation, created_date, ",
        "created_by, modified_date,modified_by, error_code) ",
        "values (#{batchNumber,jdbcType=VARCHAR}, ",
        "#{email,jdbcType=VARCHAR}, #{nama,jdbcType=VARCHAR}, ",
        "#{jenisKelamin,jdbcType=VARCHAR}, #{tanggalLahir,jdbcType=VARCHAR}, ",
        "#{tempatLahir,jdbcType=VARCHAR}, #{noTelpon,jdbcType=VARCHAR}, ",
        "#{ktpNumber,jdbcType=VARCHAR}, #{validationStatus,jdbcType=VARCHAR}, ",
        "#{responseMessage,jdbcType=VARCHAR},#{isValidate,jdbcType=VARCHAR}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{createdBy,jdbcType=VARCHAR}, #{modifiedDate,jdbcType=TIMESTAMP}, ",
        "#{modifiedBy,jdbcType=VARCHAR},#{errorCode,jdbcType=VARCHAR})"
    })
    int insert(BatchFileUpload record);
	
	@Insert({
        "insert into public.batch_file_upload_header (batch_number, filename, row_valid, ",
        "row_invalid, total_row, ",
        "status, upload_begin, ",
        "upload_end, created_date, ",
        "created_by, modified_date,modified_by) ",
        "values (#{batchNumber,jdbcType=VARCHAR}, #{fileName,jdbcType=VARCHAR}, #{rowValid,jdbcType=VARCHAR}, ",
        "#{rowInvalid,jdbcType=VARCHAR}, #{totalRow,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{uploadBegin,jdbcType=TIMESTAMP}, ",
        "#{uploadEnd,jdbcType=TIMESTAMP}, #{createdDate,jdbcType=TIMESTAMP}, ",
        "#{createdBy,jdbcType=VARCHAR}, #{modifiedDate,jdbcType=TIMESTAMP}, ",
        "#{modifiedBy,jdbcType=VARCHAR})"
    })
    int insertHeader(BatchFileUploadHeader record);
	
	
	List<BatchFileUpload> selectByBatchNumber(@Param("batchNumber") String batchNumber,
			@Param("isValidate") String isValidate);
	
	@Update({
	        "update public.batch_file_upload_header ",
	        "set status = 'done', ",
	        "modified_date = now(), upload_end = now(), ",
	        "modified_by = 'system' ",
	        "where batch_number = #{batchNumber,jdbcType=VARCHAR} "
	})
	int updateBatchFileUploadHeaderByBatchNumber(@Param("batchNumber") String batchNumber);
	
	@Update({
        "update public.batch_file_upload ",
        "set response_message = #{responseMessage,jdbcType=VARCHAR}, ",
            "error_code = #{errorCode,jdbcType=VARCHAR}, ",
        	"modified_date = now(), ",
        	"modified_by = 'system' ",
        "where batch_file_upload_id = #{fileId,jdbcType=BIGINT} ",
        "and batch_number = #{batchNumber,jdbcType=VARCHAR} "
    })
    int updateBatchFileUploadByBatchNumberAndId(@Param("responseMessage") String responseMessage, 
		@Param("fileId") Long fileId,@Param("batchNumber") String batchNumber,
		@Param("errorCode") String errorCode);
    
	
	List<BatchFileUploadHeader> selectByUploadDate(@Param("uploadDate") String uploadDate);
	
	List<BatchFileUpload> selectResultByBatchNumber(@Param("batchNumber") String batchNumber);
	
   
}