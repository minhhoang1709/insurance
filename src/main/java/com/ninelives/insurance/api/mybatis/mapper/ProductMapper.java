package com.ninelives.insurance.api.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.ninelives.insurance.api.model.Period;
import com.ninelives.insurance.api.model.Product;

@Mapper
public interface ProductMapper {
		
	List<Product> selectByStatusActive();
}
