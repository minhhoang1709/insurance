package com.ninelives.insurance.batch.mybatis.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.batch.model.PushNotificationData;

@Mapper
public interface BatchMapper {
	
	@Select ({
		"SELECT b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
		"FROM public.policy_order a, public.users b, public.coverage_category c ",
		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
		"  and a.policy_start_date=#{targetDate,jdbcType=DATE} ",
		"  and a.status in ('APPROVED','ACTIVE')"
	})
	public PushNotificationData selectActiveOrder(@Param("targetDate") LocalDate targetDate);
	
	@Select ({
		"SELECT b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
		"FROM public.policy_order a, public.users b, public.coverage_category c ",
		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
		"  and a.policy_end_date=#{targetDate,jdbcType=DATE} ",
		"  and a.status in ('APPROVED','ACTIVE')"
	})
	public PushNotificationData selectExpireOrder(@Param("targetDate") LocalDate targetDate);
	
	@Select ({
		"SELECT b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
		"FROM public.policy_order a, public.users b, public.coverage_category c ",
		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
		  "and (",
		    "(a.policy_end_date=(#{targetDate,jdbcType=DATE}::date - '1 day'::interval) and a.period_id='101') ",
		      "or ",
		    "(a.policy_end_date=(#{targetDate,jdbcType=DATE}::date - '3 day'::interval) and a.period_id!='101') ",
		  ") ",
		  "and a.status in ('APPROVED','ACTIVE')"
	})
	public PushNotificationData selectToBeExpireOrder(@Param("targetDate") LocalDate targetDate);
}