package com.ninelives.insurance.core.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderBeneficiaryMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderProductMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderUsersMapper;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.Period;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderBeneficiary;
import com.ninelives.insurance.model.PolicyOrderCoverage;
import com.ninelives.insurance.model.PolicyOrderDocument;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.PolicyOrderUsers;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.provider.insurance.aswata.dto.PaymentConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.PeriodUnit;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired ProductService productService;
	@Autowired VoucherService voucherService;
	@Autowired InsuranceService insuranceService;
	@Autowired FileUploadService fileUploadService;
	@Autowired PolicyOrderMapper policyOrderMapper;
	@Autowired PolicyOrderUsersMapper policyOrderUserMapper;
	@Autowired PolicyOrderProductMapper policyOrderProductMapper; 
	@Autowired PolicyOrderBeneficiaryMapper policyOrderBeneficiaryMapper;
	
	@Value("${ninelives.order.filter-limit:100}")
	int defaultOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-max-limit:100}")
	int maxOrdersFilterLimit;
	
	@Value("${ninelives.order.filter-offset:0}")
	int defaultOrdersFilterOffset;
	
	public PolicyOrder orderConfirm(String userId, String orderId) throws AppException{
		logger.info("Start process order confirm, userId:<{}>, orderId:<{}>", orderId);
		
		PolicyOrder policyOrder = fetchOrderByOrderId(userId, orderId);
		
		if(policyOrder == null || !PolicyStatus.PAID.equals(policyOrder.getStatus())) {
			throw new AppException(ErrorCode.ERR4501_ORDERCONFIRM_INVALID_ORDER_STATUS,"Order not exists or status is not paid");
		}
		
		if(policyOrder.getPolicyOrderUsers()==null) {
			PolicyOrderUsers pou = fetchPolicyOrderUsersByOrderId(policyOrder.getOrderId());
			policyOrder.setPolicyOrderUsers(pou);
		}
		if(policyOrder.getHasVoucher()) {
			if(VoucherType.B2B.equals(policyOrder.getPolicyOrderVoucher().getVoucher().getVoucherType())) {
				if(policyOrder.getPolicyOrderVoucher().getVoucher().getCorporateClient()==null) {
					Voucher voucher = voucherService.fetchVoucherbyId(policyOrder.getPolicyOrderVoucher().getVoucher().getId());
					policyOrder.getPolicyOrderVoucher().setVoucher(voucher);
				}
			}
		}
		
		orderConfirm(policyOrder);
		
		return policyOrder;
	}
	
	protected void orderConfirm(PolicyOrder policyOrder) throws AppException{
		logger.info("Start process order confirm, order:<{}>", policyOrder);
		
		if(StringUtils.isEmpty(policyOrder.getOrderId())){
			throw new AppBadRequestException(ErrorCode.ERR4000_ORDER_INVALID, "Pesanan tidak ditemukan");
		}		
		try {
			insuranceService.orderConfirm(policyOrder);
			
			policyOrder.setStatus(PolicyStatus.APPROVED);
			
			policyOrderMapper.updateStatusAndProviderResponseByOrderIdSelective(policyOrder);
			
		} catch (AppInternalServerErrorException e) {
			logger.error("Error order confirm for user:<{}> with order:<{}>, result: exception <{}>",
					policyOrder, e.getCode());
			throw e;
		}
		
	}
	
	public PolicyOrder paymentConfirm (String orderId) throws AppException{
		logger.info("Start process payment confirm, orderId:<{}>", orderId);
		
		PolicyOrder policyOrder = fetchOrderByOrderId(orderId);
		if(policyOrder.getPolicyOrderUsers()==null) {
			PolicyOrderUsers pou = fetchPolicyOrderUsersByOrderId(orderId);
			policyOrder.setPolicyOrderUsers(pou);
		}
		
		paymentConfirm(policyOrder);
		
		return policyOrder;
	}
	
	public void paymentConfirm(PolicyOrder policyOrder) throws AppException{
		logger.info("Start process payment confirm, order:<{}>", policyOrder);
		
		if(StringUtils.isEmpty(policyOrder.getOrderId())){
			throw new AppBadRequestException(ErrorCode.ERR4000_ORDER_INVALID, "Pesanan tidak ditemukan");
		}		
		try {
			insuranceService.paymentConfirm(policyOrder);
			
			policyOrder.setStatus(PolicyStatus.APPROVED);
			
			policyOrderMapper.updateStatusAndProviderResponseByOrderIdSelective(policyOrder);
			
		} catch (AppInternalServerErrorException e) {
			logger.error("Error order confirm for user:<{}> with order:<{}>, result: exception <{}>",
					policyOrder, e.getCode());
			throw e;
		}
//		logger.info("Process payment confirm, order:<{}>", policyOrder);
//		ResponseDto<PaymentConfirmResponseDto>  result = insuranceProvider.paymentConfirm(policyOrder);
//		if(insuranceProvider.isSuccess(result)){
//			PolicyOrder updateOrder = new PolicyOrder();
//			updateOrder.setOrderId(policyOrder.getOrderId());
//			updateOrder.setStatus(PolicyStatus.APPROVED);
//			if(result.getResponse().getResponseParam().getDownloadUrl()!=null){
//				updateOrder.setProviderDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());
//			}
//			if(result.getResponse().getResponseParam().getPolicyNumber()!=null){
//				updateOrder.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
//			}
//			logger.debug("Update order status to approved, order:<{}>", updateOrder);
//			policyOrderMapper.updateStatusAndProviderResponseByOrderIdSelective(updateOrder);
//		}
//		

	}
	
	public boolean isEquals(PolicyOrderBeneficiary pob, UserBeneficiary ub){
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
			
	public LocalDate fetchMaxPolicyEndDateByCoverage(String userId, LocalDate policyEndDate,
			List<String> coverageIds){
		return policyOrderMapper.selectMaxPolicyEndDateByCoverage(userId, policyEndDate, coverageIds);
	}
	
	public int insertBeneficiary(PolicyOrderBeneficiary beneficiary){
		return policyOrderBeneficiaryMapper.insert(beneficiary);
	}
	
	public void hideOrder(PolicyOrder policyOrder) throws AppException{
		if(policyOrder == null || StringUtils.isEmpty(policyOrder.getOrderId())){
			throw new AppBadRequestException(ErrorCode.ERR4000_ORDER_INVALID, "Pesanan tidak ditemukan");
		}
		policyOrder.setIsHide(true);
		policyOrderMapper.updateIsHideByOrderId(policyOrder);
	}
	
	public LocalDate calculatePolicyEndDate(LocalDate localDate, Period period) {
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
	
	public boolean isPolicyEndDateWithinRange(LocalDate startDate, LocalDate endDate, Period period){
		if(startDate==null||endDate==null||period==null){
			return false;
		}
		if(PeriodUnit.RANGE_DAY.equals(period.getUnit())){
			long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1; //inclusive enddate
			if(daysBetween >= period.getStartValue() &&
					daysBetween <= period.getEndValue())
			return true;
		}
		return false;
	}

	public Boolean isUserProfileCompleteForOrder(User user){
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
	
	public Boolean hasPaidOrder(String userId){
		return policyOrderMapper.selectPaidOrderExists(userId); 
	}
	
//	/**
//	 * Check if multiple coverage purchase with the same period is allowed for SELF PA
//	 * @param userId
//	 * @param policyStartDate
//	 * @param policyEndDate
//	 * @param dueOrderDate
//	 * @param coverageIds
//	 * @return
//	 */
//	public boolean isOverCoverageInSamePeriodLimit(String userId, LocalDate policyStartDate, LocalDate policyEndDate,
//			LocalDate dueOrderDate, List<String> coverageIds) {
//		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
//				policyStartDate, policyEndDate, dueOrderDate, coverageIds);		
//		
//		Map<String, Long> conflictCoverageMap = conflictList.stream()
//				.collect(Collectors.groupingBy(PolicyOrderCoverage::getCoverageId, Collectors.counting()));
//		
//		return conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=config.getOrder().getPolicyConflictPeriodLimit());
//	}
	
	public boolean isOverCoverageInSamePeriodLimit(String userId, LocalDate policyStartDate, LocalDate policyEndDate,
			LocalDate dueOrderDate, List<String> coverageIds, String coverageCategoryId) {
		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
				policyStartDate, policyEndDate, dueOrderDate, coverageIds);		
		
		Map<String, Long> conflictCoverageMap = conflictList.stream()
				.collect(Collectors.groupingBy(PolicyOrderCoverage::getCoverageId, Collectors.counting()));
		
		if(CoverageCategoryId.TRAVEL_INTERNATIONAL.equals(coverageCategoryId)||
				CoverageCategoryId.TRAVEL_DOMESTIC.equals(coverageCategoryId)){
			return conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=config.getOrder().getTravelPolicyConflictPeriodLimit());
		}else{
			return conflictCoverageMap.entrySet().stream().anyMatch(map -> map.getValue()>=config.getOrder().getPolicyConflictPeriodLimit());
		}
				
	}
	
	public PolicyOrder fetchOrderWithBeneficiaryByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectWithBeneficiaryByUserIdAndOrderId(userId, orderId);
		postRetrieval(policyOrder, LocalDate.now());
		return policyOrder;
	}
	
	public PolicyOrder fetchOrderByOrderId(final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectByOrderId(orderId);
		postRetrieval(policyOrder,LocalDate.now());
		return policyOrder;
	}
	
	public PolicyOrder fetchOrderByOrderId(final String userId, final String orderId){
		PolicyOrder policyOrder = policyOrderMapper.selectByOrderIdAndUserId(userId, orderId);
		postRetrieval(policyOrder,LocalDate.now());
		return policyOrder;
	}
	
	public List<PolicyOrder> fetchOrdersByUserId(final String userId, int limit, int offset){
		if (limit > this.maxOrdersFilterLimit){
			limit = this.maxOrdersFilterLimit;
		}		
		List<PolicyOrder> orders = policyOrderMapper.selectByUserId(userId, limit, offset, false);
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,LocalDate.now());
			}
		}
		return orders;
	}
	
	public List<PolicyOrder> fetchOrdersWhereStatusActiveByUserId(final String userId, int limit, int offset){
		if (limit > this.maxOrdersFilterLimit){
			limit = this.maxOrdersFilterLimit;
		}
		List<PolicyOrder> orders = policyOrderMapper.selectWhereStatusActiveByUserId(userId, limit, offset, false);
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,LocalDate.now());
			}
		}
		return orders;
	}
	
	public List<PolicyOrder> fetchOrdersWhereStatusApprovedByUserId(final String userId, int limit, int offset){
		if (limit > this.maxOrdersFilterLimit){
			limit = this.maxOrdersFilterLimit;
		}
		List<PolicyOrder> orders = policyOrderMapper.selectWhereStatusApprovedByUserId(userId, limit, offset, false);
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,LocalDate.now());
			}
		}
		return orders;
	}
	
	public List<PolicyOrder> fetchOrdersWhereStatusExpiredOrTerminatedByUserId(final String userId, int limit, int offset){
		if (limit > this.maxOrdersFilterLimit){
			limit = this.maxOrdersFilterLimit;
		}
		List<PolicyOrder> orders = policyOrderMapper.selectWhereStatusExpiredOrTerminatedByUserId(userId, limit, offset, false);
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,LocalDate.now());
			}
		}
		return orders;
	}

	public List<PolicyOrder> fetchOrdersWhereStatusBeforeApprovedByUserId(final String userId, int limit, int offset){
		if (limit > this.maxOrdersFilterLimit){
			limit = this.maxOrdersFilterLimit;
		}
		List<PolicyOrder> orders = policyOrderMapper.selectWhereStatusBeforeApprovedByUserId(userId, limit, offset, false);
		if(orders!=null){
			for(PolicyOrder po: orders){
				postRetrieval(po,LocalDate.now());
			}
		}
		return orders;
	}
	
	public PolicyOrderUsers fetchPolicyOrderUsersByOrderId (String orderId) {
		return policyOrderUserMapper.selectByOrderId(orderId);
	}
	
	
	public PolicyOrder fetchOrderForDownload(String userId, String orderId) throws AppException{		
		
		PolicyOrder order = fetchOrderByOrderId(userId, orderId);
		
		if (order!=null && (order.getStatus().equals(PolicyStatus.APPROVED) || order.getStatus().equals(PolicyStatus.ACTIVE)
				|| order.getStatus().equals(PolicyStatus.EXPIRED))) {
			if(!StringUtils.isEmpty(order.getProviderDownloadUrl())){
				logger.info("Fetch download, userId:<{}>, orderId:<{}>, downloadUrl<{}>", userId, orderId, order.getProviderDownloadUrl());
				return order;
			}else{
				logger.error("Fetch download, userId:<{}>, orderId:<{}>, result:<exception empty download url>, exception:<{}>",
						userId, orderId, ErrorCode.ERR4301_DOWNLOAD_NO_URL);
				throw new AppInternalServerErrorException(ErrorCode.ERR4301_DOWNLOAD_NO_URL, "Permintaan tidak dapat diproses, terjadi kesalahan pada sistem");
			}
		}else{
			logger.error("Fetch download, userId:<{}>, orderId:<{}>, result:<exception status not valid>, exception:<{}>",
					userId, orderId, ErrorCode.ERR4302_DOWNLOAD_NOT_ELIGIBLE);
			throw new AppBadRequestException(ErrorCode.ERR4302_DOWNLOAD_NOT_ELIGIBLE, "Permintaan tidak dapat diproses, status asuransi tidak valid");
		}
		
		
	}
	

	public void postRetrieval(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			mapPolicyOrderStatus(policyOrder, today);
			if(policyOrder.getPolicyOrderProducts()!=null){
				for(PolicyOrderProduct pop: policyOrder.getPolicyOrderProducts()){
					Coverage c = productService.fetchCoverageByCoverageId(pop.getCoverageId()); 
					pop.setCoverageClaimDocTypes(c.getCoverageClaimDocTypes());
					pop.setCoverageDisplayRank(c.getDisplayRank());
					pop.setIsLumpSum(c.getIsLumpSum());
					pop.setCoverageOption(c.getCoverageOption());
					pop.setCoverageCategory(c.getCoverageCategory());
					
					pop.setProduct(productService.fetchProductByProductId(pop.getProductId()));
				}
				List<PolicyOrderProduct> sortedList = policyOrder.getPolicyOrderProducts().stream().sorted(
						(o1, o2) -> Integer.compare(o1.getCoverageDisplayRank(), o2.getCoverageDisplayRank()))
						.collect(Collectors.toList());
				policyOrder.setPolicyOrderProducts(sortedList);
			}
		}		
		
	}
	
	public void mapPolicyOrderStatus(PolicyOrder policyOrder, LocalDate today){
		if(policyOrder!=null){
			policyOrder.setStatus(parsePolicyStatus(policyOrder.getStatus(), policyOrder.getOrderDate(),
					policyOrder.getPolicyStartDate(), policyOrder.getPolicyEndDate(), today));			
		}
	}
	
	private PolicyStatus parsePolicyStatus(PolicyStatus policyStatus, LocalDate policyOrderDate,
			LocalDate policyStartDate, LocalDate policyEndDate, LocalDate today) {
		if(PolicyStatus.SUBMITTED.equals(policyStatus)){
			if(policyOrderDate.plusDays(config.getOrder().getPolicyDueDatePeriod()).isBefore(today)){
				return PolicyStatus.OVERDUE;
			}else if(policyStartDate.isBefore(today)){
				return PolicyStatus.OVERDUE;
			}
		}else if(PolicyStatus.APPROVED.equals(policyStatus)){
			if(!policyStartDate.isAfter(today) && !policyEndDate.isBefore(today)){
				return PolicyStatus.ACTIVE;
			}else if(policyEndDate.isBefore(today)){
				return PolicyStatus.EXPIRED;				
			}
		}
		
		return policyStatus;
	}

		
	public String generateOrderId(){
		return UUID.randomUUID().toString();
	}

	public int getDefaultOrdersFilterLimit() {
		return defaultOrdersFilterLimit;
	}

	public int getDefaultOrdersFilterOffset() {
		return defaultOrdersFilterOffset;
	}
	public void updateOrderDocumentFiles(List<PolicyOrderDocument> docs) throws AppException {
		if (docs!=null) {
			for(PolicyOrderDocument doc: docs){
				fileUploadService.moveTemp(doc.getUserFile(), FileUseType.ORDER);
			}
		}		
	}
	
	public PolicyOrder getOrderByOrderId(String orderId, String userId) {
		PolicyOrder policyOrder = policyOrderMapper.selectByOrderIdAndUserId(orderId, userId);
		return policyOrder;

	}
	
	public void updatePolicyOrderId2c2p(PolicyOrder policyOrder){
		policyOrderMapper.updateStatusAndProviderResponseByOrderIdSelective(policyOrder);
	}
	
	
}
