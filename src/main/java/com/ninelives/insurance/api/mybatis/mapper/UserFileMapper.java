package com.ninelives.insurance.api.mybatis.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ninelives.insurance.api.model.UserFile;

@Mapper
public interface UserFileMapper {
    
	@Insert({
        "insert into public.user_file (file_id, user_id, ",
        "file_use_type, file_path, ",
        "status, file_size, ",
        "content_type, upload_date) ",
        "values (#{fileId,jdbcType=BIGINT}, #{userId,jdbcType=VARCHAR}, ",
        "#{fileUseType,jdbcType=VARCHAR}, #{filePath,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=VARCHAR}, #{fileSize,jdbcType=BIGINT}, ",
        "#{contentType,jdbcType=VARCHAR}, #{uploadDate,jdbcType=DATE})"
    })
    int insert(UserFile record);

    int insertSelective(UserFile record);

    @Select({
        "select",
        "file_id, user_id, file_use_type, file_path, status, file_size, content_type, ",
        "upload_date, create_date, update_date",
        "from public.user_file",
        "where file_id = #{fileId,jdbcType=BIGINT}"
    })
    @ResultMap("com.ninelives.insurance.api.mybatis.mapper.UserFileMapper.BaseResultMap")
    UserFile selectByPrimaryKey(Long fileId);

    @Update({
        "update public.user_file",
        "set user_id = #{userId,jdbcType=VARCHAR},",
          "file_use_type = #{fileUseType,jdbcType=VARCHAR},",
          "file_path = #{filePath,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=VARCHAR},",
          "file_size = #{fileSize,jdbcType=BIGINT},",
          "content_type = #{contentType,jdbcType=VARCHAR},",
          "upload_date = #{uploadDate,jdbcType=DATE},",
          "create_date = #{createDate,jdbcType=TIMESTAMP},",
          "update_date = #{updateDate,jdbcType=TIMESTAMP}",
        "where file_id = #{fileId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(UserFile record);

    @Select("select nextval('user_file_id_seq')")
	Long selectNextFileId();
}