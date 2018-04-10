package com.ninelives.insurance.batch.support;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.ninelives.insurance.batch.model.B2bOrderConfirmData;

public class B2bOrderConfirmDataFromStringMapper implements FieldSetMapper<B2bOrderConfirmData>{

	@Override
	public B2bOrderConfirmData mapFieldSet(FieldSet fieldSet) throws BindException {
		B2bOrderConfirmData data = new B2bOrderConfirmData();
		
		data.setUserId(fieldSet.readString(0));
		data.setEmail(fieldSet.readString(1));
		data.setOrderId(fieldSet.readString(2));
		data.setVoucherId(fieldSet.readInt(3));
		data.setVoucherCode(fieldSet.readString(4));
		
		return data;
	}

}
