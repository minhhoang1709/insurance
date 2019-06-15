
package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.camel.FluentProducerTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDocumentDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.dto.PolicyOrderFamilyDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.ClaimService;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.core.service.InsuranceService;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.core.service.VoucherService;
import com.ninelives.insurance.core.trx.PolicyOrderTrxService;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.CoverageOrderDocType;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderDocument;
import com.ninelives.insurance.model.PolicyOrderFamily;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyOrderVoucher;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.ProductType;
import com.ninelives.insurance.ref.VoucherType;
import com.ninelives.insurance.route.EndPointRef;
import com.ninelives.insurance.util.ValidationUtil;

@Service
public class ApiOrderService {
	private static final Logger logger = LoggerFactory.getLogger(ApiOrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired OrderService orderService;
	@Autowired ClaimService claimService;
	@Autowired ProductService productService;
	@Autowired UserService userService;
	@Autowired VoucherService voucherService;	
	@Autowired PolicyOrderTrxService policyOrderTrxService;
	@Autowired NotificationService notificationService;
	@Autowired InsuranceService insuranceService;
	@Autowired FileUploadService fileUploadService;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Autowired MessageSource messageSource;
	
	@Autowired FluentProducerTemplate producerTemplate;
	
	
	public PolicyOrderBeneficiaryDto updateBeneficiary(String authUserId, String orderId,
			PolicyOrderBeneficiaryDto beneficiary) throws AppException {
		PolicyOrderBeneficiary policyOrderBeneficiary = registerBeneficiary(authUserId, orderId, beneficiary);
		return modelMapperAdapter.toDto(policyOrderBeneficiary);
	}
	
	public OrderDto submitOrder(final String userId, final OrderDto orderDto, final boolean isValidateOnly) throws AppException{
		PolicyOrder policyOrder = registerOrder(userId, orderDto, isValidateOnly);
		
		if(isValidateOnly){
			return orderDto;
		}else{
			return modelMapperAdapter.toDto(policyOrder);
		}		
	}
	
	public PolicyOrder fetchOrderForDownload(String userId, String orderId) throws AppException{
		return orderService.fetchOrderForDownload(userId, orderId);
	}
	
	public OrderDto fetchOrderDtoByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = orderService.fetchOrderByOrderId(userId, orderId);
		return modelMapperAdapter.toDto(policyOrder);
	}
	
	public List<OrderDto> fetchOrderDtos(final String userId, final FilterDto filter){
		ArrayList<OrderDto> orderDtos = new ArrayList<>();
		List<PolicyOrder> orders = fetchOrders(userId, filter);
		if(orders!=null){
			for(PolicyOrder po: orders){
				//orderDtos.add(policyOrderToDto(po));
				orderDtos.add(modelMapperAdapter.toDto(po));
			}
		}
		return orderDtos;
	}
	
	protected PolicyOrderBeneficiary registerBeneficiary(String userId, String orderId,
			PolicyOrderBeneficiaryDto beneficiaryDto) throws AppException{

		logger.info("Process start registerBeneficiary, userId:<{}>, orderId:<{}> , beneficiary:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto);
		
		if (beneficiaryDto == null 
				|| StringUtils.isEmpty(beneficiaryDto.getEmail())
				|| StringUtils.isEmpty(beneficiaryDto.getName())
				|| StringUtils.isEmpty(beneficiaryDto.getPhone())
				|| StringUtils.isEmpty(beneficiaryDto.getRelationship())
				) {
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception invalid beneficiary>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4101_BENEFICIARY_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR4101_BENEFICIARY_INVALID,
					"Permintaan tidak dapat diproses, silahkan cek kembali data penerima.");
		}
		PolicyOrder policyOrder = orderService.fetchOrderWithBeneficiaryByOrderId(userId, orderId);
		if(policyOrder == null){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception order not found>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR5001_ORDER_NOT_FOUND);
			throw new AppBadRequestException(ErrorCode.ERR5001_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan.");
		}		
		if(!policyOrder.getHasBeneficiary()){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception order doesnt require beneficiary>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4103_BENEFICIARY_NOTACCEPTED);
			throw new AppBadRequestException(ErrorCode.ERR4103_BENEFICIARY_NOTACCEPTED,
					"Permintaan tidak dapat diproses, pemesanan Anda tidak membutuhkan data penerima.");
		}
		if(policyOrder.getPolicyOrderBeneficiary()!=null){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception beneficiary already exists>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4102_BENEFICIARY_EXISTS);
			throw new AppBadRequestException(ErrorCode.ERR4102_BENEFICIARY_EXISTS,
					"Permintaan tidak dapat diproses, daftar penerima sudah didaftarkan untuk asuransi ini.");
		}
		if(policyOrder.getStatus().equals(PolicyStatus.TERMINATED)||policyOrder.getStatus().equals(PolicyStatus.EXPIRED)){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<cannot update expired/terminated order>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4102_BENEFICIARY_EXISTS);
			throw new AppBadRequestException(ErrorCode.ERR4104_BENEFICIARY_ORDER_STATUS,
					"Permintaan tidak dapat diproses, masa aktif asuransi Anda telah melewati periode.");
		}
		
		PolicyOrderBeneficiary beneficiary = new PolicyOrderBeneficiary();
		beneficiary.setOrderId(orderId);
		beneficiary.setName(beneficiaryDto.getName());
		beneficiary.setEmail(beneficiaryDto.getEmail());
		beneficiary.setPhone(beneficiaryDto.getPhone());
		beneficiary.setRelationship(beneficiaryDto.getRelationship());
		
		orderService.insertBeneficiary(beneficiary);
		
		UserBeneficiary userBeneficiary = userService.fetchUserBeneficiaryByUserId(userId);
		if(userBeneficiary==null){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<success insert>, error:<>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto);

			userBeneficiary = new UserBeneficiary();
			userBeneficiary.setUserId(userId);
			userBeneficiary.setName(beneficiaryDto.getName());
			userBeneficiary.setEmail(beneficiaryDto.getEmail());
			userBeneficiary.setPhone(beneficiaryDto.getPhone());
			userBeneficiary.setRelationship(beneficiaryDto.getRelationship());
			
			userService.registerUserBeneficiary(userBeneficiary);
			
			
		}else if(!orderService.isEquals(beneficiary, userBeneficiary)){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<success update>, error:<>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto);

			userBeneficiary.setName(beneficiaryDto.getName());
			userBeneficiary.setEmail(beneficiaryDto.getEmail());
			userBeneficiary.setPhone(beneficiaryDto.getPhone());
			userBeneficiary.setRelationship(beneficiaryDto.getRelationship());
			
			userService.updateUserBeneficiaryFromOrder(userBeneficiary);
			
		}
		
