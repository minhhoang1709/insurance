package com.ninelives.insurance.core.service;

import java.time.LocalDate;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.core.mybatis.mapper.VoucherMapper;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.VoucherType;

@Service
public class VoucherService {
	private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

	@Autowired
	ProductService productService;
	@Autowired
	OrderService orderService;
	@Autowired
	VoucherMapper voucherMapper;
	@Autowired
	PolicyOrderMapper policyOrderMapper;

	@Cacheable("InviteVoucher")
	public Voucher fetchVoucherForInviteById(int voucherId) {
		Voucher voucher = voucherMapper.selectVoucherForInviteById(voucherId);
		if (voucher != null && StringUtils.isEmpty(voucher.getCode())) {
			voucher.setCode(null);
		}
		return voucher;
	}

	@Cacheable("VoucherById")
	public Voucher fetchVoucherbyId(int voucherId) {
		return voucherMapper.selectById(voucherId);
	}

	public Voucher fetchVoucherForInviteByUserId(String userId) {
		return voucherMapper.selectVoucherForInviteByUserId(userId);
	}

	public Voucher fetchVoucherByCode(String code) throws AppNotFoundException {
		return fetchVoucherByCode(code, null, null, true);
	}

	public Voucher fetchVoucherByCode(String code, VoucherType voucherType) throws AppNotFoundException {
		return fetchVoucherByCode(code, null, voucherType, true);
	}
	
	public Voucher fetchVoucherByCode(String code, VoucherType voucherType, boolean isValidateUseable)
			throws AppNotFoundException {
		return fetchVoucherByCode(code, null, voucherType, true);
	}

	
	public Voucher fetchVoucherByCode(String code, String userId) throws AppNotFoundException {
		return fetchVoucherByCode(code, userId, null, true);
	}

	public Voucher fetchVoucherByCode(String code, String userId, VoucherType voucherType, boolean isValidateUseable)
			throws AppNotFoundException {
		// TODO return voucher from cache for B2B (non-invite)
		LocalDate today = LocalDate.now();

		if (StringUtils.isEmpty(code)) {
			throw new AppNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}

		Voucher voucher = null;

		if (voucherType != null && VoucherType.INVITE.equals(voucherType)) {
			voucher = voucherMapper.selectByInviteCode(code);
		} else {
			voucher = voucherMapper.selectByCode(code);

			if (voucher == null) {
				voucher = voucherMapper.selectByInviteCode(code);
			}
		}

		if (voucher != null) {
			if (!CollectionUtils.isEmpty(voucher.getProducts())) {
				for (Product p : voucher.getProducts()) {
					Product cachedProduct = productService.fetchProductByProductId(p.getProductId());
					p.setName(cachedProduct.getName());
					p.setCoverage(cachedProduct.getCoverage());
					p.setCoverageId(cachedProduct.getCoverageId());
					p.setPeriod(cachedProduct.getPeriod());
					p.setPeriodId(cachedProduct.getPeriodId());
					p.setBasePremi(cachedProduct.getPremi());
					p.setStatus(cachedProduct.getStatus());
				}
				if (voucher.getPeriod() == null || voucher.getPeriod().getUnit() == null) {
					voucher.setPeriod(voucher.getProducts().get(0).getPeriod());
				}

			}

			// incase of invite voucher, set the code from parameter since the code is not
			// stored in table voucher
			if (StringUtils.isEmpty(voucher.getCode())) {
				voucher.setCode(code);
			}
			if (voucher.getVoucherType().equals(VoucherType.INVITE)) {
				voucher.setPolicyStartDate(today);
				voucher.setPolicyEndDate(orderService.calculatePolicyEndDate(today, voucher.getPeriod()));
			} else if (voucher.getVoucherType().equals(VoucherType.B2B)) {
				if (isValidateUseable) {
					if (isUseExpired(voucher)) {
						throw new AppNotFoundException(ErrorCode.ERR9002_VOUCHER_EXPIRED,
								"Masa berlaku kode ini sudah habis.");
					}
				}
			} else if (voucher.getVoucherType().equals(VoucherType.FREE_PROMO_NEW_USER)) {
				if (isValidateUseable) {
					if (isUseExpired(voucher)) {
						throw new AppNotFoundException(ErrorCode.ERR9002_VOUCHER_EXPIRED,
								"Masa berlaku kode ini sudah habis.");
					} else if (voucher.getMaxUse() != null && voucher.getApproveCnt() != null
							&& voucher.getApproveCnt() >= voucher.getMaxUse()) {
						throw new AppNotFoundException(ErrorCode.ERR9003_VOUCHER_OVERUSE,
								"Kode ini sudah tidak tersedia.");
					}
				}
				voucher.setPolicyStartDate(today);
				voucher.setPolicyEndDate(orderService.calculatePolicyEndDate(today, voucher.getPeriod()));
			} else if (voucher.getVoucherType().equals(VoucherType.B2B2C)) {
				if (isValidateUseable) {
					if (isUseExpired(voucher)) {
						throw new AppNotFoundException(ErrorCode.ERR9002_VOUCHER_EXPIRED,
								"Masa berlaku kode ini sudah habis.");
					} else if (voucher.getMaxUse() != null && voucher.getApproveCnt() != null
							&& voucher.getApproveCnt() >= voucher.getMaxUse()) {
						throw new AppNotFoundException(ErrorCode.ERR9003_VOUCHER_OVERUSE,
								"Kode ini sudah tidak tersedia.");
					} else if (isUsedVoucher(code, userId)) {
						throw new AppNotFoundException(ErrorCode.ERR9004_VOUCHER_DUPLICATE,
								"Kode ini tidak dapat digunakan kembali");
					}
				}
				// resolved user policy start date
				voucher.setPolicyStartDate(orderService.resolveB2b2cVoucherPolicyStartDate(userId,
						voucherMapper.getListCoverages(voucher.getId()), today));
				voucher.setPolicyEndDate(orderService.calculatePolicyEndDate(today, voucher.getPeriod()));
			}
		}

		if (voucher == null) {
			throw new AppNotFoundException(ErrorCode.ERR9001_VOUCHER_NOT_FOUND, "Asuransi gratis tidak ditemukan");
		}

		return voucher;
	}

	public boolean isUsable(Voucher voucher) {
		if (voucher != null) {
			if (voucher.getVoucherType() != null) {
				if (voucher.getVoucherType().equals(VoucherType.B2B)) {
					if (isUseExpired(voucher)) {
						return false;
					}
				} else if (voucher.getVoucherType().equals(VoucherType.FREE_PROMO_NEW_USER)) {
					if (isUseExpired(voucher)) {
						return false;
					} else if (voucher.getMaxUse() != null && voucher.getApproveCnt() != null
							&& voucher.getApproveCnt() >= voucher.getMaxUse()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean isUseExpired(Voucher voucher) {
		LocalDate today = LocalDate.now();
		if (today.isBefore(voucher.getUseStartDate()) || today.isAfter(voucher.getUseEndDate())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUsedVoucher(String code, String userId) {
		if (policyOrderMapper.countPolicyOrderVoucherByUserId(code, userId) != 0) {
			return true;
		} else {
			return false;
		}
	}

	public void increaseInviterRewardCounter(String code, String userId) {
		voucherMapper.increamentInviterRewardCount(code, userId);
	}

	public void increaseVoucherApproveCounter(int id) {
		voucherMapper.increamentVoucherApproveCount(id);
	}
}
