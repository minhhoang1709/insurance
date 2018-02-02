package com.ninelives.insurance.api.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeDto {
	@JsonProperty("customer_details")
	private CustomerDetails customerDetails;
	
	@JsonProperty("credit_card")
	private CreditCard creditCard;
	
	@JsonProperty("item_details")
	private List<ItemDetails> itemDetails;
	
	@JsonProperty("transaction_details")
	private TransactionDetails transactionDetails;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("custom_field1")
	private String authToken;
	
	@JsonProperty("custom_field2")
	private String paymentSeq;
	
	private Expiry expiry;
	
	@JsonProperty("enabled_payments")
	private List<String> enabledPayments;
	
	@JsonIgnore
	public Map<String, Object> other = new HashMap<>();
	
	public CustomerDetails getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetails customerDetails) {
		this.customerDetails = customerDetails;
	}
		
	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public List<ItemDetails> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<ItemDetails> itemDetails) {
		this.itemDetails = itemDetails;
	}

	public TransactionDetails getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(TransactionDetails transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public String getPaymentSeq() {
		return paymentSeq;
	}

	public void setPaymentSeq(String paymentSeq) {
		this.paymentSeq = paymentSeq;
	}

	public Expiry getExpiry() {
		return expiry;
	}

	public void setExpiry(Expiry expiry) {
		this.expiry = expiry;
	}
	
	public List<String> getEnabledPayments() {
		return enabledPayments;
	}

	public void setEnabledPayments(List<String> enabledPayments) {
		this.enabledPayments = enabledPayments;
	}

	@JsonAnyGetter
	public Map<String, Object> any() {
		return other;
	}

	@JsonAnySetter
	public void set(String name, Object value) {
		other.put(name, value);
	}

	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}
	
	public Map<String, Object> getOther() {
		return other;
	}

	public void setOther(Map<String, Object> other) {
		this.other = other;
	}
	public static class ItemDetails{
		String id;
		String name;
		Integer price;
		Integer quantity;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getPrice() {
			return price;
		}
		public void setPrice(Integer price) {
			this.price = price;
		}
		public Integer getQuantity() {
			return quantity;
		}
		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
		@Override
		public String toString() {
			return "ItemDetails [" + (id != null ? "id=" + id + ", " : "") + (name != null ? "name=" + name + ", " : "")
					+ (price != null ? "price=" + price + ", " : "") + (quantity != null ? "quantity=" + quantity : "")
					+ "]";
		}
		
	}
	public static class TransactionDetails{
		@JsonProperty("gross_amount")
		Integer grossAmount;
		
		@JsonProperty("order_id")
		String orderId;
		

		public Integer getGrossAmount() {
			return grossAmount;
		}
		public void setGrossAmount(Integer grossAmount) {
			this.grossAmount = grossAmount;
		}
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		@Override
		public String toString() {
			return "TransactionDetails [" + (grossAmount != null ? "grossAmount=" + grossAmount + ", " : "")
					+ (orderId != null ? "orderId=" + orderId : "") + "]";
		}		
	}
	
	public static class CreditCard{
		String bank;
		
		String channel;
		
		@JsonProperty("save_card")
		Boolean saveCard;
		
		Boolean secure;

		public String getBank() {
			return bank;
		}
		public void setBank(String bank) {
			this.bank = bank;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public Boolean getSaveCard() {
			return saveCard;
		}
		public void setSaveCard(Boolean saveCard) {
			this.saveCard = saveCard;
		}
		public Boolean getSecure() {
			return secure;
		}
		public void setSecure(Boolean secure) {
			this.secure = secure;
		}
		@Override
		public String toString() {
			return "CreditCard [" + (bank != null ? "bank=" + bank + ", " : "")
					+ (channel != null ? "channel=" + channel + ", " : "")
					+ (saveCard != null ? "saveCard=" + saveCard + ", " : "")
					+ (secure != null ? "secure=" + secure : "") + "]";
		}
		
	}
	public static class CustomerDetails{
		@JsonProperty("billing_address")
		BillingAddress billingAddress;
		
		@JsonProperty("shipping_address")
		ShippingAddress shippingAddress;
		
		String email;
		
		@JsonProperty("first_name")
		String firstName;
		
		String phone;

		public BillingAddress getBillingAddress() {
			return billingAddress;
		}

		public void setBillingAddress(BillingAddress billingAddress) {
			this.billingAddress = billingAddress;
		}

		public ShippingAddress getShippingAddress() {
			return shippingAddress;
		}

		public void setShippingAddress(ShippingAddress shippingAddress) {
			this.shippingAddress = shippingAddress;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		@Override
		public String toString() {
			return "CustomerDetails [" + (billingAddress != null ? "billingAddress=" + billingAddress + ", " : "")
					+ (shippingAddress != null ? "shippingAddress=" + shippingAddress + ", " : "")
					+ (email != null ? "email=" + email + ", " : "")
					+ (firstName != null ? "firstName=" + firstName + ", " : "")
					+ (phone != null ? "phone=" + phone : "") + "]";
		}
	}
	
	public static class BillingAddress{
		String address;
		
		String city;
		
		@JsonProperty("country_code")
		String countryCode;
		
		@JsonProperty("first_name")
		String firstName;
		
		String phone;
		
		@JsonProperty("postal_code")
		String postalCode;
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountryCode() {
			return countryCode;
		}
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		@Override
		public String toString() {
			return "BillingAddress [" + (address != null ? "address=" + address + ", " : "")
					+ (city != null ? "city=" + city + ", " : "")
					+ (countryCode != null ? "countryCode=" + countryCode + ", " : "")
					+ (firstName != null ? "firstName=" + firstName + ", " : "")
					+ (phone != null ? "phone=" + phone + ", " : "")
					+ (postalCode != null ? "postalCode=" + postalCode : "") + "]";
		}
	}
	
	public static class ShippingAddress{
		String address;
		
		String city;
		
		@JsonProperty("country_code")
		String countryCode;
		
		@JsonProperty("first_name")
		String firstName;
		
		String phone;
		
		@JsonProperty("postal_code")
		String postalCode;
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCountryCode() {
			return countryCode;
		}
		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getPostalCode() {
			return postalCode;
		}
		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		@Override
		public String toString() {
			return "ShippingAddress [" + (address != null ? "address=" + address + ", " : "")
					+ (city != null ? "city=" + city + ", " : "")
					+ (countryCode != null ? "countryCode=" + countryCode + ", " : "")
					+ (firstName != null ? "firstName=" + firstName + ", " : "")
					+ (phone != null ? "phone=" + phone + ", " : "")
					+ (postalCode != null ? "postalCode=" + postalCode : "") + "]";
		}
	}
	
	public static class Expiry{
		String unit;
		String duration;
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		@Override
		public String toString() {
			return "Expiry [" + (unit != null ? "unit=" + unit + ", " : "")
					+ (duration != null ? "duration=" + duration : "") + "]";
		}
		
	}

	@Override
	public String toString() {
		return "ChargeDto [customerDetails=" + customerDetails + ", creditCard=" + creditCard + ", itemDetails="
				+ itemDetails + ", transactionDetails=" + transactionDetails + ", userId=" + userId + ", authToken="
				+ authToken + ", paymentSeq=" + paymentSeq + ", expiry=" + expiry + ", enabledPayments="
				+ enabledPayments + ", other=" + other + "]";
	}
}
