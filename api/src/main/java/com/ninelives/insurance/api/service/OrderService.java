package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.camel.FluentProducerTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.config.NinelivesConfigProperties;
import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderBeneficiaryMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderUsersMapper;
import com.ninelives.insurance.api.service.trx.PolicyOrderTrxService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderCoverage;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyOrderVoucher;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.provider.notification.fcm.ref.FcmNotifAction;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.ref.PeriodUnit;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.ProductType;
import com.ninelives.insurance.ref.VoucherType;
import com.ninelives.insurance.route.EndPointRef;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired NinelivesConfigProperties config;
	@Autowired ProductService productService;
	@Autowired UserService userService;
	@Autowired VoucherService voucherService;
	@Autowired InviteService inviteService;
	@Autowired PolicyOrderTrxService policyOrderTrxService;
	@Autowired NotificationService notificationService;
	@Autowired InsuranceService insuranceService;
	
	@Autowired PolicyOrderMapper policyOrderMapper;
	@Autowired PolicyOrderUsersMapper policyOrderUserMapper;
	@Autowired PolicyOrderProductMapper policyOrderProductMapper; 
	@Autowired PolicyOrderBeneficiaryMapper policyOrderBeneficiaryMapper;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Autowired MessageSource messageSource;
	
	@Autowired FluentProducerTemplate producerTemplate;
	
//	@Value("${ninelives.order.policy-startdate-period:366}")
//	int policyStartDatePeriod;
//	
//	@Value("${ninelives.order.policy-duedate-period:30}")
//	int policyDueDatePeriod;
//	
//	@Value("${ninelives.order.policy-conflict-period-limit:3}")
//	int policyConflictPeriodLimit;
	
