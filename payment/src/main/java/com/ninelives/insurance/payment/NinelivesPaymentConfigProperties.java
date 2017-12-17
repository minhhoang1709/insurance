package com.ninelives.insurance.payment;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ninelives-payment")
@Validated
public class NinelivesPaymentConfigProperties {
	@Valid
	private Midtrans midtrans = new Midtrans();
	
	public Midtrans getMidtrans() {
		return midtrans;
	}

	public void setMidtrans(Midtrans midtrans) {
		this.midtrans = midtrans;
	}

	public static class Midtrans {
		@NotEmpty
		private String serverKey;

		public String getServerKey() {
			return serverKey;
		}

		public void setServerKey(String serverKey) {
			this.serverKey = serverKey;
		}		
	}
}
