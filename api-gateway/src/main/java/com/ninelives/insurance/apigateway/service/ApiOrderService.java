package com.ninelives.insurance.apigateway.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.FluentProducerTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.apigateway.adapter.ModelMapperAdapter;
import com.ninelives.insurance.apigateway.dto.FilterDto;
import com.ninelives.insurance.apigateway.dto.OrderDto;
import com.ninelives.insurance.apigateway.dto.PolicyOrderBeneficiaryDto;
import com.ninelives.insurance.apigateway.model.RegisterUsersResult;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.InsuranceService;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.core.service.VoucherService;
import com.ninelives.insurance.core.trx.PolicyOrderTrxService;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.PolicyOrderVoucher;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class ApiOrderService {
	private static final Logger logger = LoggerFactory.getLogger(ApiOrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired OrderService orderService;
	@Autowired ProductService productService;
	@Autowired UserService userService;
	@Autowired VoucherService voucherService;	
	@Autowired PolicyOrderTrxService policyOrderTrxService;
	@Autowired NotificationService notificationService;
	@Autowired InsuranceService insuranceService;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired MessageSource messageSource;
	@Autowired FluentProducerTemplate producerTemplate;
	
	public OrderDto submitOrder(RegisterUsersResult registerResult, String voucherCode) 
			throws AppException{
		PolicyOrder policyOrder = registerOrder(registerResult, voucherCode);
		return modelMapperAdapter.toDto(policyOrder);
				
	}
	

	protected PolicyOrder registerOrder(RegisterUsersResult registerResult, String voucherCode) 
			throws AppException{
		
		logger.debug("Process order isvalidationonly <{}> user: <{}> with order: <{}>",
				registerResult.getUserDto().getUserId(), registerResult);
		
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();		
		
		String userId = registerResult.getUserDto().getUserId();
		
		//order with voucher		
		Map<String, Product> voucherProductMap = null;
		Set<String> voucherProductIdSet = null;
		Voucher voucher = validateVoucher(userId, voucherCode);
		if(voucher!=null){
			voucherProductMap = voucher.getProducts().stream().
					collect(Collectors.toMap(Product::getProductId, item -> item)); 
			voucherProductIdSet = voucherProductMap.keySet();			
		}
				
		LocalDate limitPolicyStartDate = today.plusDays(config.getOrder().getPolicyStartDatePeriod());
		if(voucher.getPolicyStartDate().isAfter(limitPolicyStartDate)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date exceed limit {}", userId, config.getOrder().getPolicyStartDatePeriod());
			throw new AppBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");			
		}
		if(voucher.getPolicyStartDate().isBefore(today)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date before today", userId );
			throw new AppBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan periksa tanggal mulai asuransi Anda.");			
			
		}
		
		List<Product> products = voucherProductIdSet.stream().map(productService::fetchProductByProductId).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(products)||products.get(0)==null){
			logger.debug("Process order for {} with order {} with result: exception product not found", userId);
			throw new AppBadRequestException(ErrorCode.ERR4003_ORDER_PRODUCT_NOTFOUND, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk.");
		}
	
		String periodId = products.get(0).getPeriodId();
		String coverageCategoryId = products.get(0).getCoverage().getCoverageCategoryId();
		int calculatedTotalPremi = 0;
		int calculatedTotalBasePremi = 0;
		boolean hasBeneficiary = false;
		List<String> coverageIds = new ArrayList<>();
		for(Product p: products){
			if(!periodId.equals(p.getPeriodId())){
				logger.debug("Process order for {} with order {} with result: exception period mismatch", userId);
				throw new AppBadRequestException(ErrorCode.ERR4004_ORDER_PERIOD_MISMATCH, "Permintaan tidak dapat diproses, silahkan cek kembali periode asuransi.");
			}
			if(!coverageCategoryId.equals(p.getCoverage().getCoverageCategoryId())){
				logger.debug("Process order for {} with order {} with result: exception coverage mismatch", userId);
				throw new AppBadRequestException(ErrorCode.ERR4006_ORDER_COVERAGE_MISMATCH, "Permintaan tidak dapat diproses, pembelian lebih dari satu tipe asuransi belum didukung.");
			}
			
			calculatedTotalPremi += p.getPremi();
			calculatedTotalBasePremi += p.getBasePremi();
			hasBeneficiary = hasBeneficiary || p.getCoverage().getHasBeneficiary();
			coverageIds.add(p.getCoverageId());
		}
			
		calculatedTotalPremi = voucher.getTotalPremi();
		Period period = products.get(0).getPeriod();
		
		if(voucher == null){
			if(calculatedTotalPremi < config.getOrder().getMinimumPayment()){
				logger.debug("Process order for {} with order {} with result: exception minimum payment", userId);
				throw new AppBadRequestException(ErrorCode.ERR4019_ORDER_PAYMENT_MINIMUM,
							"Jumlah minimum transaksi adalah Rp. 5.000.");
			}
		}
		
		LocalDate policyEndDate = orderService.calculatePolicyEndDate(voucher.getPolicyStartDate(), 
				products.get(0).getPeriod());
		LocalDate dueOrderDate = today.minusDays(config.getOrder().getPolicyDueDatePeriod());
		
		boolean isOverLimit = orderService.isOverCoverageInSamePeriodLimit(userId, 
				voucher.getPolicyStartDate(), policyEndDate, dueOrderDate, coverageIds, coverageCategoryId);
		if(isOverLimit){
			logger.debug("Process order for {} with order {} with result: exception conflict coverage", userId);
			throw new AppBadRequestException(ErrorCode.ERR4009_ORDER_PRODUCT_CONFLICT,
						"Anda membeli jaminan yang sama lebih dari 3 kali untuk periode yang sama. "
						+ "Silakan atur kembali periode pemakaian jaminan.");
		}
				
		PolicyOrder policyOrder = null;
		boolean isAllProfileInfoUpdated = false;
		boolean isPhoneInfoUpdated = false;
		boolean isAddressInfoUpdated = false;
		
		final User existingUser = userService.fetchByUserId(userId);
		boolean isExistingUserProfileCompleteForOrder = orderService.isUserProfileCompleteForOrder(existingUser);
		
		//verify age
		LocalDate birthDate = existingUser.getBirthDate();
		if(birthDate == null){
				if(registerResult.getUserDto()!=null && registerResult.getUserDto().getBirthDate()!=null){
					birthDate = registerResult.getUserDto().getBirthDate().toLocalDate();
			}
		}
		
		if(birthDate != null){
			long age = ChronoUnit.YEARS.between(birthDate, today);
				if(age > config.getOrder().getMaximumAge()||
						age < config.getOrder().getMinimumAge()){
					logger.debug("Process order for {} with order {} with result: age invalid", userId);
					throw new AppBadRequestException(ErrorCode.ERR4018_ORDER_PROFILE_AGE_INVALID,
							"Produk ini hanya tersedia untuk usia 17 sampai 60 tahun.");
				}				
			}
			
		//modify phone
		String modifiedPhone = null;
		if(registerResult.getUserDto()!=null && !StringUtils.isEmpty(registerResult.getUserDto().getPhone())){
				modifiedPhone = registerResult.getUserDto().getPhone().replaceAll("\\D+", "");
		}
			
		User newUserProfile = null;			
		if(!isExistingUserProfileCompleteForOrder){
				//in case existing profile is not complete, check the submit data
				if(registerResult.getUserDto()==null){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId);
					throw new AppBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan.");
				}
				 
				newUserProfile = new User();
				newUserProfile.setUserId(userId);;
				newUserProfile.setName(registerResult.getUserDto().getName());
				newUserProfile.setGender(registerResult.getUserDto().getGender());
				
				if(registerResult.getUserDto().getBirthDate()==null){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId);
					throw new AppBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan.");
				}
				else{
					newUserProfile.setBirthDate(registerResult.getUserDto().getBirthDate().toLocalDate());
				}
				
				newUserProfile.setBirthPlace(registerResult.getUserDto().getBirthPlace());
				newUserProfile.setAddress(registerResult.getUserDto().getAddress());
				newUserProfile.setPhone(modifiedPhone);
				
				if(!orderService.isUserProfileCompleteForOrder(newUserProfile)){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId);
					throw new AppBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi Anda untuk melanjutkan pemesanan.");
				}
				
				userService.updateProfileInfo(newUserProfile);
				
				isAllProfileInfoUpdated = true;
			}else{
				//incase existing profile exists, allow update phone or address
				if(registerResult.getUserDto()!=null){
					User updateUser = new User();
					updateUser.setUserId(userId);
					
					if(modifiedPhone!=null && !existingUser.getPhone().equals(modifiedPhone)){
						updateUser.setPhone(modifiedPhone);
						isPhoneInfoUpdated = true;
					}
					if(!StringUtils.isEmpty(registerResult.getUserDto().getAddress()) 
							&& !registerResult.getUserDto().getAddress().equals(existingUser.getAddress())){
						updateUser.setAddress(registerResult.getUserDto().getAddress());
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
			policyOrder.setPolicyStartDate(voucher.getPolicyStartDate());
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
			policyOrderUser.setIdCardNo(registerResult.getUserDto().getIdCardNo());

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
				policyOrderUser.setAddress(existingUser.getAddress());
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
				
				if(voucher.getTotalPremi().equals(0)){
						policyOrder.setStatus(PolicyStatus.PAID);
				}
			}else{
				policyOrder.setHasVoucher(false);
			}
			
			try {
				insuranceService.orderPolicy(policyOrder);
			} catch (AppInternalServerErrorException e) {
				logger.error("Process registerOrder for user:<{}> with order:<{}>, result: exception provider <{}>",
						userId, e.getCode());
				throw e;
			}
			
			policyOrderTrxService.registerPolicyOrder(policyOrder);
						
			logger.debug("Process registerOrder isValidateOnly <{}> for <{}> with order <{}>, "
					+ "result update profile: <{}>, update phone: <{}>, order: <{}> with result success",
				 userId,  isAllProfileInfoUpdated, isPhoneInfoUpdated, policyOrder);
		
		return policyOrder;
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
		
		return beneficiary;
	}
	
	
	
	protected Voucher validateVoucher(final String userId, String voucherCode) 
			throws AppBadRequestException{
		
		Voucher voucher = null;
		if(userId!=null && voucherCode!=null){
			try {
				voucher = voucherService.fetchVoucherByCode(voucherCode, VoucherType.B2B, false);
			} catch (AppNotFoundException e) {
				logger.debug("Process order for user: <{}> with order <{}> with result: voucher not found <{}>", 
						userId,	voucherCode, e.getCode());								
				throw new AppBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
						"Permintaan tidak dapat diproses, silahkan cek kode asuransi gratis Anda.");
			}
			
			logger.info("Validate voucher, process order for user: <{}> with order: <{}> "
					+ "with voucher: <{}>", userId, voucherCode, voucher);
			
			//check over limit case
			if(!voucherService.isUsable(voucher)){
				boolean isUsable = false;
				
				if(!isUsable){
					logger.debug("Process order for user: <{}> with order <{}> with result: voucher unusable", 
							userId,	voucherCode);
					throw new AppBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
								"Kode ini sudah tidak tersedia.");
				}
	
			}
			
		}
				
		return voucher;
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
		LocalDate maxExistingPolicyEndDate = orderService.fetchMaxPolicyEndDateByCoverage(inviterUser.getUserId(), today, coverageIds);
		LocalDate inviterPolicyStartDate = null;
		if(maxExistingPolicyEndDate==null){
			inviterPolicyStartDate = today;
		}else{
			inviterPolicyStartDate = maxExistingPolicyEndDate.plusDays(1);
		}
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

	public OrderDtoFilterStatus getFetchOrderFilterType(String[] status){
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
	
	public Voucher validateVoucherB2B(String b2bCode, VoucherType voucherType) throws AppBadRequestException{
		Voucher voucher = null;
			try {
				voucher = voucherService.fetchVoucherByCode(b2bCode, voucherType, false);
			} catch (AppNotFoundException e) {
				throw new AppBadRequestException(ErrorCode.ERR4011_ORDER_VOUCHER_NOTFOUND,
						"Permintaan tidak dapat diproses, silahkan cek kode asuransi gratis Anda.");
			}
		return voucher;
	}
	

}