//	@Value("${ninelives.order.policy-title}")
//	String policyTitle;
//	
//	@Value("${ninelives.order.policy-imgUrl}")
//	String policyImgUrl;
//	
	@Value("${ninelives.order.filter-limit:100}")
	int defaultOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-max-limit:100}")
	int maxOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-offset:0}")
	int defaultOrdersFilterOffset;
	
	
	public PolicyOrderBeneficiaryDto updateBeneficiary(String authUserId, String orderId,
			PolicyOrderBeneficiaryDto beneficiary) throws ApiException {
		PolicyOrderBeneficiary policyOrderBeneficiary = registerBeneficiary(authUserId, orderId, beneficiary);
		return modelMapperAdapter.toDto(policyOrderBeneficiary);
	}
	
	public OrderDto submitOrder(final String userId, final OrderDto orderDto, final boolean isValidateOnly) throws ApiException{
		PolicyOrder policyOrder = registerOrder(userId, orderDto, isValidateOnly);
		return modelMapperAdapter.toDto(policyOrder);
	}
	
	public OrderDto fetchOrderDtoByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = fetchOrderByOrderId(userId, orderId);
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
			PolicyOrderBeneficiaryDto beneficiaryDto) throws ApiException{

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
			throw new ApiBadRequestException(ErrorCode.ERR4101_BENEFICIARY_INVALID,
					"Permintaan tidak dapat diproses, silahkan cek kembali data penerima");
		}
		PolicyOrder policyOrder = fetchOrderWithBeneficiaryByOrderId(userId, orderId);
		if(policyOrder == null){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception order not found>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR5001_ORDER_NOT_FOUND);
			throw new ApiBadRequestException(ErrorCode.ERR5001_ORDER_NOT_FOUND,
					"Permintaan tidak dapat diproses, data pemesanan tidak ditemukan");
		}		
		if(!policyOrder.getHasBeneficiary()){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception order doesnt require beneficiary>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4103_BENEFICIARY_NOTACCEPTED);
			throw new ApiBadRequestException(ErrorCode.ERR4103_BENEFICIARY_NOTACCEPTED,
					"Permintaan tidak dapat diproses, pemesanan Anda tidak membutuhkan data penerima");
		}
		if(policyOrder.getPolicyOrderBeneficiary()!=null){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<exception beneficiary already exists>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4102_BENEFICIARY_EXISTS);
			throw new ApiBadRequestException(ErrorCode.ERR4102_BENEFICIARY_EXISTS,
					"Permintaan tidak dapat diproses, daftar penerima sudah didaftarkan untuk asuransi ini");
		}
		if(policyOrder.getStatus().equals(PolicyStatus.TERMINATED)||policyOrder.getStatus().equals(PolicyStatus.EXPIRED)){
			logger.info("Process registerBeneficiary, userId:<{}>, orderId:<{}>, beneficiary:<{}>, result:<cannot update expired/terminated order>, error:<{}>", userId,
					orderId, beneficiaryDto==null?"":beneficiaryDto, ErrorCode.ERR4102_BENEFICIARY_EXISTS);
			throw new ApiBadRequestException(ErrorCode.ERR4104_BENEFICIARY_ORDER_STATUS,
					"Permintaan tidak dapat diproses, masa aktif asuransi Anda telah melewati periode");
		}
		
		PolicyOrderBeneficiary beneficiary = new PolicyOrderBeneficiary();
		beneficiary.setOrderId(orderId);
		beneficiary.setName(beneficiaryDto.getName());
		beneficiary.setEmail(beneficiaryDto.getEmail());
		beneficiary.setPhone(beneficiaryDto.getPhone());
		beneficiary.setRelationship(beneficiaryDto.getRelationship());
		
		policyOrderBeneficiaryMapper.insert(beneficiary);
		
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
			
			
		}else if(!isEquals(beneficiary, userBeneficiary)){
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
	
	protected boolean isEquals(PolicyOrderBeneficiary pob, UserBeneficiary ub){
		if(pob.getName()==null){
			if(ub.getName()!=null){
				return false;
			}
		}else if(!pob.getName().equals(ub.getName())){
			return false;		
		}
		if(pob.getEmail()==null){
			if(ub.getEmail()!=null){
				return false;
			}
		}else if(!pob.getEmail().equals(ub.getEmail())){
			return false;		
		}
		if(pob.getPhone()==null){
			if(ub.getPhone()!=null){
				return false;
			}
		}else if(!pob.getPhone().equals(ub.getPhone())){
			return false;		
		}
		if(pob.getRelationship()==null){
			if(ub.getRelationship()!=null){
				return false;
			}
		}else if(!pob.getRelationship().equals(ub.getRelationship())){
			return false;		
		}		
		
		return true;
	}
	
	/**
	 * 
	 * @param userId
	 * @param submitOrderDto
	 * @param isValidateOnly Dry-run. Validate if order is valid (without validation to the user's profile).
	 * @return
	 * @throws ApiException 
	 */
	protected PolicyOrder registerOrder(final String userId, final OrderDto submitOrderDto, final boolean isValidateOnly) throws ApiException{
		logger.debug("Process order isvalidationonly <{}> user: <{}> with order: <{}>", isValidateOnly, userId, submitOrderDto);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();		
		
		if (submitOrderDto == null || submitOrderDto.getProducts() == null
				|| submitOrderDto.getPolicyStartDate() == null || submitOrderDto.getTotalPremi() == null) {
			logger.debug("Process order for {} with order {} with result: exception empty order or product", 
					userId,	submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4000_ORDER_INVALID,
					"Permintaan tidak dapat diproses, silahkan cek kembali pesanan");
		}
		
		//order with voucher
		Voucher voucher = null;
		Map<String, Product> voucherProductMap = null;
		Set<String> voucherProductIdSet = null;
		if(submitOrderDto.getVoucher()!=null){
			try {
				voucher = voucherService.fetchVoucherByCode(submitOrderDto.getVoucher().getCode(), submitOrderDto.getVoucher().getVoucherType());
			} catch (ApiNotFoundException e) {
				logger.debug("Process order for user: <{}> with order <{}> with result: voucher not found <{}>", 
						userId,	submitOrderDto, e.getCode());
				throw new ApiBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
						"Permintaan tidak dapat diproses, silahkan cek kode asuransi gratis Anda");
			}
			if(VoucherType.INVITE.equals(voucher.getVoucherType())){
				//verify that user is eligible
				if(hasPaidOrder(userId)){
					logger.debug("Process order for user: <{}> with order <{}> with result: voucher not eligible", 
							userId,	submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4012_ORDER_VOUCHER_NOTELIGIBLE,
							"Permintaan tidak dapat diproses, asuransi gratis ini hanya dapat digunakan oleh pengguna baru");
				}
				
			}
			if(!voucher.getTotalPremi().equals(submitOrderDto.getTotalPremi())){
				logger.debug("Process order for user: <{}> with order <{}> with result: voucher premi not match, voucher: <{}>", 
						userId,	submitOrderDto, voucher.getTotalPremi());
				throw new ApiBadRequestException(ErrorCode.ERR4013_ORDER_VOUCHER_PREMI_MISMATCH,
						"Permintaan tidak dapat diproses, premi voucher tidak sesuai dengan pemesanan");
			}
			if(!voucher.getPolicyStartDate().isEqual(submitOrderDto.getPolicyStartDate().toLocalDate())||
					!voucher.getPolicyEndDate().isEqual(submitOrderDto.getPolicyEndDate().toLocalDate())){
				throw new ApiBadRequestException(ErrorCode.ERR4014_ORDER_VOUCHER_DATE_MISMATCH,
						"Permintaan tidak dapat diproses, tanggal asuransi voucher tidak sesuai dengan pemesanan");
			}
			
			//voucherProductIdSet = voucher.getProducts().stream().map(Product::getProductId).collect(Collectors.toSet());
			voucherProductMap = voucher.getProducts().stream().collect(Collectors.toMap(Product::getProductId, item -> item)); 
			voucherProductIdSet = voucherProductMap.keySet();
			
			//logger.debug("voucher with productMap <{}> and productIdSet <{}>", map, voucherProductIdSet);
		}
				
		Set<String> productIdSet = submitOrderDto.getProducts().stream().map(ProductDto::getProductId).collect(Collectors.toSet()); 
				
		if(CollectionUtils.isEmpty(productIdSet)){
			logger.debug("Process order for {} with order {} with result: exception empty product", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4001_ORDER_PRODUCT_EMPTY, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}
		if(submitOrderDto.getProducts().size()!= productIdSet.size()){
			logger.debug("Process order for {} with order {} with result: exception duplicate product", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4002_ORDER_PRODUCT_DUPLICATE, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}
		if(!CollectionUtils.isEmpty(voucherProductIdSet)){
			if(!voucherProductIdSet.equals(productIdSet)){
				logger.debug("Process order for user <{}> with order <{}> with result: exception voucher product mismatch, voucher:<{}>", userId, submitOrderDto, voucherProductIdSet);
				throw new ApiBadRequestException(ErrorCode.ERR4015_ORDER_VOUCHER_PRODUCT_MISMATCH, "Permintaan tidak dapat diproses, produk voucher tidak sesuai dengan pemesanan");	
			}
		}
		
		LocalDate limitPolicyStartDate = today.plusDays(config.getOrder().getPolicyStartDatePeriod());
		if(submitOrderDto.getPolicyStartDate().toLocalDate().isAfter(limitPolicyStartDate)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date exceed limit {}", userId, submitOrderDto, config.getOrder().getPolicyStartDatePeriod());
			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda");			
//
//			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
//					"Permintaan tidak dapat diproses, silahkan pilih tanggal mulai asuransi antara hari ini sampai tanggal "
//							+ limitPolicyStartDate.format(formatter));			
		}
		if(submitOrderDto.getPolicyStartDate().toLocalDate().isBefore(today)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date before today", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda");			
//			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
//					"Permintaan tidak dapat diproses, silahkan pilih tanggal mulai asuransi antara hari ini sampai tanggal "
//							+ limitPolicyStartDate.format(formatter));
			
		}
		
		List<Product> products = productIdSet.stream().map(productService::fetchProductByProductId).collect(Collectors.toList());
		//List<Product> products = productService.fetchProductByProductIds(productIdSet);
		if(CollectionUtils.isEmpty(products)||products.get(0)==null|| products.size()!=submitOrderDto.getProducts().size()){
			logger.debug("Process order for {} with order {} with result: exception product not found", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4003_ORDER_PRODUCT_NOTFOUND, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}
		//logger.debug("products is not empty? empty is {}, and the products <{}> and the size is {}",products.isEmpty(),products, products.size());
		String periodId = products.get(0).getPeriodId();
		String coverageCategoryId = products.get(0).getCoverage().getCoverageCategoryId();
		int calculatedTotalPremi = 0;
		int calculatedTotalBasePremi = 0;
		boolean hasBeneficiary = false;
		List<String> coverageIds = new ArrayList<>();
		for(Product p: products){
			//logger.debug("CHECK {}", p);
			if(!periodId.equals(p.getPeriodId())){
				logger.debug("Process order for {} with order {} with result: exception period mismatch", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4004_ORDER_PERIOD_MISMATCH, "Permintaan tidak dapat diproses, silahkan cek kembali periode asuransi");
			}
			if(!coverageCategoryId.equals(p.getCoverage().getCoverageCategoryId())){
				logger.debug("Process order for {} with order {} with result: exception coverage mismatch", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4006_ORDER_COVERAGE_MISMATCH, "Permintaan tidak dapat diproses, pembelian lebih dari satu tipe asuransi belum didukung");
			}
			//cannot by free product without voucher
			if(ProductType.FREE.equals(p.getProductType()) && voucher==null){
				logger.debug("Process order for {} with order {} with result: exception voucher needed", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4016_ORDER_VOUCHER_REQUIRED, "Permintaan tidak dapat diproses, produk ini hanya dapat dibeli lewat undangan");
			}
			calculatedTotalPremi += p.getPremi();
			calculatedTotalBasePremi += p.getBasePremi();
			hasBeneficiary = hasBeneficiary || p.getCoverage().getHasBeneficiary();
			coverageIds.add(p.getCoverageId());
		}
		if(voucher == null){
			if(calculatedTotalPremi!=submitOrderDto.getTotalPremi()){
				logger.debug("Process order for {} with order {} with result: exception calculated premi {} ", userId, submitOrderDto, calculatedTotalPremi);
				throw new ApiBadRequestException(ErrorCode.ERR4005_ORDER_PREMI_MISMATCH, "Permintaan tidak dapat diproses");
			}
		}else{
			calculatedTotalPremi = voucher.getTotalPremi();
		}
		
		Period period = products.get(0).getPeriod();
//		if(!period.getUnit().equals(PeriodUnit.DAY)){
//			logger.debug("Process order for {} with order {} with result: exception period unit {}", userId, submitOrderDto, period.getUnit());
//			throw new ApiBadRequestException(ErrorCode.ERR4008_ORDER_PRODUCT_UNSUPPORTED, "Permintaan tidak dapat diproses");
//		}
		
		//LocalDate policyEndDate = submitOrderDto.getPolicyStartDate().toLocalDate().plusDays(products.get(0).getPeriod().getValue()-1);
		
		LocalDate policyEndDate = calculatePolicyEndDate(submitOrderDto.getPolicyStartDate().toLocalDate(), products.get(0).getPeriod());
		LocalDate dueOrderDate = today.minusDays(config.getOrder().getPolicyDueDatePeriod());
		
		boolean isOverLimit = isOverCoverageInSamePeriodLimit(userId, submitOrderDto.getPolicyStartDate().toLocalDate(), policyEndDate, dueOrderDate, coverageIds);
		if(isOverLimit){
			logger.debug("Process order for {} with order {} with result: exception conflict coverage", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4009_ORDER_PRODUCT_CONFLICT,
					"Permintaan tidak dapat diproses, Anda telah memiliki 3 asuransi yang akan atau telah aktif pada waktu yang sama");
		}
				
		PolicyOrder policyOrder = null;
		boolean isAllProfileInfoUpdated = false;
		boolean isPhoneInfoUpdated = false;
		boolean isAddressInfoUpdated = false;
		if(!isValidateOnly){
			final User existingUser = userService.fetchByUserId(userId);

			boolean isExistingUserProfileCompleteForOrder = isUserProfileCompleteForOrder(existingUser);
			
			if(existingUser.getIdCardFileId()==null){
				logger.debug("Process order for <{}> with order <{}> with result: id card not found", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4017_ORDER_IDCARD_NOTFOUND,
						"Permintaan tidak dapat diproses, unggah KTP Anda untuk melanjutkan pemesanan");
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
				long age = ChronoUnit.YEARS.between(birthDate, today);
				if(age > config.getOrder().getMaximumAge()||
						age < config.getOrder().getMinimumAge()){
					logger.debug("Process order for {} with order {} with result: age invalid", userId, submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
							"Produk ini hanya tersedia untuk usia 17 sampai 60 tahun");
				}				
			}
			//modify phone
			String modifiedPhone = null;
			if(submitOrderDto.getUser()!=null && !StringUtils.isEmpty(submitOrderDto.getUser().getPhone())){
				modifiedPhone = submitOrderDto.getUser().getPhone().replaceAll("\\D+", "");
			}
			
			User newUserProfile = null;			
			if(!isExistingUserProfileCompleteForOrder){
				//in case existing profile is not complete, check the submit data
				if(submitOrderDto.getUser()==null){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan");
				}
				 
				newUserProfile = new User();
				newUserProfile.setUserId(userId);;
				newUserProfile.setName(submitOrderDto.getUser().getName());
				newUserProfile.setGender(submitOrderDto.getUser().getGender());
				newUserProfile.setBirthDate(submitOrderDto.getUser().getBirthDate().toLocalDate());
				newUserProfile.setBirthPlace(submitOrderDto.getUser().getBirthPlace());
				newUserProfile.setAddress(submitOrderDto.getUser().getAddress());
				newUserProfile.setPhone(modifiedPhone);
				
				if(!isUserProfileCompleteForOrder(newUserProfile)){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan");
				}
				
				userService.updateProfileInfo(newUserProfile);
				
				isAllProfileInfoUpdated = true;
			}else{
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
			policyOrder.setOrderId(generateOrderId());
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

			//Set user info
			PolicyOrderUsers policyOrderUser = new PolicyOrderUsers();
			policyOrderUser.setOrderId(policyOrder.getOrderId());
			policyOrderUser.setEmail(existingUser.getEmail());
			policyOrderUser.setIdCardFileId(existingUser.getIdCardFileId());

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
					policyOrderUser.setPhone(submitOrderDto.getUser().getAddress());
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
				pop.setCoverageName(p.getCoverage().getName());
				pop.setCoverageMaxLimit(p.getCoverage().getMaxLimit());
				pop.setBasePremi(p.getBasePremi());
				if(voucher!=null){
					pop.setPremi(voucherProductMap.get(p.getProductId()).getPremi());
				}else{
					pop.setPremi(p.getPremi());
				}				
				pop.setCoverageHasBeneficiary(p.getCoverage().getHasBeneficiary());
				pop.setPeriod(p.getPeriod());
				policyOrderProducts.add(pop);
			}
			
			policyOrder.setPolicyOrderProducts(policyOrderProducts);
			
			//Set for policy voucher
			if(voucher!=null){
				PolicyOrderVoucher pov = new PolicyOrderVoucher();
				pov.setOrderId(policyOrder.getOrderId());
				pov.setVoucher(voucher);
				policyOrder.setPolicyOrderVoucher(pov);
				policyOrder.setHasVoucher(true);
				
				//check if INVITE free voucher, if yes, then mark as APPROVED instead of SUBMITTED
				if(voucher.getTotalPremi().equals(0)){
					if(voucher.getVoucherType().equals(VoucherType.INVITE)){
						policyOrder.setStatus(PolicyStatus.APPROVED);
					}else if(voucher.getVoucherType().equals(VoucherType.B2B )){
						policyOrder.setStatus(PolicyStatus.PAID);
					}
				}				
			}else{
				policyOrder.setHasVoucher(false);
			}
			
			try {
				insuranceService.orderPolicy(policyOrder);
			} catch (ApiInternalServerErrorException e) {
				logger.error("Process registerOrder for user:<{}> with order:<{}>, result: exception provider <{}>",
						userId, submitOrderDto, e.getCode());
				throw e;
			}
			
			policyOrderTrxService.registerPolicyOrder(policyOrder);
						
			if(voucher!=null){
				//if product is active now, send the notification
				sendSuccessNotification(existingUser.getFcmToken(), policyOrder, today);
				
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
			}
			
		}
		logger.debug("Process registerOrder isValidateOnly <{}> for <{}> with order <{}>, result update profile: <{}>, update phone: <{}>, order: <{}> with result success",
				isValidateOnly, userId, submitOrderDto, isAllProfileInfoUpdated, isPhoneInfoUpdated, policyOrder);
		
		return policyOrder;
	}
	private void sendSuccessNotification(String receiverFcmToken, PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null && policyOrder.getCoverageCategory()!=null){
			if(policyOrder.getStatus().equals(PolicyStatus.APPROVED)
					&& !policyOrder.getPolicyStartDate().isAfter(today) 
					&& !policyOrder.getPolicyEndDate().isBefore(today)){
				CoverageCategory covCat = policyOrder.getCoverageCategory();
				
				if(covCat!=null  && !StringUtils.isEmpty(covCat.getName()) && !StringUtils.isEmpty(receiverFcmToken)){
					FcmNotifMessageDto.Notification notifMessage = new FcmNotifMessageDto.Notification();
					notifMessage.setTitle(messageSource.getMessage("message.notification.order.invite.active.title",
							new Object[] { covCat.getName() }, Locale.ROOT));
					notifMessage.setBody(messageSource.getMessage("message.notification.order.invite.active.body",
							new Object[] { covCat.getName() }, Locale.ROOT));
					try {
						notificationService.sendFcmNotification(receiverFcmToken, notifMessage,
								FcmNotifAction.order, policyOrder.getOrderId());
					} catch (Exception e) {
						logger.error("Failed to send message notif for register order", e);
					}
				}	
			}
		}
	}
	private void asyncRegisterOrderForInviter(final PolicyOrder policyOrder){
		logger.debug("Async register order for inviter with order <{}>", policyOrder);
		producerTemplate.to(EndPointRef.QUEUE_ORDER).withBodyAs(policyOrder, PolicyOrder.class).send();
		//producerTemplate.to(DirectEndPointRef.QUEUE_ORDER).withBodyAs(policyOrder, PolicyOrder.class).send();
	}
	
	public void registerOrderForInviter(final PolicyOrder policyOrder) throws Exception {		
		logger.debug("Process registerOrder for inviter, orderInvitee: <{}>",
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
		inviterPolicy.setOrderId(generateOrderId());
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
		LocalDate maxExistingPolicyEndDate = fetchMaxPolicyEndDateByCoverage(inviterUser.getUserId(), today, coverageIds);
		LocalDate inviterPolicyStartDate = null;
		if(maxExistingPolicyEndDate==null){
			inviterPolicyStartDate = today;
		}else{
			inviterPolicyStartDate = maxExistingPolicyEndDate.plusDays(1);
		}
		LocalDate inviterPolicyEndDate = calculatePolicyEndDate(inviterPolicyStartDate, policyOrder.getPeriod());
		
		inviterPolicy.setPolicyStartDate(inviterPolicyStartDate);
		inviterPolicy.setPolicyEndDate(inviterPolicyEndDate);

		//TOOD: Reenable aswata
		try {
			insuranceService.orderPolicy(inviterPolicy);
		} catch (ApiInternalServerErrorException e) {
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
	protected LocalDate fetchMaxPolicyEndDateByCoverage(String userId, LocalDate policyEndDate,
			List<String> coverageIds){
		return policyOrderMapper.selectMaxPolicyEndDateByCoverage(userId, policyEndDate, coverageIds);
	}
	protected LocalDate calculatePolicyEndDate(LocalDate localDate, Period period) {
		LocalDate calculatedDate = null;
		if(period!=null && period.getValue()!=null){
			if(PeriodUnit.DAY.equals(period.getUnit())){
				calculatedDate = localDate.plusDays(period.getValue()).minusDays(1);
			}else if(PeriodUnit.WEEK.equals(period.getUnit())){
				calculatedDate = localDate.plusWeeks(period.getValue()).minusDays(1);
			}else if(PeriodUnit.MONTH.equals(period.getUnit())){
				calculatedDate = localDate.plusMonths(period.getValue()).minusDays(1);
			}else if(PeriodUnit.YEAR.equals(period.getUnit())){
				calculatedDate = localDate.plusYears(period.getValue()).minusDays(1);
			}
		}
		return calculatedDate;
	}

	protected Boolean isUserProfileCompleteForOrder(User user){
		boolean result = true;
		if(user == null 
				|| StringUtils.isEmpty(user.getName()) 
				|| user.getGender()==null
				|| user.getBirthDate()==null
				|| StringUtils.isEmpty(user.getBirthPlace())
				|| StringUtils.isEmpty(user.getPhone())		
				){
			result = false;
		}
		return result;
	}
	
	protected Boolean hasPaidOrder(String userId){
		return policyOrderMapper.selectPaidOrderExists(userId); 
	}
	
	protected boolean isOverCoverageInSamePeriodLimit(String userId, LocalDate policyStartDate, LocalDate policyEndDate,
			LocalDate dueOrderDate, List<String> coverageIds) {
		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
				policyStartDate, policyEndDate, dueOrderDate, coverageIds);		
		
		Map<String, Long> conflictCoverageMap = conflictList.stream().collect(Collectors.groupingBy(PolicyOrderCoverage::getCoverageId, Collectors.counting()));
		
		return  conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=config.getOrder().getPolicyConflictPeriodLimit());
	}
	
	public PolicyOrder fetchOrderWithBeneficiaryByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectWithBeneficiaryByUserIdAndOrderId(userId, orderId);
		postRetrieval(policyOrder, LocalDate.now());
		return policyOrder;
	}
	
	public PolicyOrder fetchOrderByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectByUserIdAndOrderId(userId, orderId);
		postRetrieval(policyOrder,LocalDate.now());
		return policyOrder;
	}
	
	public List<PolicyOrder> fetchOrders(final String userId, final FilterDto filter){
		int offset = this.defaultOrdersFilterOffset;
		int limit = this.defaultOrdersFilterLimit;
		String[] filterStatus = null;
		if(filter!=null){
			offset = filter.getOffset();
			if(filter.getLimit() > this.maxOrdersFilterLimit){
				limit = this.maxOrdersFilterLimit;
			}else{
				limit = filter.getLimit();
			}
			filterStatus = filter.getStatus();
		}
		OrderDtoFilterStatus filterType = getFetchOrderFilterType(filterStatus);
		List<PolicyOrder> orders = null;
		if(filterType.equals(OrderDtoFilterStatus.ALL)){
			orders = policyOrderMapper.selectByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.ACTIVE)){
			orders = policyOrderMapper.selectWhereStatusActiveByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.APPROVED)){
			orders = policyOrderMapper.selectWhereStatusApprovedByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.EXPIRED)){
			orders = policyOrderMapper.selectWhereStatusExpiredOrTerminatedByUserId(userId, limit, offset);
		}else if (filterType.equals(OrderDtoFilterStatus.UNPAID)){
			orders = policyOrderMapper.selectWhereStatusBeforeApprovedByUserId(userId, limit, offset);
		}
		
		LocalDate today = LocalDate.now();		
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,today);
			}
		}
		
		return orders;
	}
	
	protected void postRetrieval(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			mapPolicyOrderStatus(policyOrder,today);
			if(policyOrder.getPolicyOrderProducts()!=null){
				for(PolicyOrderProduct pop: policyOrder.getPolicyOrderProducts()){
					Coverage c = productService.fetchCoverageByCoverageId(pop.getCoverageId()); 
					pop.setCoverageClaimDocTypes(c.getCoverageClaimDocTypes());
					pop.setCoverageDisplayRank(c.getDisplayRank());
				}
			}
		}		
		
	}
	
	protected void mapPolicyOrderStatus(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			if(PolicyStatus.SUBMITTED.equals(policyOrder.getStatus())){
				if(policyOrder.getOrderDate().plusDays(config.getOrder().getPolicyDueDatePeriod()).isBefore(today)){
					policyOrder.setStatus(PolicyStatus.OVERDUE);
				}else if(policyOrder.getPolicyStartDate().isBefore(today)){
					policyOrder.setStatus(PolicyStatus.OVERDUE);
				}
			}else if(PolicyStatus.APPROVED.equals(policyOrder.getStatus())){
				if(!policyOrder.getPolicyStartDate().isAfter(today) && !policyOrder.getPolicyEndDate().isBefore(today)){
					policyOrder.setStatus(PolicyStatus.ACTIVE);
				}else if(policyOrder.getPolicyEndDate().isBefore(today)){
					policyOrder.setStatus(PolicyStatus.EXPIRED);
				}
			}
		}
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
	
