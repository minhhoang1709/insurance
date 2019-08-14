package com.ninelives.insurance.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

import com.ninelives.insurance.provider.payment.midtrans.ref.MidtransDurationUnit;


public class NinelivesConfigProperties {
	
	@NotEmpty
	private String helpPolicyStandardFilePath;
	
	@NotEmpty
	private String helpTravelPolicyStandardFilePath;
	
	@NotEmpty
	private String helpSelfiePolicyStandardFilePath;
	
	private AppLocale appLocale = new AppLocale();
	
	private Order order = new Order();
	private Claim claim = new Claim();
	
	@Valid
	private Storage storage = new Storage();
	@Valid
	private Payment payment = new Payment();
	@Valid
	private Promo promo = new Promo();
	@Valid
	private Insurance insurance = new Insurance();
	@Valid
	private Account account = new Account();
	
	private Email email = new Email();
	
	public String getHelpPolicyStandardFilePath() {
		return helpPolicyStandardFilePath;
	}
	public void setHelpPolicyStandardFilePath(String helpPolicyStandardFilePath) {
		this.helpPolicyStandardFilePath = helpPolicyStandardFilePath;
	}	
	public String getHelpTravelPolicyStandardFilePath() {
		return helpTravelPolicyStandardFilePath;
	}
	public void setHelpTravelPolicyStandardFilePath(String helpTravelPolicyStandardFilePath) {
		this.helpTravelPolicyStandardFilePath = helpTravelPolicyStandardFilePath;
	}
	
	public String getHelpSelfiePolicyStandardFilePath() {
		return helpSelfiePolicyStandardFilePath;
	}
	public void setHelpSelfiePolicyStandardFilePath(String helpSelfiePolicyStandardFilePath) {
		this.helpSelfiePolicyStandardFilePath = helpSelfiePolicyStandardFilePath;
	}
	
