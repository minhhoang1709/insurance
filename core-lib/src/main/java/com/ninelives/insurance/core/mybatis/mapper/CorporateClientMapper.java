package com.ninelives.insurance.core.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.model.CorporateClient;

@Mapper
public interface CorporateClientMapper {
	
	 @Insert({
	        "insert into public.corporate_client (corporate_client_name, corporate_client_address, ",
	        "corporate_client_phone_number, corporate_client_email, ",
	        "corporate_client_provider, corporate_client_provider_id, created_date,",
	        "created_by,update_date, update_by) ",
	        "values (#{corporateClientName,jdbcType=VARCHAR}, #{corporateClientAddress,jdbcType=VARCHAR}, ",
	        "#{corporateClientPhoneNumber,jdbcType=VARCHAR}, #{corporateClientEmail,jdbcType=VARCHAR}, ",
	        "#{corporateClientProvider,jdbcType=VARCHAR}, #{corporateClientProviderId,jdbcType=VARCHAR}, ",
	        "#{createdDate,jdbcType=TIMESTAMP}, #{createdBy,jdbcType=VARCHAR}, ",
	        "#{updateDate,jdbcType=TIMESTAMP}, #{updateBy,jdbcType=VARCHAR})"
	    })
	    int insertSelective(CorporateClient corporateClient);
	 
	 @Select({
	        "select",
	        "corporate_client_id, corporate_client_name, corporate_client_phone_number, corporate_client_email, corporate_client_provider, ",
	        "corporate_client_provider_id,  created_date, created_by ",
	        "from public.corporate_client",
	        "where corporate_client_name = #{companyName,jdbcType=VARCHAR} and corporate_client_provider_id = #{corporateId,jdbcType=VARCHAR}"
	    })
	 CorporateClient selectByCompanyIdAndCorporateId(@Param("companyName") String companyName,@Param("corporateId") String corporateId);

	List<CorporateClient> selectAllCorporateClient();

	@Select({
        "select corporate_client_id, corporate_client_name, corporate_client_address, corporate_client_phone_number ",
        "corporate_client_email, corporate_client_provider, corporate_client_provider_id, created_date ",
        "created_by, update_date, update_by ",
        "from public.corporate_client",
        "where corporate_client_id = #{id,jdbcType=INTEGER}"
    })
	CorporateClient selectCorporateClientById(@Param("id") int id);

	int updateCorporateClientSelective(CorporateClient cc);
}