//	protected OrderDto policyOrderToDto(final PolicyOrder policyOrder){
//		OrderDto orderDto = null;
//
//		if(policyOrder!=null){
//			orderDto = new OrderDto();
//			orderDto.setOrderId(policyOrder.getOrderId());
//			orderDto.setOrderDate(policyOrder.getOrderDate());
//			orderDto.setPolicyNumber(policyOrder.getPolicyNumber());
//			orderDto.setPolicyStartDate(policyOrder.getPolicyStartDate());
//			orderDto.setPolicyEndDate(policyOrder.getPolicyEndDate());
//			orderDto.setTotalPremi(policyOrder.getTotalPremi());
//			orderDto.setHasBeneficiary(policyOrder.getHasBeneficiary());
//			orderDto.setProductCount(policyOrder.getProductCount());
//			orderDto.setStatus(policyOrder.getStatus());
//			orderDto.setCreatedDate(policyOrder.getCreatedDate());
//			orderDto.setTitle(this.policyTitle);
//			orderDto.setImgUrl(this.policyImgUrl);
//
//			PeriodDto periodDto = new PeriodDto();
//			periodDto.setName(policyOrder.getPeriod().getName());
//			periodDto.setPeriodId(policyOrder.getPeriodId());
//			periodDto.setUnit(policyOrder.getPeriod().getUnit());
//			periodDto.setValue(policyOrder.getPeriod().getValue());
//
//			int rank = 99;
//			List<ProductDto> productDtos = new ArrayList<>();
//			for(PolicyOrderProduct p: policyOrder.getPolicyOrderProducts()){
//				ProductDto dto = new ProductDto();
//				dto.setProductId(p.getProductId());
//				dto.setName(p.getCoverageName());
//				dto.setPremi(p.getPremi());
//				
//				dto.setPeriod(periodDto);
//
//				CoverageDto covDto = new CoverageDto();
//				covDto.setCoverageId(p.getCoverageId());
//				covDto.setName(p.getCoverageName());
//				covDto.setMaxLimit(p.getCoverageMaxLimit());
//				covDto.setHasBeneficiary(p.getCoverageHasBeneficiary());
//				dto.setCoverage(covDto);
//
//				productDtos.add(dto);
//				
//				if(p.getCoverageDisplayRank() < rank){
//					orderDto.setSubtitle(p.getCoverageName());
//					rank = p.getCoverageDisplayRank();
//				}
//				
//				if(!CollectionUtils.isEmpty(p.getClaimDocTypes())){
//					List<ClaimDocTypeDto> docTypeDtos = new ArrayList<>();
//					for(ClaimDocType docType: p.getClaimDocTypes()){
//						ClaimDocTypeDto docTypeDto = new ClaimDocTypeDto();
//						docTypeDto.setClaimDocTypeId(docType.getClaimDocTypeId());
//						docTypeDto.setName(docType.getName());
//						docTypeDtos.add(docTypeDto);
//					}
//					covDto.setClaimDocTypes(docTypeDtos);
//				}
//			}
//			orderDto.setProducts(productDtos);
//			
//			orderDto.setPeriod(periodDto);
//			
//			if(policyOrder.getPolicyOrderUsers()!=null){
//				UserDto userDto = new UserDto();
//				userDto.setUserId(policyOrder.getUserId());
//				userDto.setName(policyOrder.getPolicyOrderUsers().getName());
//				userDto.setBirthDate(policyOrder.getPolicyOrderUsers().getBirthDate());
//				userDto.setBirthPlace(policyOrder.getPolicyOrderUsers().getBirthPlace());
//				userDto.setEmail(policyOrder.getPolicyOrderUsers().getEmail());
//				userDto.setGender(policyOrder.getPolicyOrderUsers().getGender());
//				if(policyOrder.getPolicyOrderUsers().getIdCardFileId()!=null){
//					userDto.setIdCardFile(new UserFileDto(policyOrder.getPolicyOrderUsers().getIdCardFileId()));
//				}				
//				userDto.setPhone(policyOrder.getPolicyOrderUsers().getPhone());
//				userDto.setAddress(policyOrder.getPolicyOrderUsers().getAddress());
//				
//				orderDto.setUser(userDto);
//			}
//			
//		}
//
//		return orderDto;
//		
//	}
	