	public AppLocale getAppLocale() {
		return appLocale;
	}
	public void setAppLocale(AppLocale appLocale) {
		this.appLocale = appLocale;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Claim getClaim() {
		return claim;
	}
	public void setClaim(Claim claim) {
		this.claim = claim;
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}

	public static class AppLocale {
		private String defaultLocale;
		private Map<String, String> defaultLocaleByCountry = new HashMap<>();
		private List<String> supportedLocales = new ArrayList<>();
		public String getDefaultLocale() {
			return defaultLocale;
		}
		public void setDefaultLocale(String defaultLocale) {
			this.defaultLocale = defaultLocale;
		}			
		public Map<String, String> getDefaultLocaleByCountry() {
			return defaultLocaleByCountry;
		}
		public void setDefaultLocaleByCountry(Map<String, String> defaultLocaleByCountry) {
			this.defaultLocaleByCountry = defaultLocaleByCountry;
		}
		public List<String> getSupportedLocales() {
			return supportedLocales;
		}
		public void setSupportedLocales(List<String> supportedLocales) {
			this.supportedLocales = supportedLocales;
		}

	}
	
	public static class Account {
		@NotEmpty
		private String googleTokenEndpointUrl;
		@NotEmpty
		private List<String> googleOauth2ClientIds;
		@NotEmpty
		private List<String> googleIss;
		
		private int emailVerificationValidHours = 24;
		
		private String verificationSignKey;
		private String verificationSuccessUrl = "http://www.9lives.me/sso.html";
		private String verificationFailUrl = "http://www.9lives.me/error_sso.html";
		
		private int temporaryPasswordLength = 8;
		private int temporaryPasswordValidHours = 24;
		
		public String getGoogleTokenEndpointUrl() {
			return googleTokenEndpointUrl;
		}
		public void setGoogleTokenEndpointUrl(String googleTokenEndpointUrl) {
			this.googleTokenEndpointUrl = googleTokenEndpointUrl;
		}
		public List<String> getGoogleOauth2ClientIds() {
			return googleOauth2ClientIds;
		}
		public void setGoogleOauth2ClientIds(List<String> googleOauth2ClientIds) {
			this.googleOauth2ClientIds = googleOauth2ClientIds;
		}
		public List<String> getGoogleIss() {
			return googleIss;
		}
		public void setGoogleIss(List<String> googleIss) {
			this.googleIss = googleIss;
		}	
		public int getEmailVerificationValidHours() {
			return emailVerificationValidHours;
		}
		public void setEmailVerificationValidHours(int emailVerificationValidHours) {
			this.emailVerificationValidHours = emailVerificationValidHours;
		}
		public int getTemporaryPasswordValidHours() {
			return temporaryPasswordValidHours;
		}
		public void setTemporaryPasswordValidHours(int temporaryPasswordValidHours) {
			this.temporaryPasswordValidHours = temporaryPasswordValidHours;
		}
		public String getVerificationSignKey() {
			return verificationSignKey;
		}
		public void setVerificationSignKey(String verificationSignKey) {
			this.verificationSignKey = verificationSignKey;
		}
		public String getVerificationSuccessUrl() {
			return verificationSuccessUrl;
		}
		public void setVerificationSuccessUrl(String verificationSuccessUrl) {
			this.verificationSuccessUrl = verificationSuccessUrl;
		}
		public String getVerificationFailUrl() {
			return verificationFailUrl;
		}
		public void setVerificationFailUrl(String verificationFailUrl) {
			this.verificationFailUrl = verificationFailUrl;
		}
		public int getTemporaryPasswordLength() {
			return temporaryPasswordLength;
		}
		public void setTemporaryPasswordLength(int temporaryPasswordLength) {
			this.temporaryPasswordLength = temporaryPasswordLength;
		}
		
	}
	public static class Email {
		private String sendgridBearerToken = "";
		private Map<String, String> sendgridTemplateForVerification = new HashMap<>();
		private Map<String, String> sendgridTemplateForPasswordReset = new HashMap<>();
		private String sendgridEmailFrom = "noreply@9lives.me";
		
		private String signupVerificationLink = "https://api.9lives.me/email/verify?token=%s&lang=%s";
		
		public String getSendgridBearerToken() {
			return sendgridBearerToken;
		}
		public void setSendgridBearerToken(String sendgridBearerToken) {
			this.sendgridBearerToken = sendgridBearerToken;
		}
		public Map<String, String> getSendgridTemplateForVerification() {
			return sendgridTemplateForVerification;
		}
		public void setSendgridTemplateForVerification(Map<String, String> sendgridTemplateForVerification) {
			this.sendgridTemplateForVerification = sendgridTemplateForVerification;
		}
		public Map<String, String> getSendgridTemplateForPasswordReset() {
			return sendgridTemplateForPasswordReset;
		}
		public void setSendgridTemplateForPasswordReset(Map<String, String> sendgridTemplateForPasswordReset) {
			this.sendgridTemplateForPasswordReset = sendgridTemplateForPasswordReset;
		}
		public String getSendgridEmailFrom() {
			return sendgridEmailFrom;
		}
		public void setSendgridEmailFrom(String sendgridEmailFrom) {
			this.sendgridEmailFrom = sendgridEmailFrom;
		}
		public String getSignupVerificationLink() {
			return signupVerificationLink;
		}
		public void setSignupVerificationLink(String signupVerificationLink) {
			this.signupVerificationLink = signupVerificationLink;
		}
		
	}
	public static class Insurance {
		private Boolean aswataEnableConnection = true;
		
		@NotEmpty
		private String aswataClientCode;
		@NotEmpty
		private String aswataClientKey;
		@NotEmpty
		private String aswataUrl;
		
		private int aswataConnectionPoolSize = 32;
		private int aswataPoolTimeout = 5000;
		private int aswataConnectTimeout = 5000;
		private int aswataSocketTimeout = 30000;
		
		//PTI configurations

		private Boolean ptiEnableConnection = true;
		
		@NotEmpty
		@Value("${ninelives.insurance.pti-url}")
		private String ptiUrl;
		
		private String ptiInsurerUrl = "localhost:9443/insurer/pti/idtFiles/";
		
		@NotEmpty
		private String ptiSecretCode = "cbf01306c1865742901ff2e184020c60";
		
		private int ptiServiceCode = 100;
		private int ptiConnectionPoolSize = 32;
		private int ptiPoolTimeout = 5000;
		private int ptiConnectTimeout = 5000;
		private int ptiSocketTimeout = 30000;
		
		private String ptiPolicyFileDir;
		
		@Value("${ninelives.insurance.pti-template-file-path}")
		private String ptiTemplateFilePath;
		
		@Value("${ninelives.insurance.pti-template-font-file-path}")
		private String ptiTemplateFontFilePath;
		
		private String ptiTemplateFontDefaultAppearance;
		private int ptiTemplateCoverageMaxCount = 6;		
				
		public Boolean getAswataEnableConnection() {
			return aswataEnableConnection;
		}
		public void setAswataEnableConnection(Boolean aswataEnableConnection) {
			this.aswataEnableConnection = aswataEnableConnection;
		}
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
		public Boolean getPtiEnableConnection() {
			return ptiEnableConnection;
		}
		public void setPtiEnableConnection(Boolean ptiEnableConnection) {
			this.ptiEnableConnection = ptiEnableConnection;
		}
		public String getPtiUrl() {
			return ptiUrl;
		}
		public void setPtiUrl(String ptiUrl) {
			this.ptiUrl = ptiUrl;
		}
		public String getPtiInsurerUrl() {
			return ptiInsurerUrl;
		}
		public void setPtiInsurerUrl(String ptiInsurerUrl) {
			this.ptiInsurerUrl = ptiInsurerUrl;
		}
		public String getPtiSecretCode() {
			return ptiSecretCode;
		}
		public void setPtiSecretCode(String ptiSecretCode) {
			this.ptiSecretCode = ptiSecretCode;
		}
		public int getPtiServiceCode() {
			return ptiServiceCode;
		}
		public void setPtiServiceCode(int ptiServiceCode) {
			this.ptiServiceCode = ptiServiceCode;
		}
		public int getPtiConnectionPoolSize() {
			return ptiConnectionPoolSize;
		}
		public void setPtiConnectionPoolSize(int ptiConnectionPoolSize) {
			this.ptiConnectionPoolSize = ptiConnectionPoolSize;
		}
		public int getPtiPoolTimeout() {
			return ptiPoolTimeout;
		}
		public void setPtiPoolTimeout(int ptiPoolTimeout) {
			this.ptiPoolTimeout = ptiPoolTimeout;
		}
		public int getPtiConnectTimeout() {
			return ptiConnectTimeout;
		}
		public void setPtiConnectTimeout(int ptiConnectTimeout) {
			this.ptiConnectTimeout = ptiConnectTimeout;
		}
		public int getPtiSocketTimeout() {
			return ptiSocketTimeout;
		}
		public void setPtiSocketTimeout(int ptiSocketTimeout) {
			this.ptiSocketTimeout = ptiSocketTimeout;
		}
		public String getPtiPolicyFileDir() {
			return ptiPolicyFileDir;
		}
		public void setPtiPolicyFileDir(String ptiPolicyFileDir) {
			this.ptiPolicyFileDir = ptiPolicyFileDir;
		}
		public String getPtiTemplateFilePath() {
			return ptiTemplateFilePath;
		}
		public void setPtiTemplateFilePath(String ptiTemplateFilePath) {
			this.ptiTemplateFilePath = ptiTemplateFilePath;
		}
		public String getPtiTemplateFontFilePath() {
			return ptiTemplateFontFilePath;
		}
		public void setPtiTemplateFontFilePath(String ptiTemplateFontFilePath) {
			this.ptiTemplateFontFilePath = ptiTemplateFontFilePath;
		}
		public String getPtiTemplateFontDefaultAppearance() {
			return ptiTemplateFontDefaultAppearance;
		}
		public void setPtiTemplateFontDefaultAppearance(String ptiTemplateFontDefaultAppearance) {
			this.ptiTemplateFontDefaultAppearance = ptiTemplateFontDefaultAppearance;
		}
		public int getPtiTemplateCoverageMaxCount() {
			return ptiTemplateCoverageMaxCount;
		}
		public void setPtiTemplateCoverageMaxCount(int ptiTemplateCoverageMaxCount) {
			this.ptiTemplateCoverageMaxCount = ptiTemplateCoverageMaxCount;
		}
		
		
	}

	public static class Promo {		
		private Boolean isPromoAvailable = false;
		
		@NotNull
		private Integer inviteVoucherId;
		@NotNull
		private Integer voucherMinimumAggregatePayment;
		
		private int voucherMaxUseMargin = 20;
		
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

		public int getVoucherMaxUseMargin() {
			return voucherMaxUseMargin;
		}

		public void setVoucherMaxUseMargin(int voucherMaxUseMargin) {
			this.voucherMaxUseMargin = voucherMaxUseMargin;
		}

		public int getVoucherCodeLength() {
			return voucherCodeLength;
		}

		public void setVoucherCodeLength(int voucherCodeLength) {
			this.voucherCodeLength = voucherCodeLength;
		}

		@Override
		public String toString() {
			return "Promo [isPromoAvailable=" + isPromoAvailable + ", inviteVoucherId=" + inviteVoucherId
					+ ", voucherMinimumAggregatePayment=" + voucherMinimumAggregatePayment + ", voucherMaxUseMargin="
					+ voucherMaxUseMargin + ", voucherCodeLength=" + voucherCodeLength + "]";
		}
		
	}
	public static class Payment {
		private Boolean midtransEnableConnection = true;
		private Boolean twoc2pEnable = false;
		
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
		
		private String twoc2pVersion;
		private String twoc2pMerchantId;
		private String twoc2pKey;
		private String twoc2pCurrency;
		private String twoc2pResultUrl1;
		private String twoc2pResultUrl2;
		private String twoc2pPaymentUrl;
		
		public Boolean getMidtransEnableConnection() {
			return midtransEnableConnection;
		}
		public void setMidtransEnableConnection(Boolean midtransEnableConnection) {
			this.midtransEnableConnection = midtransEnableConnection;
		}
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
		public Boolean getTwoc2pEnable() {
			return twoc2pEnable;
		}
		public void setTwoc2pEnable(Boolean twoc2pEnable) {
			this.twoc2pEnable = twoc2pEnable;
		}		
		public String getTwoc2pVersion() {
			return twoc2pVersion;
		}
		public void setTwoc2pVersion(String twoc2pVersion) {
			this.twoc2pVersion = twoc2pVersion;
		}
		public String getTwoc2pMerchantId() {
			return twoc2pMerchantId;
		}
		public void setTwoc2pMerchantId(String twoc2pMerchantId) {
			this.twoc2pMerchantId = twoc2pMerchantId;
		}
		public String getTwoc2pKey() {
			return twoc2pKey;
		}
		public void setTwoc2pKey(String twoc2pKey) {
			this.twoc2pKey = twoc2pKey;
		}
		public String getTwoc2pCurrency() {
			return twoc2pCurrency;
		}
		public void setTwoc2pCurrency(String twoc2pCurrency) {
			this.twoc2pCurrency = twoc2pCurrency;
		}
		public String getTwoc2pResultUrl1() {
			return twoc2pResultUrl1;
		}
		public void setTwoc2pResultUrl1(String twoc2pResultUrl1) {
			this.twoc2pResultUrl1 = twoc2pResultUrl1;
		}
		public String getTwoc2pResultUrl2() {
			return twoc2pResultUrl2;
		}
		public void setTwoc2pResultUrl2(String twoc2pResultUrl2) {
			this.twoc2pResultUrl2 = twoc2pResultUrl2;
		}
		public String getTwoc2pPaymentUrl() {
			return twoc2pPaymentUrl;
		}
		public void setTwoc2pPaymentUrl(String twoc2pPaymentUrl) {
			this.twoc2pPaymentUrl = twoc2pPaymentUrl;
		}
		
	}

	public static class Storage {
		/**
	     * Folder location for storing files
	     */
		@NotEmpty
	    private String location;
		
		/**
		 * Folder under location for storing user uploaded file
		 */
		@NotEmpty
		private String userFilePath;

	    public String getLocation() {
	        return location;
	    }

	    public void setLocation(String location) {
	        this.location = location;
	    }

	    
		public String getUserFilePath() {
			return userFilePath;
		}

		public void setUserFilePath(String userFilePath) {
			this.userFilePath = userFilePath;
		}

		@Override
		public String toString() {
			return "Storage [location=" + location + ", userFilePath=" + userFilePath + "]";
		}
	    
	}
	
	public static class Order {
		/**
		 * Allowed policy-start-date should not exceed the specified period
		 */
	    private int policyStartDatePeriod = 365;
	    
	    /**
		 * Allowed policy-start-date for Travel insurance should not more than specified period
		 */
	    private int travelMaxPolicyStartDatePeriod = 31;
	    
	       /**
		 * Payment should be made within specified period (inclusive)
		 */
	    private int policyDueDatePeriod = 30;
	    
	    /**
		 * Specify number of coverage that allowed to be active at the same period
		 */
	    private int policyConflictPeriodLimit = 3;
	    
	    /**
		 * Specify number of coverage that allowed to be active at the same period for Travel Insurance 
		 */
	    private int travelPolicyConflictPeriodLimit = 1;
	    	
	    /**
		 * Allowed policy-start-date for Travel insurance should not less than specified period
		 */
	    private int travelMinPolicyStartDatePeriod = 2;
	   
	    private int minimumAge = 17;
	    private int maximumAge = 60;
	    
	    private int travelMinimumAge = 17;
	    private int travelMaximumAge = 75;
	    
	    private int minimumPayment = 5000;
	    
	    private int familyMinorMinimumAge = 2;
	    private int familyMinorMaximumAge = 16;
	    private int familyAdultMinimumAge = 17;
	    private int familyAdultMaximumAge = 75;
	    
	    private int familyAdultMaximumCount = 1;	    
	    private int familyMinorMaximumCount = 3;
	    
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

		public int getTravelMinPolicyStartDatePeriod() {
			return travelMinPolicyStartDatePeriod;
		}

		public void setTravelMinPolicyStartDatePeriod(int travelMinPolicyStartDatePeriod) {
			this.travelMinPolicyStartDatePeriod = travelMinPolicyStartDatePeriod;
		}

		public int getTravelMaxPolicyStartDatePeriod() {
			return travelMaxPolicyStartDatePeriod;
		}

		public void setTravelMaxPolicyStartDatePeriod(int travelMaxPolicyStartDatePeriod) {
			this.travelMaxPolicyStartDatePeriod = travelMaxPolicyStartDatePeriod;
		}

		public int getPolicyConflictPeriodLimit() {
			return policyConflictPeriodLimit;
		}

		public void setPolicyConflictPeriodLimit(int policyConflictPeriodLimit) {
			this.policyConflictPeriodLimit = policyConflictPeriodLimit;
		}		

		public int getTravelPolicyConflictPeriodLimit() {
			return travelPolicyConflictPeriodLimit;
		}

		public void setTravelPolicyConflictPeriodLimit(int travelPolicyConflictPeriodLimit) {
			this.travelPolicyConflictPeriodLimit = travelPolicyConflictPeriodLimit;
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

		public int getMinimumPayment() {
			return minimumPayment;
		}

		public void setMinimumPayment(int minimumPayment) {
			this.minimumPayment = minimumPayment;
		}		
		
		public int getFamilyAdultMinimumAge() {
			return familyAdultMinimumAge;
		}

		public void setFamilyAdultMinimumAge(int familyAdultMinimumAge) {
			this.familyAdultMinimumAge = familyAdultMinimumAge;
		}

		public int getFamilyAdultMaximumAge() {
			return familyAdultMaximumAge;
		}

		public void setFamilyAdultMaximumAge(int familyAdultMaximumAge) {
			this.familyAdultMaximumAge = familyAdultMaximumAge;
		}
		
		public int getFamilyAdultMaximumCount() {
			return familyAdultMaximumCount;
		}

		public void setFamilyAdultMaximumCount(int familyAdultMaximumCount) {
			this.familyAdultMaximumCount = familyAdultMaximumCount;
		}

		public int getFamilyMinorMaximumCount() {
			return familyMinorMaximumCount;
		}

		public void setFamilyMinorMaximumCount(int familyMinorMaximumCount) {
			this.familyMinorMaximumCount = familyMinorMaximumCount;
		}

		public int getTravelMinimumAge() {
			return travelMinimumAge;
		}

		public void setTravelMinimumAge(int travelMinimumAge) {
			this.travelMinimumAge = travelMinimumAge;
		}

		public int getTravelMaximumAge() {
			return travelMaximumAge;
		}

		public void setTravelMaximumAge(int travelMaximumAge) {
			this.travelMaximumAge = travelMaximumAge;
		}

		public int getFamilyMinorMinimumAge() {
			return familyMinorMinimumAge;
		}

		public void setFamilyMinorMinimumAge(int familyMinorMinimumAge) {
			this.familyMinorMinimumAge = familyMinorMinimumAge;
		}

		public int getFamilyMinorMaximumAge() {
			return familyMinorMaximumAge;
		}

		public void setFamilyMinorMaximumAge(int familyMinorMaximumAge) {
			this.familyMinorMaximumAge = familyMinorMaximumAge;
		}

		@Override
		public String toString() {
			return "Order [policyStartDatePeriod=" + policyStartDatePeriod + ", policyDueDatePeriod="
					+ policyDueDatePeriod + ", policyConflictPeriodLimit=" + policyConflictPeriodLimit
					+ ", travelMinPolicyStartDatePeriod=" + travelMinPolicyStartDatePeriod + ", minimumAge="
					+ minimumAge + ", maximumAge=" + maximumAge + ", minimumPayment=" + minimumPayment
					+ ", familyAdultMinimumAge=" + familyAdultMinimumAge + ", familyAdultMaximumAge="
					+ familyAdultMaximumAge + ", familyAdultMaximumCount=" + familyAdultMaximumCount
					+ ", familyMinorMaximumCount=" + familyMinorMaximumCount + "]";
		}
		
	}
	
	public static class Claim {
		/**
		 * Allowed period between claim and policy-end-date
		 */
	    private int maxPolicyEndDatePeriod = 30;

		public int getMaxPolicyEndDatePeriod() {
			return maxPolicyEndDatePeriod;
		}

		public void setMaxPolicyEndDatePeriod(int maxPolicyEndDatePeriod) {
			this.maxPolicyEndDatePeriod = maxPolicyEndDatePeriod;
		}
	    
	}

	@Override
	public String toString() {
		return "NinelivesConfigProperties [" + (storage != null ? "storage=" + storage + ", " : "")
				+ (payment != null ? "payment=" + payment : "") + "]";
	}
	
	
}