//		logger.debug("Process registerBeneficiary for {} with order {} and beneficiary {} finish", userId,
//				orderId, beneficiaryDto==null?"":beneficiaryDto) ;
		
		return beneficiary;
	}
	
	
	/**
	 * 
	 * @param userId
	 * @param submitOrderDto
	 * @param isValidateOnly Dry-run. Validate if order is valid (without validation to the user's profile).
	 * @return
	 * @throws AppException 
	 */
	protected PolicyOrder registerOrder(final String userId, final OrderDto submitOrderDto, final boolean isValidateOnly) throws AppException{
		logger.debug("Process order isvalidationonly <{}> user: <{}> with order: <{}>", isValidateOnly, userId, submitOrderDto);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();		
		
		if (submitOrderDto == null || submitOrderDto.getProducts() == null
				|| submitOrderDto.getPolicyStartDate() == null || submitOrderDto.getTotalPremi() == null) {
			logger.debug("Process order for {} with order {} with result: exception empty order or product", 
					userId,	submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4000_ORDER_INVALID,
					"Permintaan tidak dapat diproses, silahkan cek kembali pesanan.");
		}
		
		//order with voucher		
		Map<String, Product> voucherProductMap = null;
		Set<String> voucherProductIdSet = null;
		Voucher voucher = validateVoucher(userId, submitOrderDto);
		if(voucher!=null){
			voucherProductMap = voucher.getProducts().stream().collect(Collectors.toMap(Product::getProductId, item -> item)); 
			voucherProductIdSet = voucherProductMap.keySet();			
		}
				
		Set<String> productIdSet = submitOrderDto.getProducts().stream().map(ProductDto::getProductId).collect(Collectors.toSet()); 
				
		if(CollectionUtils.isEmpty(productIdSet)){
			logger.debug("Process order for {} with order {} with result: exception empty product", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4001_ORDER_PRODUCT_EMPTY, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk.");
		}
		if(submitOrderDto.getProducts().size()!= productIdSet.size()){
			logger.debug("Process order for {} with order {} with result: exception duplicate product", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4002_ORDER_PRODUCT_DUPLICATE, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk.");
		}
		if(!CollectionUtils.isEmpty(voucherProductIdSet)){
			if(!voucherProductIdSet.equals(productIdSet)){
				logger.debug("Process order for user <{}> with order <{}> with result: exception voucher product mismatch, voucher:<{}>", userId, submitOrderDto, voucherProductIdSet);
				throw new AppBadRequestException(ErrorCode.ERR4015_ORDER_VOUCHER_PRODUCT_MISMATCH, "Permintaan tidak dapat diproses, produk voucher tidak sesuai dengan pemesanan.");	
			}
		}
		
		LocalDate limitPolicyStartDate = today.plusDays(config.getOrder().getPolicyStartDatePeriod());
		if(submitOrderDto.getPolicyStartDate().toLocalDate().isAfter(limitPolicyStartDate)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date exceed limit {}", userId, submitOrderDto, config.getOrder().getPolicyStartDatePeriod());
			throw new AppBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");			
		}
		if(submitOrderDto.getPolicyStartDate().toLocalDate().isBefore(today)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date before today", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");			
		}
		
		List<Product> products = productIdSet.stream().map(productService::fetchProductByProductId).collect(Collectors.toList());
		//List<Product> products = productService.fetchProductByProductIds(productIdSet);
		if(CollectionUtils.isEmpty(products)||products.get(0)==null|| products.size()!=submitOrderDto.getProducts().size()){
			logger.debug("Process order for {} with order {} with result: exception product not found", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4003_ORDER_PRODUCT_NOTFOUND, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk.");
		}
		//logger.debug("products is not empty? empty is {}, and the products <{}> and the size is {}",products.isEmpty(),products, products.size());
		String periodId = products.get(0).getPeriodId();
		String coverageCategoryId = products.get(0).getCoverage().getCoverageCategoryId();
		CoverageCategory coverageCategory = productService.fetchCoverageCategoryByCoverageCategoryId(coverageCategoryId);
				
		if(coverageCategoryId.equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)||
				coverageCategoryId.equals(CoverageCategoryId.TRAVEL_DOMESTIC)){
			Long travelStartInDays = ChronoUnit.DAYS.between(today, submitOrderDto.getPolicyStartDate().toLocalDate());
			if ( travelStartInDays < config.getOrder().getTravelMinPolicyStartDatePeriod()) {
				logger.debug(
						"Process order for {} with order {} with result: exception travel startdate not valid with travel-min-policy-start-date-period={}",
						userId, submitOrderDto, config.getOrder().getTravelMinPolicyStartDatePeriod());
				throw new AppBadRequestException(ErrorCode.ERR4026_ORDER_TRAVEL_STARTDATE_INVALID, "Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");
			}else if(travelStartInDays > config.getOrder().getTravelMaxPolicyStartDatePeriod()){
				logger.debug(
						"Process order for {} with order {} with result: exception travel startdate not valid with travel-max-policy-start-date-period={}",
						userId, submitOrderDto, config.getOrder().getTravelMaxPolicyStartDatePeriod());
				throw new AppBadRequestException(ErrorCode.ERR4026_ORDER_TRAVEL_STARTDATE_INVALID, "Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");
			}
		}
		
		int calculatedTotalPremi = 0;
		int calculatedTotalBasePremi = 0;
		boolean hasBeneficiary = false;
		boolean isFamily = submitOrderDto.getIsFamily()!=null?submitOrderDto.getIsFamily():false;
		List<String> coverageIds = new ArrayList<>();
		for(Product p: products){
			//logger.debug("CHECK {}", p);
			if(!periodId.equals(p.getPeriodId())){
				logger.debug("Process order for {} with order {} with result: exception period mismatch", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4004_ORDER_PERIOD_MISMATCH, "Permintaan tidak dapat diproses, silahkan cek kembali periode asuransi.");
			}
			if(!coverageCategoryId.equals(p.getCoverage().getCoverageCategoryId())){
				logger.debug("Process order for {} with order {} with result: exception coverage mismatch", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4006_ORDER_COVERAGE_MISMATCH, "Permintaan tidak dapat diproses, pembelian lebih dari satu tipe asuransi belum didukung.");
			}
			//cannot by free product without voucher
			if(ProductType.FREE.equals(p.getProductType()) && voucher==null){
				logger.debug("Process order for {} with order {} with result: exception voucher needed", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4016_ORDER_VOUCHER_REQUIRED, "Permintaan tidak dapat diproses, produk ini hanya dapat dibeli lewat undangan.");
			}
			//order policy +family, multiplier is applied
			if(isFamily){
				//calculatedTotalPremi += (int) (p.getPremi() * config.getOrder().getFamilyMultiplier());
				//calculatedTotalBasePremi += (int) (p.getBasePremi() * config.getOrder().getFamilyMultiplier());
				calculatedTotalPremi += p.getFamilyPremi();
				calculatedTotalBasePremi += p.getFamilyPremi();
			}else{
				calculatedTotalPremi += p.getPremi();
				calculatedTotalBasePremi += p.getBasePremi();
			}			
			hasBeneficiary = hasBeneficiary || p.getCoverage().getHasBeneficiary();
			coverageIds.add(p.getCoverageId());
		}
		if(voucher == null){
			if(calculatedTotalPremi!=submitOrderDto.getTotalPremi()){
				logger.debug("Process order for {} with order {} with result: exception calculated premi {} ", userId, submitOrderDto, calculatedTotalPremi);
				throw new AppBadRequestException(ErrorCode.ERR4005_ORDER_PREMI_MISMATCH, "Permintaan tidak dapat diproses.");
			}
		}else{
			calculatedTotalPremi = voucher.getTotalPremi();
		}
			
		//if voucher is not used, then check for the minimum payment
		if(voucher == null){
			if(calculatedTotalPremi < config.getOrder().getMinimumPayment()){
				logger.debug("Process order for {} with order {} with result: exception minimum payment", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4019_ORDER_PAYMENT_MINIMUM,
						"Jumlah minimum transaksi adalah Rp. {0}.",
						new String[] { String.valueOf(config.getOrder().getMinimumPayment()) });
				
//				throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
//						"Produk ini hanya tersedia untuk usia {0} sampai {1} tahun.",
//						new String[] { String.valueOf(config.getOrder().getTravelMinimumAge()), 
//								String.valueOf(config.getOrder().getTravelMaximumAge()) });
			}
		}
		
		Period period = products.get(0).getPeriod();

		//policyEndDate is calculated in server side for non-travel product
		LocalDate policyEndDate = null;
		if(coverageCategoryId.equals(CoverageCategoryId.TRAVEL_DOMESTIC) ||
				coverageCategoryId.equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)){
			policyEndDate = submitOrderDto.getPolicyEndDate().toLocalDate();
			
			//policyEndDate should within range of selected period for travel product		
			if(!orderService.isPolicyEndDateWithinRange(submitOrderDto.getPolicyStartDate().toLocalDate(), policyEndDate, period)){
				logger.debug("Process order for <{}> with order <{}> with result: exception policy-end-date not match range period", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4020_ORDER_ENDDATE_INVALID,
						"Permintaan tidak dapat diproses.");
			}
		}else{
			policyEndDate = orderService.calculatePolicyEndDate(submitOrderDto.getPolicyStartDate().toLocalDate(), period);
		}				
		
		LocalDate dueOrderDate = today.minusDays(config.getOrder().getPolicyDueDatePeriod());		
		boolean isOverLimit = orderService.isOverCoverageInSamePeriodLimit(userId,
				submitOrderDto.getPolicyStartDate().toLocalDate(), policyEndDate, dueOrderDate, coverageIds, coverageCategoryId);
		if(isOverLimit){
			if(coverageCategoryId.equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)||
					coverageCategoryId.equals(CoverageCategoryId.TRAVEL_DOMESTIC)){
				logger.debug("Process order for {} with order {} with result: exception conflict coverage", userId, submitOrderDto);
				if(config.getOrder().getTravelPolicyConflictPeriodLimit()>1){
					throw new AppBadRequestException(ErrorCode.ERR4029_ORDER_TRAVEL_PRODUCT_CONFLICT,
							"Anda membeli jaminan yang sama lebih dari {0} kali untuk periode yang sama. Silakan atur kembali periode pemakaian jaminan.",
							new String[] { String.valueOf(config.getOrder().getTravelPolicyConflictPeriodLimit()) });					
				}else{
					throw new AppBadRequestException(ErrorCode.ERR4031_ORDER_TRAVEL_PRODUCT_SINGLE_CONFLICT,
							"Anda sudah membeli jaminan ini untuk periode yang sama. Silahkan atur kembali periode pemakaian jaminan.");					
				}
			}else{
				logger.debug("Process order for {} with order {} with result: exception conflict coverage", userId, submitOrderDto);
				if(config.getOrder().getPolicyConflictPeriodLimit()>1){
					throw new AppBadRequestException(ErrorCode.ERR4009_ORDER_PRODUCT_CONFLICT,
							"Anda membeli jaminan yang sama lebih dari {0} kali untuk periode yang sama. Silakan atur kembali periode pemakaian jaminan.",
							new String[] { String.valueOf(config.getOrder().getPolicyConflictPeriodLimit()) });
				}else{
					throw new AppBadRequestException(ErrorCode.ERR4030_ORDER_PRODUCT_SINGLE_CONFLICT,
							"Anda sudah membeli jaminan ini untuk periode yang sama. Silahkan atur kembali periode pemakaian jaminan.");					
				}
				
			}
		}
		
		PolicyOrder policyOrder = null;
		boolean isAllProfileInfoUpdated = false;
		boolean isPhoneInfoUpdated = false;
		boolean isAddressInfoUpdated = false;
		if(!isValidateOnly){
			if(isFamily){
				validateFamilyMember(userId, submitOrderDto);
			}
			
			final User existingUser = userService.fetchByUserId(userId);

			boolean isExistingUserProfileCompleteForOrder = orderService.isUserProfileCompleteForOrder(existingUser, coverageCategory);
			
			//if it is international travel, then check for passport
			if(coverageCategoryId.equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)){
				if(existingUser.getPassportFileId()==null){
					logger.debug("Process order for <{}> with order <{}> with result: passport not found", userId, submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4025_ORDER_PASSPORT_NOTFOUND,
							"Permintaan tidak dapat diproses, unggah paspor Anda untuk melanjutkan pemesanan.");
				}
			}else{
				if(existingUser.getIdCardFileId()==null){
					logger.debug("Process order for <{}> with order <{}> with result: id card not found", userId, submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4017_ORDER_IDCARD_NOTFOUND,
							"Permintaan tidak dapat diproses, unggah KTP Anda untuk melanjutkan pemesanan.");
				}				
			}
						
			//verify age
			LocalDate birthDate = existingUser.getBirthDate();
			if(birthDate == null){
				if(submitOrderDto.getUser()!=null && submitOrderDto.getUser().getBirthDate()!=null){
					birthDate = submitOrderDto.getUser().getBirthDate().toLocalDate();
				}
			}
			//logger.debug("Going to check birthdate {}", birthDate);
			if(birthDate != null){								
				if (coverageCategoryId.equals(CoverageCategoryId.TRAVEL_DOMESTIC)
						|| coverageCategoryId.equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)) {
					
					long age = ChronoUnit.YEARS.between(birthDate, submitOrderDto.getPolicyStartDate().toLocalDate());
					if(age > config.getOrder().getTravelMaximumAge()||
							age < config.getOrder().getTravelMinimumAge()){
						logger.debug("Process order for {} with order {} with result: age invalid", userId, submitOrderDto);
//						throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
//								"Produk ini hanya tersedia untuk usia " + config.getOrder().getTravelMinimumAge()
//										+ " sampai " + config.getOrder().getTravelMaximumAge() + " tahun.");
						throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
								"Produk ini hanya tersedia untuk usia {0} sampai {1} tahun.",
								new String[] { String.valueOf(config.getOrder().getTravelMinimumAge()), 
										String.valueOf(config.getOrder().getTravelMaximumAge()) });
					}
				}else{
					long age = ChronoUnit.YEARS.between(birthDate, today);
					if(age > config.getOrder().getMaximumAge()||
							age < config.getOrder().getMinimumAge()){
						logger.debug("Process order for {} with order {} with result: age invalid", userId, submitOrderDto);
//						throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
//								"Produk ini hanya tersedia untuk usia " + config.getOrder().getMinimumAge() + " sampai "
//										+ config.getOrder().getMaximumAge() + " tahun.");
						throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
								"Produk ini hanya tersedia untuk usia {0} sampai {1} tahun.",
								new String[] { String.valueOf(config.getOrder().getMinimumAge()), 
										String.valueOf(config.getOrder().getMaximumAge()) });
					}
				}
			}
			
			//modify phone
			String modifiedPhone = null;
			if(submitOrderDto.getUser()!=null && !StringUtils.isEmpty(submitOrderDto.getUser().getPhone())){
				modifiedPhone = submitOrderDto.getUser().getPhone().replaceAll("\\D+", "");
				
				if(!ValidationUtil.isPhoneNumberValid(modifiedPhone)){
					logger.debug("Process order for {} with order {} with result: phone invalid", userId, submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4027_ORDER_PROFILE_PHONE_INVALID,
							"Periksa kembali nomor telepon Anda");
				}
			}
						
			User newUserProfile = null;			
			if(!isExistingUserProfileCompleteForOrder){
				//in case existing profile is not complete, check the submit data
				if(submitOrderDto.getUser()==null){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan.");
				}
				 
				newUserProfile = new User();
				newUserProfile.setUserId(userId);;
				newUserProfile.setName(submitOrderDto.getUser().getName());
				newUserProfile.setGender(submitOrderDto.getUser().getGender());
				newUserProfile.setBirthDate(submitOrderDto.getUser().getBirthDate().toLocalDate());
				newUserProfile.setBirthPlace(submitOrderDto.getUser().getBirthPlace());
				newUserProfile.setAddress(submitOrderDto.getUser().getAddress());
				newUserProfile.setPhone(modifiedPhone);
				
				if(!orderService.isUserProfileCompleteForOrder(newUserProfile, coverageCategory)){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan.");
				}
				
				userService.updateProfileInfo(newUserProfile);
				
				isAllProfileInfoUpdated = true;
			} else {
				//incase existing profile exists, allow update phone or address
				if(submitOrderDto.getUser()!=null){
					User updateUser = new User();
					updateUser.setUserId(userId);
					
					if(modifiedPhone!=null && !existingUser.getPhone().equals(modifiedPhone)){
						updateUser.setPhone(modifiedPhone);
						isPhoneInfoUpdated = true;
					}
					if(!StringUtils.isEmpty(submitOrderDto.getUser().getAddress()) 
							&& !submitOrderDto.getUser().getAddress().equals(existingUser.getAddress())){
						updateUser.setAddress(submitOrderDto.getUser().getAddress());
						isAddressInfoUpdated = true;
					}
					if(isPhoneInfoUpdated || isAddressInfoUpdated){
						userService.updateProfileInfo(updateUser);
					}					
				}
				
			}	
			
			policyOrder = new PolicyOrder();
			policyOrder.setOrderId(orderService.generateOrderId());
			policyOrder.setOrderDate(today);
			policyOrder.setOrderTime(now);
			policyOrder.setUserId(userId);
			policyOrder.setCoverageCategoryId(coverageCategoryId);
			policyOrder.setCoverageCategory(productService.fetchCoverageCategoryByCoverageCategoryId(coverageCategoryId));
			policyOrder.setHasBeneficiary(hasBeneficiary);
			policyOrder.setPeriodId(periodId);
			policyOrder.setPolicyStartDate(submitOrderDto.getPolicyStartDate().toLocalDate());
			policyOrder.setPolicyEndDate(policyEndDate);
			policyOrder.setBasePremi(calculatedTotalBasePremi);
			policyOrder.setTotalPremi(calculatedTotalPremi);
			policyOrder.setProductCount(products.size());
			policyOrder.setPeriod(period);
			policyOrder.setStatus(PolicyStatus.SUBMITTED);
			policyOrder.setIsFamily(isFamily);

			//Set user info
			PolicyOrderUsers policyOrderUser = new PolicyOrderUsers();
			policyOrderUser.setOrderId(policyOrder.getOrderId());
			policyOrderUser.setEmail(existingUser.getEmail());
			policyOrderUser.setIdCardFileId(existingUser.getIdCardFileId());
			policyOrderUser.setPassportFileId(existingUser.getPassportFileId());

			if(isAllProfileInfoUpdated){			
				policyOrderUser.setName(newUserProfile.getName());
				policyOrderUser.setGender(newUserProfile.getGender());
				policyOrderUser.setBirthDate(newUserProfile.getBirthDate());
				policyOrderUser.setBirthPlace(newUserProfile.getBirthPlace());
				policyOrderUser.setAddress(newUserProfile.getAddress());
				policyOrderUser.setPhone(newUserProfile.getPhone());
			}else{
				policyOrderUser.setName(existingUser.getName());
				policyOrderUser.setGender(existingUser.getGender());
				policyOrderUser.setBirthDate(existingUser.getBirthDate());
				policyOrderUser.setBirthPlace(existingUser.getBirthPlace());				
				if(isAddressInfoUpdated){
					policyOrderUser.setAddress(submitOrderDto.getUser().getAddress());
				}else{
					policyOrderUser.setAddress(existingUser.getAddress());
				}
				if(isPhoneInfoUpdated){
					policyOrderUser.setPhone(modifiedPhone);
				}else{
					policyOrderUser.setPhone(existingUser.getPhone());
				}
			}
			
			policyOrder.setPolicyOrderUsers(policyOrderUser);
							
			//Set for policy product
			List<PolicyOrderProduct> policyOrderProducts = new ArrayList<>();
			for(Product p: products){
				PolicyOrderProduct pop = new PolicyOrderProduct();
				pop.setOrderId(policyOrder.getOrderId());
				pop.setCoverageId(p.getCoverageId());
				pop.setPeriodId(p.getPeriodId());
				pop.setProductId(p.getProductId());
				pop.setProductName(p.getName());
				pop.setCoverageName(p.getCoverage().getName());
				if(isFamily){
					//pop.setCoverageMaxLimit((long) (p.getCoverage().getMaxLimit() * config.getOrder().getFamilyMultiplier()));
					//pop.setBasePremi((int) (p.getBasePremi()* config.getOrder().getFamilyMultiplier()));
					//pop.setPremi((int) (p.getPremi()* config.getOrder().getFamilyMultiplier()));
					pop.setCoverageMaxLimit(p.getCoverage().getFamilyMaxLimit());
					pop.setBasePremi(p.getFamilyPremi());
					pop.setPremi(p.getFamilyPremi());
				}else{
					pop.setCoverageMaxLimit(p.getCoverage().getMaxLimit());
					pop.setBasePremi(p.getBasePremi());
					pop.setPremi(p.getPremi());
				}
				
				if(voucher!=null){
					pop.setPremi(voucherProductMap.get(p.getProductId()).getPremi());
				}				
				pop.setCoverageHasBeneficiary(p.getCoverage().getHasBeneficiary());
				pop.setPeriod(p.getPeriod());
				pop.setProduct(p); //for translation purpose
				policyOrderProducts.add(pop);
			}			
			policyOrder.setPolicyOrderProducts(policyOrderProducts);

			//Set for family member
			if(isFamily){
				List<PolicyOrderFamily> policyOrderFamilies = new ArrayList<>();
				int i=1;
				for(PolicyOrderFamilyDto family: submitOrderDto.getFamilies()){
					PolicyOrderFamily pof = new PolicyOrderFamily();
					pof.setOrderId(policyOrder.getOrderId());
					pof.setSubId(i++);
					pof.setName(family.getName());
					pof.setRelationship(family.getRelationship());
					pof.setBirthDate(family.getBirthDate().toLocalDate());
					pof.setGender(family.getGender());
					policyOrderFamilies.add(pof);
				}
				policyOrder.setPolicyOrderFamilies(policyOrderFamilies);
			}
			
			//Set for policy voucher
			if(voucher!=null){
				PolicyOrderVoucher pov = new PolicyOrderVoucher();
				pov.setOrderId(policyOrder.getOrderId());
				pov.setVoucher(voucher);
				policyOrder.setPolicyOrderVoucher(pov);
				policyOrder.setHasVoucher(true);
				
				//check if INVITE free voucher, if yes, then mark as APPROVED instead of SUBMITTED
				if(voucher.getTotalPremi().equals(0)){
					if(voucher.getVoucherType().equals(VoucherType.INVITE) 
							||voucher.getVoucherType().equals(VoucherType.FREE_PROMO_NEW_USER)){
						policyOrder.setStatus(PolicyStatus.APPROVED);
					}else if(voucher.getVoucherType().equals(VoucherType.B2B )){
						policyOrder.setStatus(PolicyStatus.PAID);
					}
				}
			}else{
				policyOrder.setHasVoucher(false);
			}
						
			//Set for documents
			List<PolicyOrderDocument> policyOrderDocuments = validateOrderDocuments(userId, policyOrder.getOrderId(), submitOrderDto);
			if(policyOrderDocuments!=null && policyOrderDocuments.size()>=0) {
				policyOrder.setPolicyOrderDocuments(policyOrderDocuments);
			}								
			
			try {
				insuranceService.orderPolicy(policyOrder);
			} catch (AppInternalServerErrorException e) {
				logger.error("Process registerOrder for user:<{}> with order:<{}>, result: exception provider <{}>",
						userId, submitOrderDto, e.getCode());
				throw e;
			}
			
			policyOrderTrxService.registerPolicyOrder(policyOrder);
			
			//move file from temp to claim
			if(policyOrderDocuments!=null && policyOrderDocuments.size()>=0) {
				orderService.updateOrderDocumentFiles(policyOrder.getPolicyOrderDocuments());
			}
			
						
			if(voucher!=null){
				//no need to send, comment out the success notif
				//sendSuccessNotification(existingUser.getFcmToken(), policyOrder, today);
				
				//process for inviter
				if(voucher.getVoucherType().equals(VoucherType.INVITE) &&
						policyOrder.getPolicyOrderVoucher().getVoucher().getInviterRewardCount() < 
						policyOrder.getPolicyOrderVoucher().getVoucher().getInviterRewardLimit()){
					try {
						asyncRegisterOrderForInviter(policyOrder);
					} catch (Exception e) {
						logger.error("Failed to register order for inviter", e);
					}
				}
				
				//update approve count
				if(voucher.getVoucherType().equals(VoucherType.FREE_PROMO_NEW_USER) 
						&& policyOrder.getStatus().equals(PolicyStatus.APPROVED)){
					voucherService.increaseVoucherApproveCounter(voucher.getId());
				}
			}
			
		}
		logger.debug("Process registerOrder isValidateOnly <{}> for <{}> with order <{}>, result update profile: <{}>, update phone: <{}>, order: <{}> with result success",
				isValidateOnly, userId, submitOrderDto, isAllProfileInfoUpdated, isPhoneInfoUpdated, policyOrder);
		
		return policyOrder;
	}
	
	/**
	 * Return null if there is no required document
	 * 
	 * 
	 * @param userId
	 * @param orderDto
	 * @return
	 * @throws AppBadRequestException
	 */
	@Nullable
	protected List<PolicyOrderDocument> validateOrderDocuments(final String userId, final String orderId, final OrderDto orderDto) throws AppBadRequestException{
		List<OrderDocumentDto> requiredDocDtos = requiredOrderDocumentDtos(orderDto);
		
		if(requiredDocDtos == null || requiredDocDtos.size() == 0) {
			return null;
		}
		
		if(orderDto.getOrderDocuments()==null 
				|| orderDto.getOrderDocuments().size() == 0
				|| orderDto.getOrderDocuments().size() != requiredDocDtos.size()) {
			logger.debug(
					"Process validateOrderDocuments, userId:<{}>, order:<{}>, result:<error doc list not match>, exception:<{}>, required:<{}>",
					userId, orderDto, ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY,
					requiredDocDtos);
			
			throw new AppBadRequestException(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
			
		}
		
		List<PolicyOrderDocument> orderDocs = new ArrayList<>();
		Set<Long> fileIdSet = new HashSet<>();
		for(OrderDocumentDto requiredDocDto: requiredDocDtos) {
			for (OrderDocumentDto submittedDocDto : orderDto.getOrderDocuments()) {
				if (submittedDocDto.getOrderDocType() != null 
						&& submittedDocDto.getOrderDocType().getOrderDocTypeId()
								.equals(requiredDocDto.getOrderDocType().getOrderDocTypeId())) {
					if(submittedDocDto.getFile()!=null) {
						UserFile submittedFile = fileUploadService.featchUploadedTempFileById(submittedDocDto.getFile().getFileId());
						if(submittedFile != null) {
							PolicyOrderDocument orderDoc = new PolicyOrderDocument();
							orderDoc.setOrderId(orderId);
							orderDoc.setOrderDocTypeId(submittedDocDto.getOrderDocType().getOrderDocTypeId());
							orderDoc.setFileId(submittedFile.getFileId());
							orderDoc.setUserFile(submittedFile);
							orderDocs.add(orderDoc);
							
							fileIdSet.add(submittedFile.getFileId());
						} else {
							logger.error(
									"Process validateOrderDocuments, userId:<{}>, order:<{}>, file <{}> not found in db",
									userId, orderDto, submittedDocDto.getFile().getFileId());
						}
					}
				}
			}
		}
		
		if(fileIdSet.size()!=requiredDocDtos.size() || orderDocs.size()!=requiredDocDtos.size()) {
			logger.debug(
					"Process validateOrderDocuments, userId:<{}>, order:<{}>, result:<error doc list not match>, exception:<{}>, required:<{}>",
					userId, orderDto, ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY,
					requiredDocDtos);
			
			throw new AppBadRequestException(ErrorCode.ERR4028_ORDER_DOCUMENT_MANDATORY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
		}
		
		return orderDocs;
	}

	/**
	 * Validate family member count and age againts policy-start-date
	 * 
	 * @param userId
	 * @param submitOrderDto
	 * @throws AppBadRequestException
	 */
	protected void validateFamilyMember(final String userId, OrderDto submitOrderDto) throws AppBadRequestException{
		if(CollectionUtils.isEmpty(submitOrderDto.getFamilies())){
			logger.debug("Process order for <{}> with order <{}> with result:<exception empty family>", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4021_ORDER_FAMILY_EMPTY,
					"Permintaan tidak dapat diproses. Periksa kembali data keluarga Anda");
		}		
		//LocalDate today = LocalDate.now();
		int minorCnt = 0;
		int adultCnt = 0;		
		for(PolicyOrderFamilyDto family: submitOrderDto.getFamilies()){
			if (StringUtils.isEmpty(family.getName()) || family.getRelationship() == null
					|| family.getBirthDate() == null || family.getGender() == null) {
				logger.debug("Process order for <{}> with order <{}> with result:<family invalid data>", userId, submitOrderDto);
				throw new AppBadRequestException(ErrorCode.ERR4022_ORDER_FAMILY_INVALID,
						"Permintaan tidak dapat diproses. Periksa kembali data keluarga Anda");
			}
			long age = ChronoUnit.YEARS.between(family.getBirthDate().toLocalDate(), submitOrderDto.getPolicyStartDate().toLocalDate());
			
			if (age < config.getOrder().getFamilyMinorMinimumAge()
					|| age > config.getOrder().getFamilyAdultMaximumAge()) {
				logger.debug("Process order for <{}> with order <{}> with result:<family invalid count>, exception:<{}> ",
						userId, submitOrderDto, ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID);
//				throw new AppBadRequestException(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID,
//						"Silakan cek kembali usia keluarga Anda. Kategori anak untuk usia "
//								+ config.getOrder().getFamilyMinorMinimumAge() + " tahun hingga "
//								+ config.getOrder().getFamilyMinorMaximumAge() + " tahun. Kategori dewasa untuk usia "
//								+ config.getOrder().getFamilyAdultMinimumAge() + " tahun hingga "
//								+ config.getOrder().getFamilyAdultMaximumAge() + " tahun");
				
				throw new AppBadRequestException(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID,
						"Silakan cek kembali usia keluarga Anda. Kategori anak untuk usia {0} tahun hingga {1} tahun. "
						+ "Kategori dewasa untuk usia {2} tahun hingga {3} tahun", new String[] {
								String.valueOf(config.getOrder().getFamilyMinorMinimumAge()),
								String.valueOf(config.getOrder().getFamilyMinorMaximumAge()),
								String.valueOf(config.getOrder().getFamilyAdultMinimumAge()),
								String.valueOf(config.getOrder().getFamilyAdultMaximumAge())
						});
			}
			
			if(age < config.getOrder().getFamilyAdultMinimumAge()){
				minorCnt++;
			}else{
				//adult should be pasangan
				if(!FamilyRelationship.PASANGAN.equals(family.getRelationship())){
					throw new AppBadRequestException(ErrorCode.ERR4022_ORDER_FAMILY_INVALID,
							"Permintaan tidak dapat diproses. Periksa kembali data keluarga Anda");
				}
				adultCnt++;				
			}
//			if(FamilyRelationship.ANAK.equals(family.getRelationship())){
//				minorCnt++;				
//				if(age < 0 || age >= config.getOrder().getFamilyAdultMinimumAge()){
//					logger.debug("Process order for <{}> with order <{0}> with result: family children age invalid", userId, submitOrderDto);
//					throw new AppBadRequestException(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID,
//							"Maksimum usia anak adalah 16 tahun");
//				}	
//			}else{
//				adultCnt++;
//				if(age < config.getOrder().getFamilyAdultMinimumAge() 
//						|| age > config.getOrder().getFamilyAdultMaximumAge()){
//					logger.debug("Process order for <{}> with order <{0}> with result: family adult age invalid", userId, submitOrderDto);
//					throw new AppBadRequestException(ErrorCode.ERR4023_ORDER_FAMILY_AGE_INVALID,
//							"Rentang usia dewasa adalah 17 tahun sampai dengan 83 tahun");
//				}
//			}
		}
		if(minorCnt > config.getOrder().getFamilyMinorMaximumCount() 
				|| adultCnt > config.getOrder().getFamilyAdultMaximumCount()){
			logger.debug("Process order for <{}> with order <{}> with result: family invalid count", userId, submitOrderDto);
			throw new AppBadRequestException(ErrorCode.ERR4024_ORDER_FAMILY_CNT_INVALID,
					"Permintaan tidak dapat diproses. Periksa kembali data keluarga Anda");
		}
	}
	
	protected Voucher validateVoucher(final String userId, OrderDto submitOrderDto) throws AppBadRequestException{
		Voucher voucher = null;
		
		if(userId!=null && submitOrderDto!=null && submitOrderDto.getVoucher()!=null){
			try {
				voucher = voucherService.fetchVoucherByCode(submitOrderDto.getVoucher().getCode(), submitOrderDto.getVoucher().getVoucherType(), false);
			} catch (AppNotFoundException e) {
				logger.debug("Process order for user: <{}> with order <{}> with result: voucher not found <{}>", 
						userId,	submitOrderDto, e.getCode());								
				throw new AppBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
						"Permintaan tidak dapat diproses, silahkan cek kode asuransi gratis Anda.");
			}
			
			logger.info("Validate voucher, process order for user: <{}> with order: <{}> with voucher: <{}>", userId, submitOrderDto, voucher);
			
			//check over limit case
			if(!voucherService.isUsable(voucher)){
				boolean isUsable = false;
				
				//for free promo allow error margin for voucher overuse
				if (VoucherType.FREE_PROMO_NEW_USER.equals(voucher.getVoucherType())
						&& !voucherService.isUseExpired(voucher)
						&& (voucher.getApproveCnt() < voucher.getMaxUse() + config.getPromo().getVoucherMaxUseMargin())){
					isUsable = true;
				}
				if(!isUsable){
					logger.debug("Process order for user: <{}> with order <{}> with result: voucher unusable", 
							userId,	submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
								"Kode ini sudah tidak tersedia.");
				}
			}
			
			if(VoucherType.INVITE.equals(voucher.getVoucherType())){
				//verify that user is eligible
				if(orderService.hasPaidOrder(userId)){
					logger.debug("Process order for user: <{}> with order <{}> with result: voucher not eligible", 
							userId,	submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4012_ORDER_VOUCHER_NOTELIGIBLE,
							"Permintaan tidak dapat diproses, asuransi gratis ini hanya dapat digunakan oleh pengguna baru.");
				}
				
			}else if(VoucherType.FREE_PROMO_NEW_USER.equals(voucher.getVoucherType())){
				if(orderService.hasPaidOrder(userId)){
					logger.debug("Process order for user: <{}> with order <{}> with result: voucher not eligible", 
							userId,	submitOrderDto);
					throw new AppBadRequestException(ErrorCode.ERR4012_ORDER_VOUCHER_NOTELIGIBLE,
							"Permintaan tidak dapat diproses, asuransi gratis ini hanya dapat digunakan oleh pengguna baru.");
				}
			}
			if(!voucher.getTotalPremi().equals(submitOrderDto.getTotalPremi())){
				logger.debug("Process order for user: <{}> with order <{}> with result: voucher premi not match, voucher: <{}>", 
						userId,	submitOrderDto, voucher.getTotalPremi());
				throw new AppBadRequestException(ErrorCode.ERR4013_ORDER_VOUCHER_PREMI_MISMATCH,
						"Permintaan tidak dapat diproses, premi voucher tidak sesuai dengan pemesanan.");
			}
			if(!voucher.getPolicyStartDate().isEqual(submitOrderDto.getPolicyStartDate().toLocalDate())||
					!voucher.getPolicyEndDate().isEqual(submitOrderDto.getPolicyEndDate().toLocalDate())){
				throw new AppBadRequestException(ErrorCode.ERR4014_ORDER_VOUCHER_DATE_MISMATCH,
						"Permintaan tidak dapat diproses, tanggal asuransi voucher tidak sesuai dengan pemesanan.");
			}
		}
				
		return voucher;
	}
//	private void sendSuccessNotification(String userId, String receiverFcmToken, PolicyOrder policyOrder, LocalDate today){
//		if(policyOrder!=null && policyOrder.getCoverageCategory()!=null){
//			if(policyOrder.getStatus().equals(PolicyStatus.APPROVED)
//					&& !policyOrder.getPolicyStartDate().isAfter(today) 
//					&& !policyOrder.getPolicyEndDate().isBefore(today)){
//				CoverageCategory covCat = policyOrder.getCoverageCategory();
//				
//				if(covCat!=null  && !StringUtils.isEmpty(covCat.getName()) && !StringUtils.isEmpty(receiverFcmToken)){
//					FcmNotifMessageDto.Notification notifMessage = new FcmNotifMessageDto.Notification();
//					notifMessage.setTitle(messageSource.getMessage("message.notification.order.invite.active.title",
//							new Object[] { covCat.getName() }, Locale.ROOT));
//					notifMessage.setBody(messageSource.getMessage("message.notification.order.invite.active.body",
//							new Object[] { covCat.getName() }, Locale.ROOT));
//					try {
//						notificationService.sendFcmNotification(userId, receiverFcmToken, notifMessage,
//								FcmNotifAction.order, policyOrder.getOrderId());
//					} catch (Exception e) {
//						logger.error("Failed to send message notif for register order", e);
//					}
//				}	
//			}
//		}
//	}

	private void asyncRegisterOrderForInviter(final PolicyOrder policyOrder){
		logger.debug("Async register order for inviter with order <{}>", policyOrder);
		producerTemplate.to(EndPointRef.QUEUE_ORDER).withBodyAs(policyOrder, PolicyOrder.class).send();
	}
	
	public void registerOrderForInviter(final PolicyOrder policyOrder) throws Exception {		
		logger.info("Process registerOrder for inviter, orderInvitee: <{}>",
				policyOrder);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();
		
		if (policyOrder == null || policyOrder.getPolicyOrderVoucher() == null
				|| policyOrder.getPolicyOrderVoucher().getVoucher() == null) {
			throw new Exception("Failed to register order for inviter cause empty voucher");
		}
		
		//return if there is no more reward available
		if (policyOrder.getPolicyOrderVoucher().getVoucher().getInviterRewardCount() >= 
				policyOrder.getPolicyOrderVoucher().getVoucher().getInviterRewardLimit()) {
			return;
		}
		
		final User inviterUser = userService.fetchByUserId(policyOrder.getPolicyOrderVoucher().getVoucher().getInviterUserId());		
		if(inviterUser==null){
			throw new Exception("Failed to register order for inviter cause user not found");
		}
		
		
		PolicyOrder inviterPolicy = new PolicyOrder();
		inviterPolicy.setOrderId(orderService.generateOrderId());
		inviterPolicy.setOrderDate(today);
		inviterPolicy.setOrderTime(now);
		inviterPolicy.setUserId(inviterUser.getUserId());
		inviterPolicy.setCoverageCategoryId(policyOrder.getCoverageCategoryId());
		inviterPolicy.setCoverageCategory(policyOrder.getCoverageCategory());
		inviterPolicy.setHasBeneficiary(policyOrder.getHasBeneficiary());
		inviterPolicy.setHasVoucher(policyOrder.getHasVoucher());
		inviterPolicy.setPeriodId(policyOrder.getPeriodId());
		inviterPolicy.setBasePremi(policyOrder.getBasePremi());
		inviterPolicy.setTotalPremi(policyOrder.getTotalPremi());
		inviterPolicy.setProductCount(policyOrder.getProductCount());
		inviterPolicy.setPeriod(policyOrder.getPeriod());
		inviterPolicy.setStatus(PolicyStatus.APPROVED);
		
		
		PolicyOrderUsers inviterPolicyUser = new PolicyOrderUsers();
		inviterPolicyUser.setOrderId(inviterPolicy.getOrderId());
		inviterPolicyUser.setEmail(inviterUser.getEmail());
		inviterPolicyUser.setIdCardFileId(inviterUser.getIdCardFileId());
		inviterPolicyUser.setPassportFileId(inviterUser.getPassportFileId());
		inviterPolicyUser.setName(inviterUser.getName());
		inviterPolicyUser.setGender(inviterUser.getGender());
		inviterPolicyUser.setBirthDate(inviterUser.getBirthDate());
		inviterPolicyUser.setBirthPlace(inviterUser.getBirthPlace());
		inviterPolicyUser.setAddress(inviterUser.getAddress());
		inviterPolicyUser.setPhone(inviterUser.getPhone());
		
		inviterPolicy.setPolicyOrderUsers(inviterPolicyUser);
		
		List<PolicyOrderProduct> policyOrderProducts = new ArrayList<>();
		List<String> coverageIds = new ArrayList<>();
		for(PolicyOrderProduct p: policyOrder.getPolicyOrderProducts()){
			PolicyOrderProduct inviterPop = new PolicyOrderProduct();
			inviterPop.setOrderId(inviterPolicy.getOrderId());
			inviterPop.setCoverageId(p.getCoverageId());
			inviterPop.setPeriodId(p.getPeriodId());
			inviterPop.setProductId(p.getProductId());
			inviterPop.setProductName(p.getProductName());
			inviterPop.setCoverageName(p.getCoverageName());
			inviterPop.setCoverageMaxLimit(p.getCoverageMaxLimit());
			inviterPop.setBasePremi(p.getBasePremi());
			inviterPop.setPremi(p.getPremi());
			inviterPop.setCoverageHasBeneficiary(p.getCoverageHasBeneficiary());
			inviterPop.setPeriod(p.getPeriod());
			policyOrderProducts.add(inviterPop);
			
			coverageIds.add(p.getCoverageId());
		}
		inviterPolicy.setPolicyOrderProducts(policyOrderProducts);
		
		PolicyOrderVoucher inviterVoucher = new PolicyOrderVoucher();
		inviterVoucher.setOrderId(inviterPolicy.getOrderId());
		inviterVoucher.setVoucher(policyOrder.getPolicyOrderVoucher().getVoucher());
		inviterPolicy.setPolicyOrderVoucher(inviterVoucher);
		
		//get start date and end date			
//		LocalDate maxExistingPolicyEndDate = orderService.fetchMaxPolicyEndDateByCoverage(inviterUser.getUserId(), today, coverageIds);
//		LocalDate inviterPolicyStartDate = null;
//		if(maxExistingPolicyEndDate==null){
//			inviterPolicyStartDate = today;
//		}else{
//			inviterPolicyStartDate = maxExistingPolicyEndDate.plusDays(1);
//		}
		LocalDate inviterPolicyStartDate = orderService.resolveInviterPolicyStartDate(inviterUser.getUserId(), coverageIds, today);
		LocalDate inviterPolicyEndDate = orderService.calculatePolicyEndDate(inviterPolicyStartDate, policyOrder.getPeriod());
		
		inviterPolicy.setPolicyStartDate(inviterPolicyStartDate);
		inviterPolicy.setPolicyEndDate(inviterPolicyEndDate);

		try {
			insuranceService.orderPolicy(inviterPolicy);
		} catch (AppInternalServerErrorException e) {
			logger.error("Process registerOrder for inviter, user:<{}>, order:<{}>, result: exception provider <{}>",
					policyOrder, inviterUser, e.getCode());
			throw e;
		}		
		
		policyOrderTrxService.registerPolicyOrder(inviterPolicy);
		
		//increase success counter
		voucherService.increaseInviterRewardCounter(inviterVoucher.getVoucher().getCode(), inviterUser.getUserId());
		
		logger.debug("Process registerOrder for inviter, from <{}> to order: <{}> with result success",
				policyOrder, inviterPolicy);
	}

	public List<OrderDocumentDto> requiredOrderDocumentDtos(OrderDto orderDto) {
		// TODO Auto-generated method stub
		//if it is selfie, get coverage object, return the 
		Map<String, OrderDocumentDto> orderDocumentDtoMaps = new LinkedHashMap<>();
		if(orderDto!=null) {
			if(orderDto.getProducts()!=null) {
				for (ProductDto productDto: orderDto.getProducts()) {
					Product product = productService.fetchProductByProductId(productDto.getProductId());
					if (product.getCoverage().getCoverageOrderDocTypes()!=null) {
						for(CoverageOrderDocType coverageOrderdocType: product.getCoverage().getCoverageOrderDocTypes()) {
							if(!orderDocumentDtoMaps.containsKey(coverageOrderdocType.getOrderDocType().getOrderDocTypeId())) {
								OrderDocumentDto dto = new OrderDocumentDto();
								dto.setOrderDocType(modelMapperAdapter.toDto(coverageOrderdocType.getOrderDocType()));
								orderDocumentDtoMaps.put(coverageOrderdocType.getOrderDocType().getOrderDocTypeId(), dto);								
							}
						}
					}
				}
			}
			
		}
		
		if(!orderDocumentDtoMaps.isEmpty()) {
			List<OrderDocumentDto> orderDocumentList = orderDocumentDtoMaps.values().stream()
					.sorted((a, b) -> a.getOrderDocType().getDisplayRank().compareTo(b.getOrderDocType().getDisplayRank()))
					.collect(Collectors.toList()); 
			
			return orderDocumentList;
		}else {
			return new ArrayList<OrderDocumentDto>();
		}
		
		
		//return Collections.sort( (List<T>) new ArrayList<OrderDocumentDto>());
		//return null;
	}
	
	public List<PolicyOrder> fetchOrders(final String userId, final FilterDto filter){
		int offset = orderService.getDefaultOrdersFilterOffset();
		int limit = orderService.getDefaultOrdersFilterLimit();
		String[] filterStatus = null;
		if(filter!=null){
			offset = filter.getOffset();
			limit = filter.getLimit();
			filterStatus = filter.getStatus();
		}
		OrderDtoFilterStatus filterType = getFetchOrderFilterType(filterStatus);
		List<PolicyOrder> orders = null;
		if(filterType.equals(OrderDtoFilterStatus.ALL)){
			orders = orderService.fetchOrdersByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.ACTIVE)){
			orders = orderService.fetchOrdersWhereStatusActiveByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.APPROVED)){
			orders = orderService.fetchOrdersWhereStatusApprovedByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.EXPIRED)){
			orders = orderService.fetchOrdersWhereStatusExpiredOrTerminatedByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.UNPAID)){
			orders = orderService.fetchOrdersWhereStatusBeforeApprovedByUserId(userId, limit, offset);
		}		
		
		return orders;
	}

	protected OrderDtoFilterStatus getFetchOrderFilterType(String[] status){
		OrderDtoFilterStatus filterType = OrderDtoFilterStatus.ALL;
		if(status!=null && status.length > 0){
			if(!StringUtils.isEmpty(status[0])){
				if(status.length==1 && status[0].equals(PolicyStatus.ACTIVE.toStr())){
					filterType = OrderDtoFilterStatus.ACTIVE;
				}else if(status.length==1 && status[0].equals(PolicyStatus.APPROVED.toStr())){
					filterType = OrderDtoFilterStatus.APPROVED;
				}else if(status.length>=2 
						&& (status[0].equals(PolicyStatus.EXPIRED.toStr()) 
								||status[1].equals(PolicyStatus.EXPIRED.toStr()))
						){
					filterType = OrderDtoFilterStatus.EXPIRED;
				}else{
					filterType = OrderDtoFilterStatus.UNPAID;
				}				
			}
		}
		return filterType;
	}

	public void deleteOrder(String authUserId, String orderId) throws AppException {
		PolicyOrder order = orderService.fetchOrderByOrderId(authUserId, orderId);
				
		if(order!=null) {
			//check if order is allowed to be hidden
			if(order.getStatus().equals(PolicyStatus.OVERDUE)
					|| order.getStatus().equals(PolicyStatus.TERMINATED)
					|| (order.getStatus().equals(PolicyStatus.EXPIRED) &&
							!claimService.isPolicyEndDateWithinClaimPeriod(LocalDate.now(), order.getPolicyEndDate()))) {
				
				orderService.hideOrder(order);
			}else {
				logger.error("Process deleteOrder, user:<{}>, order:<{}>, result: exception order status <{}>",
						authUserId, orderId, ErrorCode.ERR4404_DELETE_INVALID_ORDER_STATUS);
				throw new AppBadRequestException(ErrorCode.ERR4404_DELETE_INVALID_ORDER_STATUS, "Pesanan tidak dapat dihapus");
			}
		}else {
			throw new AppBadRequestException(ErrorCode.ERR5001_ORDER_NOT_FOUND, "Pesanan tidak ditemukan");
		}
		
	}
}
