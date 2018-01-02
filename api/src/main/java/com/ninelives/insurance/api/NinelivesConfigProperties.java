package com.ninelives.insurance.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@ConfigurationProperties("ninelives")
@Validated
public class NinelivesConfigProperties {
//	/**
//	 * Default policy title if not specified by coverage category
//	 */
//	private String policyTitle;
//	
//	/**
//	 * Default policy image url if not specified by coverage category
//	 */
//	private String policyImageUrl;
	
//	private Order order = new Order();
	
	private Storage storage = new Storage();
	
	@Valid
	private Payment payment = new Payment();
	
	@Valid
	private Promo promo = new Promo();

//	public String getPolicyTitle() {
//		return policyTitle;
//	}
//
//	public void setPolicyTitle(String policyTitle) {
//		this.policyTitle = policyTitle;
//	}
//
//	public String getPolicyImageUrl() {
//		return policyImageUrl;
//	}
//
//	public void setPolicyImageUrl(String policyImageUrl) {
//		this.policyImageUrl = policyImageUrl;
//	}
//
//	public Order getOrder() {
//		return order;
//	}
//
//	public void setOrder(Order order) {
//		this.order = order;
//	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Promo getPromo() {
		return promo;
	}

	public void setPromo(Promo promo) {
		this.promo = promo;
	}

	public static class Promo {		
		private Boolean isPromoAvailable = false;
		
		@NotNull
		private Integer inviteVoucherId;
		
		@NotNull
		private Integer voucherMinimumAggregatePayment;
		
		private int voucherCodeLength = 20;

		public Boolean getIsPromoAvailable() {
			return isPromoAvailable;
		}

		public void setIsPromoAvailable(Boolean isPromoAvailable) {
			this.isPromoAvailable = isPromoAvailable;
		}

		public Integer getInviteVoucherId() {
			return inviteVoucherId;
		}

		public void setInviteVoucherId(Integer inviteVoucherId) {
			this.inviteVoucherId = inviteVoucherId;
		}

		public Integer getVoucherMinimumAggregatePayment() {
			return voucherMinimumAggregatePayment;
		}

		public void setVoucherMinimumAggregatePayment(Integer voucherMinimumAggregatePayment) {
			this.voucherMinimumAggregatePayment = voucherMinimumAggregatePayment;
		}		

		public int getVoucherCodeLength() {
			return voucherCodeLength;
		}

		public void setVoucherCodeLength(int voucherCodeLength) {
			this.voucherCodeLength = voucherCodeLength;
		}

		@Override
		public String toString() {
			return "Promo [isPromoAvailable=" + isPromoAvailable + ", inviteVoucherId=" + inviteVoucherId + "]";
		}
		
	}
	public static class Payment {
		@NotEmpty
		private String midtransEnvironment;
		
		@NotEmpty
		private String midtransSandboxServerKey;
		
		@NotEmpty
		private String midtransSandboxClientKey;
		
		@NotEmpty
		private String midtransSandboxUrl;
		
		@NotEmpty
		private String midtransProductionServerKey;
		
		@NotEmpty
		private String midtransProductionClientKey;
		
		@NotEmpty
		private String midtransProductionUrl;
		
		private int midtransConnectionPoolSize = 16;
		private int midtransPoolTimeout = 5000;
		private int midtransConnectTimeout = 5000;
		private int midtransSocketTimeout = 20000;
		
		private int midtransPaymentExpiryDuration = 3;
		private String midtransPaymentExpiryUnit = "hours";
		
		public String getMidtransSandboxServerKey() {
			return midtransSandboxServerKey;
		}
		public void setMidtransSandboxServerKey(String midtransSandboxServerKey) {
			this.midtransSandboxServerKey = midtransSandboxServerKey;
		}
		public String getMidtransSandboxClientKey() {
			return midtransSandboxClientKey;
		}
		public void setMidtransSandboxClientKey(String midtransSandboxClientKey) {
			this.midtransSandboxClientKey = midtransSandboxClientKey;
		}
		public String getMidtransProductionServerKey() {
			return midtransProductionServerKey;
		}
		public void setMidtransProductionServerKey(String midtransProductionServerKey) {
			this.midtransProductionServerKey = midtransProductionServerKey;
		}
		public String getMidtransProductionClientKey() {
			return midtransProductionClientKey;
		}
		public void setMidtransProductionClientKey(String midtransProductionClientKey) {
			this.midtransProductionClientKey = midtransProductionClientKey;
		}
		public String getMidtransEnvironment() {
			return midtransEnvironment;
		}
		public void setMidtransEnvironment(String midtransEnvironment) {
			this.midtransEnvironment = midtransEnvironment;
		}
		public int getMidtransConnectionPoolSize() {
			return midtransConnectionPoolSize;
		}
		public void setMidtransConnectionPoolSize(int midtransConnectionPoolSize) {
			this.midtransConnectionPoolSize = midtransConnectionPoolSize;
		}		
		public int getMidtransPoolTimeout() {
			return midtransPoolTimeout;
		}
		public void setMidtransPoolTimeout(int midtransPoolTimeout) {
			this.midtransPoolTimeout = midtransPoolTimeout;
		}
		public int getMidtransConnectTimeout() {
			return midtransConnectTimeout;
		}
		public void setMidtransConnectTimeout(int midtransConnectTimeout) {
			this.midtransConnectTimeout = midtransConnectTimeout;
		}
		public int getMidtransSocketTimeout() {
			return midtransSocketTimeout;
		}
		public void setMidtransSocketTimeout(int midtransSocketTimeout) {
			this.midtransSocketTimeout = midtransSocketTimeout;
		}
		public String getMidtransSandboxUrl() {
			return midtransSandboxUrl;
		}
		public void setMidtransSandboxUrl(String midtransSandboxUrl) {
			this.midtransSandboxUrl = midtransSandboxUrl;
		}
		public String getMidtransProductionUrl() {
			return midtransProductionUrl;
		}
		public void setMidtransProductionUrl(String midtransProductionUrl) {
			this.midtransProductionUrl = midtransProductionUrl;
		}
		public int getMidtransPaymentExpiryDuration() {
			return midtransPaymentExpiryDuration;
		}
		public void setMidtransPaymentExpiryDuration(int midtransPaymentExpiryDuration) {
			this.midtransPaymentExpiryDuration = midtransPaymentExpiryDuration;
		}
		public String getMidtransPaymentExpiryUnit() {
			return midtransPaymentExpiryUnit;
		}
		public void setMidtransPaymentExpiryUnit(String midtransPaymentExpiryUnit) {
			this.midtransPaymentExpiryUnit = midtransPaymentExpiryUnit;
		}
		
	}

	public static class Storage {
		/**
	     * Folder location for storing files
	     */
	    private String location;

	    public String getLocation() {
	        return location;
	    }

	    public void setLocation(String location) {
	        this.location = location;
	    }

		@Override
		public String toString() {
			return "Storage [" + (location != null ? "location=" + location : "") + "]";
		}
	    
	}
	
	public static class Order {
		/**
		 * Allowed policy-start-date should not exceed the specified period
		 */
	    private int policyStartDatePeriod = 60;
	    
	    /**
		 * Payment should be made within specified period (inclusive)
		 */
	    private int policyDueDatePeriod = 30;
	    
	    /**
		 * Specify number of coverage that allowed to be active at the same period 
		 */
	    private int policyConflictPeriodLimit = 3;

		public int getPolicyStartDatePeriod() {
			return policyStartDatePeriod;
		}

		public void setPolicyStartDatePeriod(int policyStartDatePeriod) {
			this.policyStartDatePeriod = policyStartDatePeriod;
		}

		public int getPolicyDueDatePeriod() {
			return policyDueDatePeriod;
		}

		public void setPolicyDueDatePeriod(int policyDueDatePeriod) {
			this.policyDueDatePeriod = policyDueDatePeriod;
		}

		public int getPolicyConflictPeriodLimit() {
			return policyConflictPeriodLimit;
		}

		public void setPolicyConflictPeriodLimit(int policyConflictPeriodLimit) {
			this.policyConflictPeriodLimit = policyConflictPeriodLimit;
		}

		@Override
		public String toString() {
			return "Order [policyStartDatePeriod=" + policyStartDatePeriod + ", policyDueDatePeriod="
					+ policyDueDatePeriod + ", policyConflictPeriodLimit=" + policyConflictPeriodLimit + "]";
		}
		
	}

	@Override
	public String toString() {
		return "NinelivesConfigProperties [" + (storage != null ? "storage=" + storage + ", " : "")
				+ (payment != null ? "payment=" + payment : "") + "]";
	}
	
	
}
