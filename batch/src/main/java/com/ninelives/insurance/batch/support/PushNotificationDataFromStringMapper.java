package com.ninelives.insurance.batch.support;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.ninelives.insurance.batch.model.PushNotificationData;
import com.ninelives.insurance.batch.ref.PushNotificationType;

public class PushNotificationDataFromStringMapper implements FieldSetMapper<PushNotificationData>{

	@Override
	public PushNotificationData mapFieldSet(FieldSet fieldSet) throws BindException {
		PushNotificationData data = new PushNotificationData();
		
		data.setEmail(fieldSet.readString(0));
		data.setFcmToken(fieldSet.readString(1));
		data.setCoverageCategoryId(fieldSet.readString(2));
		data.setCoverageCategoryName(fieldSet.readString(3));
		data.setOrderId(fieldSet.readString(4));
		data.setPushNotificationType(PushNotificationType.valueOf(fieldSet.readString(5)));
		
		return data;
	}

}
