package com.ninelives.insurance.api.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.PeriodDto;
import com.ninelives.insurance.api.dto.ProductDto;
import com.ninelives.insurance.api.model.OrderProduct;
import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.PurchaseOrder;
import com.ninelives.insurance.api.mybatis.mapper.OrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PurchaseOrderMapper;

@Service
public class OrderService {
	
	@Autowired ProductService productService;
	@Autowired PurchaseOrderMapper purchaseOrderMapper;
	@Autowired OrderProductMapper orderProductMapper;
	
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
		
		PurchaseOrder po = purchaseOrderMapper.selectByOrderId(orderId);
		OrderDto order = null;
		if(po!=null){
			order = new OrderDto();
			List<ProductDto> productDtos = new ArrayList<>();

			order.setOrderId(po.getOrderId());
			order.setOrderDate(dfDate.format(po.getOrderDate()));
			order.setPolicyNumber(po.getPolicyNumber());
			order.setPolicyStartDate(dfDate.format(po.getPolicyStartDate()));
			order.setPolicyEndDate(dfDate.format(po.getPolicyEndDate()));
			order.setTotalPremi(po.getTotalPremi());
			order.setHasBeneficiary(po.getHasBeneficiary());
			order.setProductCount(po.getProductCount());
			order.setStatus(po.getStatus());
			order.setCreatedDate(dfTime.format(po.getCreatedDate()));
			order.setTitle("Asuransi kecelakaan");
			order.setImgUrl("");

			List<OrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
			for (OrderProduct op : productList) {
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
		
		List<PurchaseOrder> pos = purchaseOrderMapper.selectByUserId(userId);
		List<OrderDto> orders = new ArrayList<>();
		for(PurchaseOrder po:pos){
			OrderDto order = new OrderDto();
			List<ProductDto> productDtos = new ArrayList<>();
			
			order.setOrderId(po.getOrderId());
			order.setOrderDate(dfDate.format(po.getOrderDate()));
			order.setPolicyNumber(po.getPolicyNumber());
			order.setPolicyStartDate(dfDate.format(po.getPolicyStartDate()));
			order.setPolicyEndDate(dfDate.format(po.getPolicyEndDate()));
			order.setTotalPremi(po.getTotalPremi());
			order.setHasBeneficiary(po.getHasBeneficiary());
			order.setProductCount(po.getProductCount());
			order.setStatus(po.getStatus());
			order.setCreatedDate(dfTime.format(po.getCreatedDate()));
			order.setTitle("Asuransi kecelakaan");
			order.setImgUrl("https://i.imgur.com/f3h2z7k.jpg");
			
			List<OrderProduct> productList = orderProductMapper.selectByOrderId(po.getOrderId());
			for(OrderProduct op: productList){
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

}
