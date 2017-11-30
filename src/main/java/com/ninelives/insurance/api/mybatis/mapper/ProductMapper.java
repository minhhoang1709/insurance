package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ninelives.insurance.api.model.Product;

@Mapper
public interface ProductMapper {
		
	Product selectByProductId(@Param("productId") String productId);
	List<Product> select();
	List<Product> selectByStatusActive();	
	List<Product> selectByProductIds(@Param("productIds") Set<String> productIds);	
}
