package com.ninelives.insurance.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ninelives")
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
	    
	    

	}
}
