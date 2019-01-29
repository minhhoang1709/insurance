package com.ninelives.insurance.batch.mybatis.mapper;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.batch.model.PushNotificationData;

@Mapper
public interface BatchMapper {
	
	@Select ({
		"SELECT b.user_id, b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
		"FROM public.policy_order a, public.users b, public.coverage_category c ",
		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
		"  and a.policy_start_date=#{targetDate,jdbcType=DATE} ",
		"  and a.status in ('APPROVED','ACTIVE') ",
		"  and b.fcm_token is not null"
	})
	public PushNotificationData selectActiveOrderForFcmNotification(@Param("targetDate") LocalDate targetDate);
	
	@Select ({
		"SELECT b.user_id, b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
		"FROM public.policy_order a, public.users b, public.coverage_category c ",
		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
		"  and a.policy_end_date=(#{targetDate,jdbcType=DATE}::date - '1 day'::interval) ",
		"  and a.status in ('APPROVED','ACTIVE') ",
		"  and b.fcm_token is not null"
	})
	public PushNotificationData selectExpireOrderForFcmNotification(@Param("targetDate") LocalDate targetDate);
	
//	@Select ({
//		"SELECT b.user_id, b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name ",
//		"FROM public.policy_order a, public.users b, public.coverage_category c ",
//		"WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id ",
//		  "and (",
//		    "(a.policy_end_date=(#{targetDate,jdbcType=DATE}::date) and a.period_id='101') ",
//		      "or ",
//		    "(a.policy_end_date=(#{targetDate,jdbcType=DATE}::date + '2 day'::interval) and a.period_id!='101') ",
//		  ") ",
//		  "and a.status in ('APPROVED','ACTIVE') ",
//		  "and b.fcm_token is not null"
//	})
//	
	@Select({
		"SELECT a.user_id, a.email, a.fcm_token, a.order_id, a.coverage_category_id, a.coverage_category_name ",
		"FROM ( ",
		  "SELECT b.user_id, b.email, b.fcm_token, a.order_id, c.coverage_category_id, c.name coverage_category_name,  ",
		     " a.period_id, a.policy_end_date, (a.policy_end_date-a.policy_start_date)+1 as policy_days  ",
		  "FROM public.policy_order a, public.users b, public.coverage_category c  ",
		  "WHERE b.user_id=a.user_id and c.coverage_category_id=a.coverage_category_id  ",
		      "and a.policy_start_date<=(#{targetDate,jdbcType=DATE}::date) ",
		      "and a.policy_end_date>=(#{targetDate,jdbcType=DATE}::date) ",
		      "and a.status in ('APPROVED','ACTIVE') ",
		") a ",
		"WHERE ( ",
		  "( a.policy_end_date=(#{targetDate,jdbcType=DATE}::date) and a.period_id='101' )  ",
		  "or ",
		  "( a.policy_end_date=(#{targetDate,jdbcType=DATE}::date) and a.period_id ='201' and a.policy_days between 1 and 3 ) ",
		  "or ",
		  "( a.policy_end_date=(#{targetDate,jdbcType=DATE}::date + '2 day'::interval) and a.period_id ='201' and a.policy_days > 3 ) ",
		  "or ",
		  "( a.policy_end_date=(#{targetDate,jdbcType=DATE}::date + '2 day'::interval) and a.period_id not in ('101','201') ) ",
		")  ",
	})
	public PushNotificationData selectToBeExpireOrderForFcmNotification(@Param("targetDate") LocalDate targetDate);
}