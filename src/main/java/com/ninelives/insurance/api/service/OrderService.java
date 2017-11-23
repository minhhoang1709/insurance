package com.ninelives.insurance.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.OrderFilterDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.SubmitOrderDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.model.User;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyOrderCoverage;
import com.ninelives.insurance.api.model.PolicyOrderProduct;
import com.ninelives.insurance.api.model.PolicyOrderUsers;
import com.ninelives.insurance.api.mybatis.mapper.OrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderUsersMapper;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.OrderDtoFilterStatus;
import com.ninelives.insurance.api.ref.PolicyStatus;
import com.ninelives.insurance.api.service.trx.PolicyOrderTrxService;
import com.ninelives.insurance.api.ref.PeriodUnit;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired ProductService productService;
	@Autowired UserService userService;
	@Autowired PolicyOrderTrxService policyOrderTrxService;
	
	@Autowired PolicyOrderMapper policyOrderMapper;
	@Autowired PolicyOrderUsersMapper policyOrderUserMapper;
	@Autowired PolicyOrderProductMapper policyOrderProductMapper; 
	@Autowired OrderProductMapper orderProductMapper;
	
	@Value("${ninelives.order.policy-startdate-period:60}")
	int policyStartDatePeriod;
	
	@Value("${ninelives.order.policy-duedate-period:30}")
	int policyDueDatePeriod;
	
	@Value("${ninelives.order.policy-conflict-period-limit:3}")
	int policyConflictPeriodLimit;
	
	//TODO: Replace hardcoded policytitles and imgurl
	
	@Value("${ninelives.order.policy-title}")
	String policyTitle;
	
	@Value("${ninelives.order.policy-imgUrl}")
	String policyImgUrl;
	
	@Value("${ninelives.order.filter-limit:100}")
	int defaultOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-max-limit:100}")
	int maxOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-offset:0}")
	int defaultOrdersFilterOffset;
	
	public OrderDto submitOrder(final String userId, final OrderDto orderDto, final boolean isValidateOnly) throws ApiBadRequestException{
		PolicyOrder policyOrder = registerOrder(userId, orderDto, isValidateOnly);
		return policyOrderToOrderDto(policyOrder);
	}
	
	public OrderDto fetchOrderDtoByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = fetchOrderByOrderId(userId, orderId);
		return policyOrderToOrderDto(policyOrder);
	}
	
	public List<OrderDto> fetchOrderDtos(final String userId, final OrderFilterDto filter){
		ArrayList<OrderDto> orderDtos = new ArrayList<>();
		List<PolicyOrder> orders = fetchOrder(userId, filter);
		if(orders!=null){
			for(PolicyOrder po: orders){
				orderDtos.add(policyOrderToOrderDto(po));
			}
		}
		return orderDtos;		
	}
	/**
	 * 
	 * @param userId
	 * @param submitOrderDto
	 * @param isValidateOnly Dry-run. Validate if order is valid (without validation to the user's profile).
	 * @return
	 * @throws ApiBadRequestException
	 */
	protected PolicyOrder registerOrder(final String userId, final OrderDto submitOrderDto, final boolean isValidateOnly) throws ApiBadRequestException{
		logger.debug("Process isvalidationonly {} order for {} with order {}", isValidateOnly, userId, submitOrderDto);
		
		LocalDate today = LocalDate.now();
		
		if (submitOrderDto == null || submitOrderDto.getProducts() == null
				|| submitOrderDto.getPolicyStartDate() == null || submitOrderDto.getTotalPremi() == null) {
			logger.debug("Process order for {} with order {} with result: exception empty order or product", 
					userId,	submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4000_ORDER_INVALID,
					"Permintaan tidak dapat diproses, silahkan cek kembali pesanan");
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
		
		LocalDate limitPolicyStartDate = today.plusDays(this.policyStartDatePeriod);
		if(submitOrderDto.getPolicyStartDate().isAfter(limitPolicyStartDate)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date exceed limit {}", userId, submitOrderDto, this.policyStartDatePeriod);
			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan pilih tanggal mulai asuransi antara hari ini sampai tanggal "
							+ limitPolicyStartDate.format(formatter));			
		}
		if(submitOrderDto.getPolicyStartDate().isBefore(today)){
			logger.debug("Process order for {} with order {} with result: exception policy start-date before today {}", userId, submitOrderDto, this.policyStartDatePeriod);
			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan pilih tanggal mulai asuransi antara hari ini sampai tanggal "
							+ limitPolicyStartDate.format(formatter));
			
		}
		
		List<Product> products = productService.fetchProductByProductIds(productIdSet);
		if(products.isEmpty() || products.size()!=submitOrderDto.getProducts().size()){
			logger.debug("Process order for {} with order {} with result: exception product not found", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4003_ORDER_PRODUCT_NOTFOUND, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}
		
		String periodId = products.get(0).getPeriodId();
		String coverageCategoryId = products.get(0).getCoverage().getCoverageCategoryId();
		int calculatedTotalPremi = 0;
		boolean hasBeneficiary = false;
		List<String> coverageIds = new ArrayList<>();
		for(Product p: products){
			if(!periodId.equals(p.getPeriodId())){
				logger.debug("Process order for {} with order {} with result: exception period mismatch", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4004_ORDER_PERIOD_MISMATCH, "Permintaan tidak dapat diproses, silahkan cek kembali periode asuransi");
			}
			if(!coverageCategoryId.equals(p.getCoverage().getCoverageCategoryId())){
				logger.debug("Process order for {} with order {} with result: exception coverage mismatch", userId, submitOrderDto);
				throw new ApiBadRequestException(ErrorCode.ERR4006_ORDER_COVERAGE_MISMATCH, "Permintaan tidak dapat diproses, pembelian lebih dari satu tipe asuransi belum didukung");
			}
			calculatedTotalPremi += p.getPremi();
			hasBeneficiary = hasBeneficiary || p.getCoverage().getHasBeneficiary();
			coverageIds.add(p.getCoverageId());
		}
		if(calculatedTotalPremi!=submitOrderDto.getTotalPremi()){
			logger.debug("Process order for {} with order {} with result: exception calculatd premi {} ", userId, submitOrderDto, calculatedTotalPremi);
			throw new ApiBadRequestException(ErrorCode.ERR4005_ORDER_PREMI_MISMATCH, "Permintaan tidak dapat diproses");
		}				
		
		Period period = products.get(0).getPeriod();
		if(!period.getUnit().equals(PeriodUnit.DAILY)){
			logger.debug("Process order for {} with order {} with result: exception period unit {}", userId, submitOrderDto, period.getUnit());
			throw new ApiBadRequestException(ErrorCode.ERR4008_ORDER_PRODUCT_UNSUPPORTED, "Permintaan tidak dapat diproses");
		}
		
		LocalDate policyEndDate = submitOrderDto.getPolicyStartDate().plusDays(products.get(0).getPeriod().getValue()-1);
		LocalDate dueOrderDate = today.minusDays(policyDueDatePeriod);
		
//		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
//				submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate, coverageIds);		
//		
		//Map<String, Long> conflictCoverageMap = conflictList.stream().collect(Collectors.groupingBy(PolicyOrderCoverage::getCoverageId, Collectors.counting()));		
		//boolean isOverLimit = conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=policyConflictPeriodLimit);
		boolean isOverLimit = isOverCoverageInSamePeriodLimit(userId, submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate, coverageIds);
		if(isOverLimit){
			logger.debug("Process order for {} with order {} with result: exception conflict coverage", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4009_ORDER_PRODUCT_CONFLICT,
					"Permintaan tidak dapat diproses, anda telah memiliki 3 asuransi yang akan atau telah aktif pada waktu yang sama");
		}
				
		PolicyOrder policyOrder = null;
		boolean isAllProfileInfoUpdated = false;
		boolean isPhoneInfoUpdated = false;		
		
		if(!isValidateOnly){			
			//TODO: submit order to ASWATA
			
			final User existingUser = userService.fetchUserByUserId(userId);

			boolean isUserProfileCompleteForOrder = isUserProfileCompleteForOrder(existingUser);

			User newUserProfile = null;
			if(!isUserProfileCompleteForOrder){
				if(submitOrderDto.getUser()==null){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi anda untuk melanjutkan pemesanan");
				}
				newUserProfile = new User();
				newUserProfile.setUserId(userId);;
				newUserProfile.setName(submitOrderDto.getUser().getName());
				newUserProfile.setGender(submitOrderDto.getUser().getGender());
				newUserProfile.setBirthDate(submitOrderDto.getUser().getBirthDate());
				newUserProfile.setBirthPlace(submitOrderDto.getUser().getBirthPlace());
				newUserProfile.setPhone(submitOrderDto.getUser().getPhone());
				
				if(!isUserProfileCompleteForOrder(newUserProfile)){
					logger.debug("Process order for {} with order {} with result: incomplete users profile", userId, submitOrderDto);
					throw new ApiBadRequestException(ErrorCode.ERR4010_ORDER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, lengkapi data pribadi anda untuk melanjutkan pemesanan");
				}
				
				userService.updateProfileInfo(newUserProfile);
				
				isAllProfileInfoUpdated = true;
			}else{
				if(submitOrderDto.getUser()!=null 
						&& !existingUser.getPhone().equals(submitOrderDto.getUser().getPhone())){
					userService.updatePhoneInfo(userId, submitOrderDto.getUser().getPhone());
					isPhoneInfoUpdated = true;
				}			
				
			}		
			
			policyOrder = new PolicyOrder();
			policyOrder.setOrderId(generateOrderId());
			policyOrder.setOrderDate(today);
			policyOrder.setUserId(userId);
			policyOrder.setCoverageCategoryId(coverageCategoryId);
			policyOrder.setHasBeneficiary(hasBeneficiary);
			policyOrder.setPeriodId(periodId);
			policyOrder.setPolicyStartDate(submitOrderDto.getPolicyStartDate());
			policyOrder.setPolicyEndDate(policyEndDate);
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
				policyOrderUser.setAddress(existingUser.getAddress());
				if(isPhoneInfoUpdated){
					policyOrderUser.setPhone(submitOrderDto.getUser().getPhone());
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
				pop.setPremi(p.getPremi());
				pop.setCoverageHasBeneficiary(p.getCoverage().getHasBeneficiary());
				pop.setPeriod(p.getPeriod());
				policyOrderProducts.add(pop);
			}
			
			policyOrder.setPolicyOrderProducts(policyOrderProducts);			
			
			policyOrderTrxService.registerPolicyOrder(policyOrder);
			
		}
		logger.debug("Process order isValidateOnly {} for {} with order {}, result update profile: {}, update phone: {}, order: {}",
				isValidateOnly, userId, submitOrderDto, isAllProfileInfoUpdated, isPhoneInfoUpdated, policyOrder);
		
		return policyOrder;
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
	
	protected boolean isOverCoverageInSamePeriodLimit(String userId, LocalDate policyStartDate, LocalDate policyEndDate,
			LocalDate dueOrderDate, List<String> coverageIds) {
		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
				policyStartDate, policyEndDate, dueOrderDate, coverageIds);		
		
		Map<String, Long> conflictCoverageMap = conflictList.stream().collect(Collectors.groupingBy(PolicyOrderCoverage::getCoverageId, Collectors.counting()));
		
		//logger.debug("result conflictmap is "+conflictCoverageMap);
		
		return  conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=policyConflictPeriodLimit);
	}
	
	protected PolicyOrder fetchOrderByOrderId(final String userId, final String orderId){
		return policyOrderMapper.selectByUserIdAndOrderId(userId, orderId);		
	}
	
	protected List<PolicyOrder> fetchOrder(final String userId, final OrderFilterDto filter){
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
				mapPolicyOrderStatus(po,today);
				
			}
		}
		
		return orders;
	}
	
	protected void mapPolicyOrderStatus(PolicyOrder policyOrder, LocalDate today){
		if(PolicyStatus.SUBMITTED.equals(policyOrder.getStatus())){
			if(policyOrder.getOrderDate().plusDays(this.policyDueDatePeriod).isBefore(today)){
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
	
	protected OrderDto policyOrderToOrderDto(final PolicyOrder policyOrder){
		OrderDto orderDto = null;

		if(policyOrder!=null){
			orderDto = new OrderDto();
			orderDto.setOrderId(policyOrder.getOrderId());
			orderDto.setOrderDate(policyOrder.getOrderDate());
			orderDto.setPolicyNumber(policyOrder.getPolicyNumber());
			orderDto.setPolicyStartDate(policyOrder.getPolicyStartDate());
			orderDto.setPolicyEndDate(policyOrder.getPolicyEndDate());
			orderDto.setTotalPremi(policyOrder.getTotalPremi());
			orderDto.setHasBeneficiary(policyOrder.getHasBeneficiary());
			orderDto.setProductCount(policyOrder.getProductCount());
			orderDto.setStatus(policyOrder.getStatus());
			orderDto.setCreatedDate(policyOrder.getCreatedDate());
			orderDto.setTitle(this.policyTitle);
			orderDto.setImgUrl(this.policyImgUrl);

			PeriodDto periodDto = new PeriodDto();
			periodDto.setName(policyOrder.getPeriod().getName());
			periodDto.setPeriodId(policyOrder.getPeriodId());
			periodDto.setUnit(policyOrder.getPeriod().getUnit());
			periodDto.setValue(policyOrder.getPeriod().getValue());

			int rank = 99;
			List<ProductDto> productDtos = new ArrayList<>();
			for(PolicyOrderProduct p: policyOrder.getPolicyOrderProducts()){
				ProductDto dto = new ProductDto();
				dto.setProductId(p.getProductId());
				dto.setName(p.getCoverageName());
				dto.setPremi(p.getPremi());
				
				dto.setPeriod(periodDto);

				CoverageDto covDto = new CoverageDto();
				covDto.setCoverageId(p.getCoverageId());
				covDto.setName(p.getCoverageName());
				covDto.setMaxLimit(p.getCoverageMaxLimit());
				covDto.setHasBeneficiary(p.getCoverageHasBeneficiary());
				dto.setCoverage(covDto);

				productDtos.add(dto);
				
				if(p.getCoverageDisplayRank() < rank){
					orderDto.setSubtitle(p.getCoverageName());
					rank = p.getCoverageDisplayRank();
				}
			}
			orderDto.setProducts(productDtos);
			

			orderDto.setPeriod(periodDto);
		}

		return orderDto;
		
	}
	
//	public OrderDto fetchOrderByOrderId(String orderId){
//		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
//		DateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		
//		List<Period> periods = productService.fetchAllPeriod();
//		Map<String,PeriodDto> perMap = new HashMap<>();
//		for(Period c: periods) {
//			PeriodDto dto = new PeriodDto();
//			dto.setPeriodId(c.getPeriodId());
//			dto.setName(c.getName());
//			dto.setValue(c.getValue());
//			dto.setUnit(c.getUnit());
//			perMap.put(c.getPeriodId(), dto);
//		}
//		
//		PolicyOrder po = policyOrderMapper.selectByOrderId(orderId);
//		OrderDto order = null;
//		if(po!=null){
//			order = new OrderDto();
//			List<ProductDto> productDtos = new ArrayList<>();
//
//			order.setOrderId(po.getOrderId());
//			order.setOrderDate(po.getOrderDate());
//			order.setPolicyNumber(po.getPolicyNumber());
//			order.setPolicyStartDate(po.getPolicyStartDate());
//			order.setPolicyEndDate(po.getPolicyEndDate());
//			order.setTotalPremi(po.getTotalPremi());
//			order.setHasBeneficiary(po.getHasBeneficiary());
//			order.setProductCount(po.getProductCount());
//			order.setStatus(po.getStatus());
//			order.setCreatedDate(po.getCreatedDate());
//			order.setTitle("Asuransi kecelakaan");
//			order.setImgUrl("");
//
//			List<PolicyOrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
//			for (PolicyOrderProduct op : productList) {
//				ProductDto dto = new ProductDto();
//				dto.setProductId(op.getProductId());
//				dto.setName(op.getCoverageName());
//				dto.setPremi(op.getPremi());
//				//dto.setPeriod(perMap.get(op.getPeriodId()));
//
//				CoverageDto covDto = new CoverageDto();
//				covDto.setCoverageId(op.getCoverageId());
//				covDto.setName(op.getCoverageName());
//				covDto.setMaxLimit(op.getCoverageMaxLimit());
//				covDto.setHasBeneficiary(op.getCoverageHasBeneficiary());
//				dto.setCoverage(covDto);
//
//				productDtos.add(dto);
//			}
//			if (!productList.isEmpty()) {
//				order.setProducts(productDtos);
//			}
//
//			order.setPeriod(perMap.get(po.getPeriodId()));
//		}		
//		
//		return order;
//	}
	
	
	//test
//	public List<OrderDto> testfetchOrderListByUserId(String userId){
//		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
//		DateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		
//		List<Period> periods = productService.fetchAllPeriod();
//		Map<String,PeriodDto> perMap = new HashMap<>();
//		for(Period c: periods) {
//			PeriodDto dto = new PeriodDto();
//			dto.setPeriodId(c.getPeriodId());
//			dto.setName(c.getName());
//			dto.setValue(c.getValue());
//			dto.setUnit(c.getUnit());
//			perMap.put(c.getPeriodId(), dto);
//		}
//		
//		List<PolicyOrder> pos = policyOrderMapper.selectByUserId(userId,100,0);
//		List<OrderDto> orders = new ArrayList<>();
//		for(PolicyOrder po:pos){
//			OrderDto order = new OrderDto();
//			List<ProductDto> productDtos = new ArrayList<>();
//			
//			order.setOrderId(po.getOrderId());
//			order.setOrderDate(po.getOrderDate());
//			order.setPolicyNumber(po.getPolicyNumber());
//			order.setPolicyStartDate(po.getPolicyStartDate());
//			order.setPolicyEndDate(po.getPolicyEndDate());
//			order.setTotalPremi(po.getTotalPremi());
//			order.setHasBeneficiary(po.getHasBeneficiary());
//			order.setProductCount(po.getProductCount());
//			order.setStatus(po.getStatus());
//			order.setCreatedDate(po.getCreatedDate());
//			order.setTitle("Asuransi kecelakaan");
//			order.setImgUrl("https://i.imgur.com/f3h2z7k.jpg");
//			
//			List<PolicyOrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
//			for(PolicyOrderProduct op: productList){
//				ProductDto dto = new ProductDto();
//				dto.setProductId(op.getProductId());
//				dto.setName(op.getCoverageName());
//				dto.setPremi(op.getPremi());
//				//dto.setPeriod(perMap.get(op.getPeriodId()));
//				
//				CoverageDto covDto = new CoverageDto();
//				covDto.setCoverageId(op.getCoverageId());
//				covDto.setName(op.getCoverageName());
//				covDto.setMaxLimit(op.getCoverageMaxLimit());
//				covDto.setHasBeneficiary(op.getCoverageHasBeneficiary());				
//				dto.setCoverage(covDto);
//				
//				productDtos.add(dto);
//			}
//			if(!productList.isEmpty()){
//				order.setProducts(productDtos);
//			}
//			
//			order.setPeriod(perMap.get(po.getPeriodId()));
//			
//			orders.add(order);			
//		}		
//		return orders;
//	}
	
	//test
	public List<PolicyOrderCoverage> testConflict(String userId, final OrderDto submitOrderDto){
		Set<String> productIdSet = submitOrderDto.getProducts().stream().map(ProductDto::getProductId).collect(Collectors.toSet()); 
		List<Product> products = productService.fetchProductByProductIds(productIdSet);
		List<String> coverageIds = new ArrayList<>();
		for(Product p: products){			
			coverageIds.add(p.getCoverageId());
		}
		
		LocalDate dueOrderDate = LocalDate.now().minusDays(policyDueDatePeriod);
		LocalDate policyEndDate = submitOrderDto.getPolicyStartDate().plusDays(products.get(0).getPeriod().getValue()-1);
		
		logger.debug("trx with order-date {} is due today", dueOrderDate);
		logger.debug("check conflict {} {} {} {}",userId, submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate);
		List<PolicyOrderCoverage> checklist = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId, submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate, coverageIds);
		for(PolicyOrderCoverage item: checklist){
			logger.debug("Test item {}", item);
		}
		return checklist;
	}
	//test
	public List<PolicyOrder> tesFetch(String userId, final OrderFilterDto filter){
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
		
		//TODO: safety check here? in the loop, throw exception if the request ACTIVE doesnt return ACTIVE, etc
		LocalDate today = LocalDate.now();
		if(orders!=null){
			for(PolicyOrder po: orders){
				mapPolicyOrderStatus(po,today);
			}
		}
		
		logger.debug("call testfetch with filter {} and filterType {} and result-size {}", filter, filterType,
				orders == null ? "0" : String.valueOf(orders.size()));
		return orders;
	}
	private String generateOrderId(){
		return UUID.randomUUID().toString();
	}
}