//	protected PolicyOrderBeneficiaryDto policyOrderBeneficiaryToDto(PolicyOrderBeneficiary policyOrderBeneficiary){
//		PolicyOrderBeneficiaryDto dto = null; 
//		if(policyOrderBeneficiary!=null){
//			dto = new PolicyOrderBeneficiaryDto();
//			dto.setOrderId(policyOrderBeneficiary.getOrderId());
//			dto.setName(policyOrderBeneficiary.getName());
//			dto.setEmail(policyOrderBeneficiary.getEmail());
//			dto.setPhone(policyOrderBeneficiary.getPhone());
//			dto.setRelationship(policyOrderBeneficiary.getRelationship());
//		}
//		return dto;
//	}
	
	
//	
//	public List<PolicyOrderCoverage> testConflict(String userId, final OrderDto submitOrderDto){
//		Set<String> productIdSet = submitOrderDto.getProducts().stream().map(ProductDto::getProductId).collect(Collectors.toSet()); 
//		List<Product> products = productService.fetchProductByProductIds(productIdSet);
//		List<String> coverageIds = new ArrayList<>();
//		for(Product p: products){			
//			coverageIds.add(p.getCoverageId());
//		}
//		
//		LocalDate dueOrderDate = LocalDate.now().minusDays(policyDueDatePeriod);
//		LocalDate policyEndDate = submitOrderDto.getPolicyStartDate().toLocalDate().plusDays(products.get(0).getPeriod().getValue()-1);
//		
//		logger.debug("trx with order-date {} is due today", dueOrderDate);
//		logger.debug("check conflict {} {} {} {}",userId, submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate);
//		List<PolicyOrderCoverage> checklist = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId, submitOrderDto.getPolicyStartDate().toLocalDate(), policyEndDate, dueOrderDate, coverageIds);
//		for(PolicyOrderCoverage item: checklist){
//			logger.debug("Test item {}", item);
//		}
//		return checklist;
//	}
	
