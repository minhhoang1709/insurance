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
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.dto.SubmitOrderDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.model.PolicyOrderCoverage;
import com.ninelives.insurance.api.model.PolicyOrderProduct;
import com.ninelives.insurance.api.model.PolicyOrderUsers;
import com.ninelives.insurance.api.mybatis.mapper.OrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderUsersMapper;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.PolicyStatus;
import com.ninelives.insurance.api.service.trx.PolicyOrderTrxService;
import com.ninelives.insurance.api.ref.PeriodUnit;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired ProductService productService;
	@Autowired UsersService userService;
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
	
	@Value("${ninelives.order.policy-title}")
	String policyTitle;
	
	@Value("${ninelives.order.policy-imgUrl}")
	String policyImgUrl;
	
	//TODO: Replace hardcoded policytitles and imgurl
	
	public OrderDto submitOrder(String userId, final SubmitOrderDto submitOrderDto) throws ApiBadRequestException{
		logger.debug("Process order for {} with order {}", userId, submitOrderDto);
		
		if(CollectionUtils.isEmpty(submitOrderDto.getProducts())){
			logger.debug("Process order for {} with order {} with result: exception empty product", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4001_ORDER_PRODUCT_EMPTY, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}
		if(submitOrderDto.getProducts().size()!= (new HashSet<>(submitOrderDto.getProducts())).size()){
			logger.debug("Process order for {} with order {} with result: exception duplicate product", userId, submitOrderDto);
			throw new ApiBadRequestException(ErrorCode.ERR4002_ORDER_PRODUCT_DUPLICATE, "Permintaan tidak dapat diproses, silahkan cek kembali daftar produk");
		}		
		List<Product> products = productService.fetchProductByProductIds(submitOrderDto.getProducts());
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
				throw new ApiBadRequestException(ErrorCode.ERR4004_ORDER_PERIOD_MISMATCH, "Permintaan tidak dapat diproses, silahkan cek kembali tanggal");
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
		
		LocalDate limitPolicyStartDate = LocalDate.now().plusDays(this.policyStartDatePeriod);
		if(limitPolicyStartDate.isBefore(submitOrderDto.getPolicyStartDate())){
			logger.debug("Process order for {} with order {} with result: exception policy start-date exceed limit {}", userId, submitOrderDto, this.policyStartDatePeriod);
			throw new ApiBadRequestException(ErrorCode.ERR4007_ORDER_STARTDATE_INVALID,
					"Permintaan tidak dapat diproses, silahkan pilih tanggal mulai asuransi tidak lebih dari tanggal "
							+ limitPolicyStartDate.format(formatter));
			
		}
		
		Period period = products.get(0).getPeriod();
		if(!period.getUnit().equals(PeriodUnit.DAILY)){
			logger.debug("Process order for {} with order {} with result: exception period unit {}", userId, submitOrderDto, period.getUnit());
			throw new ApiBadRequestException(ErrorCode.ERR4008_ORDER_PRODUCT_UNSUPPORTED, "Permintaan tidak dapat diproses");
		}
		
		LocalDate policyEndDate = submitOrderDto.getPolicyStartDate().plusDays(products.get(0).getPeriod().getValue()-1);
		LocalDate dueOrderDate = LocalDate.now().minusDays(policyDueDatePeriod);
		
		List<PolicyOrderCoverage> conflictList = policyOrderMapper.selectCoverageWithConflictedPolicyDate(userId,
				submitOrderDto.getPolicyStartDate(), policyEndDate, dueOrderDate, coverageIds);
		
		if(conflictList.size()>=policyConflictPeriodLimit){
			logger.debug("Process order for {} with order {} with result: exception conflict coverage {}", userId, submitOrderDto, conflictList);
			throw new ApiBadRequestException(ErrorCode.ERR4009_ORDER_PRODUCT_CONFLICT,
					"Permintaan tidak dapat diproses, anda telah memiliki 3 asuransi yang aktif pada waktu yang sama");
		}
		
		//TODO: submit order to ASWATA
		
		
		PolicyOrder policyOrder = new PolicyOrder();
		policyOrder.setOrderId(generateOrderId());
		policyOrder.setOrderDate(LocalDate.now());
		policyOrder.setUserId(userId);
		policyOrder.setCoverageCategoryId(coverageCategoryId);
		policyOrder.setHasBeneficiary(hasBeneficiary);
		policyOrder.setPeriod(periodId);
		policyOrder.setPolicyStartDate(submitOrderDto.getPolicyStartDate());
		policyOrder.setPolicyEndDate(policyEndDate);
		policyOrder.setTotalPremi(calculatedTotalPremi);
		policyOrder.setProductCount(products.size());
		policyOrder.setStatus(PolicyStatus.SUBMITTED);
		
		//Set user info		
		final Users user = userService.fetchUser(userId);		
		PolicyOrderUsers policyOrderUser = new PolicyOrderUsers();
		policyOrderUser.setOrderId(policyOrder.getOrderId());
		policyOrderUser.setName(user.getName());
		policyOrderUser.setEmail(user.getEmail());
		policyOrderUser.setGender(user.getGender());
		policyOrderUser.setBirthDate(user.getBirthDate());
		policyOrderUser.setBirthPlace(user.getBirthPlace());
		policyOrderUser.setPhone(user.getPhone());
		policyOrderUser.setAddress(user.getAddress());
		policyOrderUser.setIdCardFileId(user.getIdCardFileId());
		
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
		
		OrderDto orderDto = policyOrderToOrderDto(policyOrder);

		return orderDto;
	}
	
	private OrderDto policyOrderToOrderDto(PolicyOrder policyOrder){
		OrderDto orderDto = new OrderDto();

		if(policyOrder!=null){
			
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
			
			List<ProductDto> productDtos = new ArrayList<>();
			for(PolicyOrderProduct p: policyOrder.getPolicyOrderProducts()){
				ProductDto dto = new ProductDto();
				dto.setProductId(p.getProductId());
				dto.setName(p.getCoverageName());
				dto.setPremi(p.getPremi());
				
				PeriodDto periodDto = new PeriodDto();
				periodDto.setName(p.getPeriod().getName());
				periodDto.setPeriodId(p.getPeriodId());
				periodDto.setUnit(p.getPeriod().getUnit());
				periodDto.setValue(p.getPeriod().getValue());
				dto.setPeriod(periodDto);

				CoverageDto covDto = new CoverageDto();
				covDto.setCoverageId(p.getCoverageId());
				covDto.setName(p.getCoverageName());
				covDto.setMaxLimit(p.getCoverageMaxLimit());
				covDto.setHasBeneficiary(p.getCoverageHasBeneficiary());
				dto.setCoverage(covDto);

				productDtos.add(dto);
			}
			orderDto.setProductList(productDtos);
		}
		
		return orderDto;
		
	}
	
	public OrderDto fetchOrderByOrderId(String orderId){
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		List<Period> periods = productService.fetchAllPeriod();
		Map<String,PeriodDto> perMap = new HashMap<>();
		for(Period c: periods) {
			PeriodDto dto = new PeriodDto();
			dto.setPeriodId(c.getPeriodId());
			dto.setName(c.getName());
			dto.setValue(c.getValue());
			dto.setUnit(c.getUnit());
			perMap.put(c.getPeriodId(), dto);
		}
		
		PolicyOrder po = policyOrderMapper.selectByOrderId(orderId);
		OrderDto order = null;
		if(po!=null){
			order = new OrderDto();
			List<ProductDto> productDtos = new ArrayList<>();

			order.setOrderId(po.getOrderId());
			order.setOrderDate(po.getOrderDate());
			order.setPolicyNumber(po.getPolicyNumber());
			order.setPolicyStartDate(po.getPolicyStartDate());
			order.setPolicyEndDate(po.getPolicyEndDate());
			order.setTotalPremi(po.getTotalPremi());
			order.setHasBeneficiary(po.getHasBeneficiary());
			order.setProductCount(po.getProductCount());
			order.setStatus(po.getStatus());
			order.setCreatedDate(po.getCreatedDate());
			order.setTitle("Asuransi kecelakaan");
			order.setImgUrl("");

			List<PolicyOrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
			for (PolicyOrderProduct op : productList) {
				ProductDto dto = new ProductDto();
				dto.setProductId(op.getProductId());
				dto.setName(op.getCoverageName());
				dto.setPremi(op.getPremi());
				dto.setPeriod(perMap.get(op.getPeriodId()));

				CoverageDto covDto = new CoverageDto();
				covDto.setCoverageId(op.getCoverageId());
				covDto.setName(op.getCoverageName());
				covDto.setMaxLimit(op.getCoverageMaxLimit());
				covDto.setHasBeneficiary(op.getCoverageHasBeneficiary());
				dto.setCoverage(covDto);

				productDtos.add(dto);
			}
			if (!productList.isEmpty()) {
				order.setProductList(productDtos);
			}

			//order.setPeriod(perMap.get(po.getPeriod()));
		}		
		
		return order;
	}
	
	public List<OrderDto> fetchOrderListByUserId(String userId){
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		List<Period> periods = productService.fetchAllPeriod();
		Map<String,PeriodDto> perMap = new HashMap<>();
		for(Period c: periods) {
			PeriodDto dto = new PeriodDto();
			dto.setPeriodId(c.getPeriodId());
			dto.setName(c.getName());
			dto.setValue(c.getValue());
			dto.setUnit(c.getUnit());
			perMap.put(c.getPeriodId(), dto);
		}
		
		List<PolicyOrder> pos = policyOrderMapper.selectByUserId(userId);
		List<OrderDto> orders = new ArrayList<>();
		for(PolicyOrder po:pos){
			OrderDto order = new OrderDto();
			List<ProductDto> productDtos = new ArrayList<>();
			
			order.setOrderId(po.getOrderId());
			order.setOrderDate(po.getOrderDate());
			order.setPolicyNumber(po.getPolicyNumber());
			order.setPolicyStartDate(po.getPolicyStartDate());
			order.setPolicyEndDate(po.getPolicyEndDate());
			order.setTotalPremi(po.getTotalPremi());
			order.setHasBeneficiary(po.getHasBeneficiary());
			order.setProductCount(po.getProductCount());
			order.setStatus(po.getStatus());
			order.setCreatedDate(po.getCreatedDate());
			order.setTitle("Asuransi kecelakaan");
			order.setImgUrl("https://i.imgur.com/f3h2z7k.jpg");
			
			List<PolicyOrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
			for(PolicyOrderProduct op: productList){
				ProductDto dto = new ProductDto();
				dto.setProductId(op.getProductId());
				dto.setName(op.getCoverageName());
				dto.setPremi(op.getPremi());
				dto.setPeriod(perMap.get(op.getPeriodId()));
				
				CoverageDto covDto = new CoverageDto();
				covDto.setCoverageId(op.getCoverageId());
				covDto.setName(op.getCoverageName());
				covDto.setMaxLimit(op.getCoverageMaxLimit());
				covDto.setHasBeneficiary(op.getCoverageHasBeneficiary());				
				dto.setCoverage(covDto);
				
				productDtos.add(dto);
			}
			if(!productList.isEmpty()){
				order.setProductList(productDtos);
			}
			
			//order.setPeriod(perMap.get(po.getPeriod()));
			
			orders.add(order);			
		}		
		return orders;
	}
	
	//test
	public List<PolicyOrderCoverage> testConflict(String userId, final SubmitOrderDto submitOrderDto){
		List<Product> products = productService.fetchProductByProductIds(submitOrderDto.getProducts());
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
	private String generateOrderId(){
		return UUID.randomUUID().toString();
	}
}
