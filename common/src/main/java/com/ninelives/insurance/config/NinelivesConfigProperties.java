package com.ninelives.insurance.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.ninelives.insurance.provider.payment.midtrans.ref.MidtransDurationUnit;


public class NinelivesConfigProperties {
	private Order order = new Order();	
	@Valid
	private Storage storage = new Storage();
	@Valid
	private Payment payment = new Payment();
	@Valid
	private Promo promo = new Promo();
	@Valid
	private Insurance insurance = new Insurance();

	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
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
	public Insurance getInsurance() {
		return insurance;
	}
	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	public static class Insurance {
		@NotEmpty
		private String aswataClientCode;
		@NotEmpty
		private String aswataClientKey;
		@NotEmpty
		private String aswataProductCode;
		@NotEmpty
		private String aswataPackageType;
		@NotEmpty
		private String aswataUrl;
		
		private int aswataConnectionPoolSize = 32;
		private int aswataPoolTimeout = 5000;
		private int aswataConnectTimeout = 5000;
		private int aswataSocketTimeout = 30000;
		
		public String getAswataClientCode() {
			return aswataClientCode;
		}
		public void setAswataClientCode(String aswataClientCode) {
			this.aswataClientCode = aswataClientCode;
		}
		public String getAswataClientKey() {
			return aswataClientKey;
		}
		public void setAswataClientKey(String aswataClientKey) {
			this.aswataClientKey = aswataClientKey;
		}		
		public String getAswataProductCode() {
			return aswataProductCode;
		}
		public void setAswataProductCode(String aswataProductCode) {
			this.aswataProductCode = aswataProductCode;
		}
		public String getAswataPackageType() {
			return aswataPackageType;
		}
		public void setAswataPackageType(String aswataPackageType) {
			this.aswataPackageType = aswataPackageType;
		}		
		public String getAswataUrl() {
			return aswataUrl;
		}
		public void setAswataUrl(String aswataUrl) {
			this.aswataUrl = aswataUrl;
		}
		public int getAswataConnectionPoolSize() {
			return aswataConnectionPoolSize;
		}
		public void setAswataConnectionPoolSize(int aswataConnectionPoolSize) {
			this.aswataConnectionPoolSize = aswataConnectionPoolSize;
		}
		public int getAswataPoolTimeout() {
			return aswataPoolTimeout;
		}
		public void setAswataPoolTimeout(int aswataPoolTimeout) {
			this.aswataPoolTimeout = aswataPoolTimeout;
		}
		public int getAswataConnectTimeout() {
			return aswataConnectTimeout;
		}
		public void setAswataConnectTimeout(int aswataConnectTimeout) {
			this.aswataConnectTimeout = aswataConnectTimeout;
		}
		public int getAswataSocketTimeout() {
			return aswataSocketTimeout;
		}
		public void setAswataSocketTimeout(int aswataSocketTimeout) {
			this.aswataSocketTimeout = aswataSocketTimeout;
		}
		
	}

	public static class Promo {		
		private Boolean isPromoAvailable = false;
		@NotNull
		private Integer inviteVoucherId;
		@NotNull
		private Integer voucherMinimumAggregatePayment;
		
		private int voucherCodeLength = 10;

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
		private int midtransSocketTimeout = 30000;
		
		private int midtransPaymentExpiryDuration = 3;
		private MidtransDurationUnit midtransPaymentExpiryUnit = MidtransDurationUnit.HOURS;
		
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
		public MidtransDurationUnit getMidtransPaymentExpiryUnit() {
			return midtransPaymentExpiryUnit;
		}
		public void setMidtransPaymentExpiryUnit(MidtransDurationUnit midtransPaymentExpiryUnit) {
			this.midtransPaymentExpiryUnit = midtransPaymentExpiryUnit;
		}
	
	}

	public static class Storage {
		/**
	     * Folder location for storing files
	     */
		@NotEmpty
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
	    private int policyStartDatePeriod = 365;
	    
	    /**
		 * Payment should be made within specified period (inclusive)
		 */
	    private int policyDueDatePeriod = 30;
	    
	    /**
		 * Specify number of coverage that allowed to be active at the same period 
		 */
	    private int policyConflictPeriodLimit = 3;
	    
	    private int minimumAge = 17;
	    private int maximumAge = 60;

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

		public int getMinimumAge() {
			return minimumAge;
		}

		public void setMinimumAge(int minimumAge) {
			this.minimumAge = minimumAge;
		}

		public int getMaximumAge() {
			return maximumAge;
		}

		public void setMaximumAge(int maximumAge) {
			this.maximumAge = maximumAge;
		}

		@Override
		public String toString() {
			return "Order [policyStartDatePeriod=" + policyStartDatePeriod + ", policyDueDatePeriod="
					+ policyDueDatePeriod + ", policyConflictPeriodLimit=" + policyConflictPeriodLimit + ", minimumAge="
					+ minimumAge + ", maximumAge=" + maximumAge + "]";
		}
		
	}

	@Override
	public String toString() {
		return "NinelivesConfigProperties [" + (storage != null ? "storage=" + storage + ", " : "")
				+ (payment != null ? "payment=" + payment : "") + "]";
	}
	
	
}
