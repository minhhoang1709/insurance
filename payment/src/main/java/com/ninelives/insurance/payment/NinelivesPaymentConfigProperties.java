package com.ninelives.insurance.payment;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("ninelives-payment")
@Validated
public class NinelivesPaymentConfigProperties {
	@Valid
	private Midtrans midtrans = new Midtrans();
	
	@Valid
	private Promo promo = new Promo();
		
	public Midtrans getMidtrans() {
		return midtrans;
	}

	public void setMidtrans(Midtrans midtrans) {
		this.midtrans = midtrans;
	}
	
	public Promo getPromo() {
		return promo;
	}

	public void setPromo(Promo promo) {
		this.promo = promo;
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
	
	public static class Promo {
		@NotNull
		private Integer voucherMinimumAggregatePayment;

		public Integer getVoucherMinimumAggregatePayment() {
			return voucherMinimumAggregatePayment;
		}

		public void setVoucherMinimumAggregatePayment(Integer voucherMinimumAggregatePayment) {
			this.voucherMinimumAggregatePayment = voucherMinimumAggregatePayment;
		}
		
	}
}