//	
//	public List<PolicyOrder> tesFetch(String userId, final FilterDto filter){
//		int offset = this.defaultOrdersFilterOffset;
//		int limit = this.defaultOrdersFilterLimit;
//		String[] filterStatus = null;
//		if(filter!=null){
//			offset = filter.getOffset();
//			if(filter.getLimit() > this.maxOrdersFilterLimit){
//				limit = this.maxOrdersFilterLimit;
//			}else{
//				limit = filter.getLimit();
//			}
//			filterStatus = filter.getStatus();
//		}
//		OrderDtoFilterStatus filterType = getFetchOrderFilterType(filterStatus);
//		List<PolicyOrder> orders = null;
//		if(filterType.equals(OrderDtoFilterStatus.ALL)){
//			orders = policyOrderMapper.selectByUserId(userId, limit, offset);
//		}else if (filterType.equals(OrderDtoFilterStatus.ACTIVE)){
//			orders = policyOrderMapper.selectWhereStatusActiveByUserId(userId, limit, offset);
//		}else if (filterType.equals(OrderDtoFilterStatus.APPROVED)){
//			orders = policyOrderMapper.selectWhereStatusApprovedByUserId(userId, limit, offset);
//		}else if (filterType.equals(OrderDtoFilterStatus.EXPIRED)){
//			orders = policyOrderMapper.selectWhereStatusExpiredOrTerminatedByUserId(userId, limit, offset);
//		}else if (filterType.equals(OrderDtoFilterStatus.UNPAID)){
//			orders = policyOrderMapper.selectWhereStatusBeforeApprovedByUserId(userId, limit, offset);
//		}		
//		
//		LocalDate today = LocalDate.now();
//		if(orders!=null){
//			for(PolicyOrder po: orders){
//				mapPolicyOrderStatus(po,today);
//			}
//		}
//		
//		logger.debug("call testfetch with filter {} and filterType {} and result-size {}", filter, filterType,
//				orders == null ? "0" : String.valueOf(orders.size()));
//		return orders;
//	}
	private String generateOrderId(){
		return UUID.randomUUID().toString();
	}
}
