package com.ninelives.insurance.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.PeriodMapper;
import com.ninelives.insurance.core.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.core.mybatis.mapper.ReportCmsMapper;
import com.ninelives.insurance.core.mybatis.mapper.VoucherMapper;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.model.VoucherProduct;

@Service
public class B2B2CService {
	
	@Autowired
	ReportCmsMapper reportCmsMapper;
	
	@Autowired
	VoucherMapper voucherMapper;
	
	@Autowired
	PeriodMapper periodMapper;
	
	@Autowired
	ProductMapper productMapper;
	
	public Voucher selectVoucherByCode(String codeVoucher, String corporateClientId) {
		int id = Integer.parseInt(corporateClientId);
		return voucherMapper.selectByCodeAndCorporateCliendId(codeVoucher,id);
	}

	public int insertVoucher(Voucher voucher) {
		return voucherMapper.insertVoucherSelective(voucher);
		
	}
	
	public int insertVoucherProduct(VoucherProduct voucherProduct) {
		return voucherMapper.insertVoucherProductSelective(voucherProduct);
		
	}

	public int deleteVoucherProduct(String voucherId) {
		int id = Integer.parseInt(voucherId);
		int rValue = voucherMapper.deleteVoucherProductByVoucherId(id);
		return rValue;
		
	}
	
	public List<Voucher> fetchAllVoucherByCorporateClientId() {
		return voucherMapper.selectAllVoucherByCorporateClientId();
	}

	public List<String> getListCoverages(Integer id) {
		return voucherMapper.getListCoverages(id);
	}

	public Voucher selectVoucherById(String voucherId) {
		int id = Integer.parseInt(voucherId);
		return voucherMapper.selectVoucherById(id);
	}

	public int updateVoucher(Voucher voucher) {
		return voucherMapper.updateVoucherSelective(voucher);
	}
	
	public String selectProductAndCoverage(String productId1, String productId2, String productId3,
			String productId4, String productId5, String productId6){
		return periodMapper.selectProductAndCoverage(productId1, productId2, productId3,productId4, productId5, productId6);
	}

	public Product selectProductByCoverageAndPeriod(String coverageId, String periodId) {
		return productMapper.selectProductByCoverageIdAndPeriodId(coverageId,periodId);
	}
	
	public List<Voucher> fetchB2B2CVoucherByDate(String start, String end) {
		return voucherMapper.selectB2B2CVoucherByDate(start, end);
		
	}
	
	public List<Voucher> fetchB2B2CVoucher() {
		return voucherMapper.selectB2B2CVoucher();
		
	}
	
	public List<String> fetchUserB2b2cByVoucherCode() {
		return reportCmsMapper.getUserB2b2cByVoucherCode();
	}
	
	public List<String> fetchUserB2b2cByOrderDate(String start, String end) {
		return reportCmsMapper.getUserB2b2cByOrderDate(start, end);
	}
}
