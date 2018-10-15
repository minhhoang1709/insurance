package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.PolicyClaimFamilyDto;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.ClaimService;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.trx.PolicyClaimTrxService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimBankAccount;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.model.PolicyClaimFamily;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderFamily;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ClaimCoverageStatus;
import com.ninelives.insurance.ref.ClaimDocUsageType;
import com.ninelives.insurance.ref.ClaimStatus;
import com.ninelives.insurance.ref.CoverageCategoryId;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.PolicyStatus;

@Service
public class ApiClaimService {
	private static final Logger logger = LoggerFactory.getLogger(ApiClaimService.class);

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired ClaimService claimService;
	@Autowired FileUploadService fileUploadService;
	@Autowired PolicyClaimTrxService policyClaimTrxService;
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	
	public AccidentClaimDto submitAccidentalClaim(final String userId, final AccidentClaimDto claimDto, boolean isValidateOnly) throws AppException{
		PolicyClaim<PolicyClaimDetailAccident> claim = registerAccidentalClaim(userId, claimDto, isValidateOnly);		
		return modelMapperAdapter.toDto(claim);
	}	
	public AccidentClaimDto fetchClaimDtoByClaimId(String userId, String claimId){
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = claimService.fetchClaimByClaimId(userId, claimId);
		return modelMapperAdapter.toDto(policyClaim);
	}
	public List<AccidentClaimDto> fetchClaimDtosByOrderId(String userId, String orderId, FilterDto filterDto) {
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = fetchClaimsByOrderId(userId, orderId, filterDto);
		List<AccidentClaimDto> dtoList = new ArrayList<>();
		if(policyClaims!=null){
			for(PolicyClaim<PolicyClaimDetailAccident> c: policyClaims){
				AccidentClaimDto dto = modelMapperAdapter.toDto(c);
				dtoList.add(dto);
			}
		}
		return dtoList;
	}
	public List<AccidentClaimDto> fetchClaimDtos(String userId, FilterDto filterDto){
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = fetchClaims(userId, filterDto);
		//logger.debug("dapat policy claims size {}",policyClaims.size());
		List<AccidentClaimDto> dtoList = new ArrayList<>();
		if(policyClaims!=null){
			for(PolicyClaim<PolicyClaimDetailAccident> c: policyClaims){
				AccidentClaimDto dto = modelMapperAdapter.toDto(c);
				dtoList.add(dto);
			}
		}
		//logger.debug("dapat dto size {}",dtoList.size());
		return dtoList;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaimsByOrderId(String userId, String orderId, FilterDto filterDto){
		int offset = claimService.getDefaultFilterOffset();
		int limit = claimService.getDefaultFilterLimit();
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			limit = filterDto.getLimit();
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		return claimService.fetchClaimsByOrderId(userId, orderId, statusSet, limit, offset);		
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaims(String userId, FilterDto filterDto){
		int offset = claimService.getDefaultFilterOffset();
		int limit = claimService.getDefaultFilterLimit();
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			limit = filterDto.getLimit();
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		return claimService.fetchClaims(userId, statusSet, limit, offset);
	}
	
	public PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto, final boolean isValidateOnly) throws AppException{

		logger.info("Start process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>", isValidateOnly, userId, claimDto);
		
		LocalDate today = LocalDate.now();
		
		if(claimDto==null || claimDto.getOrder() == null || StringUtils.isEmpty(claimDto.getOrder().getOrderId())){
			logger.debug("Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error empty data>, exception:<{}>", isValidateOnly, userId, claimDto, ErrorCode.ERR7000_CLAIM_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7000_CLAIM_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali data klaim Anda");
		}
		
		PolicyOrder order = orderService.fetchOrderByOrderId(userId, claimDto.getOrder().getOrderId());
		
		if(order==null){
			logger.debug("Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error order not found>, exception:<{}>", isValidateOnly, userId, claimDto, ErrorCode.ERR7002_CLAIM_ORDER_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7002_CLAIM_ORDER_INVALID, "Permintaan tidak dapat diproses, asuransi Anda tidak ditemukan");
		}
		
		if(!order.getStatus().equals(PolicyStatus.ACTIVE)
				&& !order.getStatus().equals(PolicyStatus.EXPIRED)){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error status not valid>, exception:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7002_CLAIM_ORDER_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7002_CLAIM_ORDER_INVALID, "Permintaan tidak dapat diproses, status asuransi Anda tidak valid");
		}

		//test
//		if(order.getStatus().equals(PolicyStatus.EXPIRED) 
//				&& ChronoUnit.DAYS.between(order.getPolicyEndDate(), today) > config.getClaim().getMaxPolicyEndDatePeriod()){
//			logger.debug(
//					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error expired order pass allowed claim period>, exception:<{}>",
//					isValidateOnly, userId, claimDto, ErrorCode.ERR7011_CLAIM_EXPIRED_ORDER);
//			throw new AppBadRequestException(ErrorCode.ERR7011_CLAIM_EXPIRED_ORDER, "Permintaan tidak dapat diproses, batas waktu pengajuan klaim telah terlewati");
//		}
		
		if(CollectionUtils.isEmpty(claimDto.getClaimCoverages())){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error empty claim coverage>, exception:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali pilihan jaminan Anda");
		}
		
		//all selected claim coverage should match policy
		Set<String> orderCoverageSet = order.getPolicyOrderProducts().stream().map(PolicyOrderProduct::getCoverageId).collect(Collectors.toSet());
		for(ClaimCoverageDto c: claimDto.getClaimCoverages()){
			if(c.getCoverage()==null
					|| !orderCoverageSet.contains(c.getCoverage().getCoverageId())){
				logger.debug(
						"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error selected coverage is not in order>, exception:<{}>",
						isValidateOnly, userId, claimDto, ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID);
				throw new AppBadRequestException(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali pilihan jaminan Anda");
			}
		}
		
		boolean isClaimHasFamily = false;
		if(!CollectionUtils.isEmpty(claimDto.getFamilies())){
			isClaimHasFamily = true;
		}		
		
		if(CollectionUtils.isEmpty(claimDto.getClaimDocuments())){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error empty document>, exception:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7004_CLAIM_DOCUMENT_EMPTY);
			throw new AppBadRequestException(ErrorCode.ERR7004_CLAIM_DOCUMENT_EMPTY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
		}
		//logger.debug("Policyproducts from order adalah {}", order.getPolicyOrderProducts());
		
		//check that document/file is in claim
		Set<String> docFromClaimSet = new HashSet<>();
		List<Long> fileIds = new ArrayList<>();
		int claimDocCount = 0;
		for(ClaimDocumentDto cd: claimDto.getClaimDocuments()){
			if(cd.getClaimDocType()==null || StringUtils.isEmpty(cd.getClaimDocType().getClaimDocTypeId())){
				logger.debug(
						"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error empty claim doc type>, exception:<{}>",
						isValidateOnly, userId, claimDto, ErrorCode.ERR7005_CLAIM_DOCUMENT_INVALID);
				throw new AppBadRequestException(ErrorCode.ERR7005_CLAIM_DOCUMENT_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
			}
			else if(cd.getFile()==null||cd.getFile().getFileId()==null){
				logger.debug(
						"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error empty file-id>, exception:<{}>",
						isValidateOnly, userId, claimDto, ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID);
				throw new AppBadRequestException(ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
			}
			docFromClaimSet.add(cd.getClaimDocType().getClaimDocTypeId());
			fileIds.add(cd.getFile().getFileId());
			claimDocCount++;
		}
		
		//mandatory doc check exists
		Map<String, Boolean> docTypeMap = extractDocTypeMap(claimDto.getClaimCoverages(), isClaimHasFamily);		
		for(Map.Entry<String, Boolean> doc: docTypeMap.entrySet()){
			if(doc.getValue()==true){
				if(!docFromClaimSet.contains(doc.getKey())){
					logger.debug(
							"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error missing mandatory doc>, exception:<{}>, doc:<{}>",
							isValidateOnly, userId, claimDto, ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, doc.getKey());
					throw new AppBadRequestException(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
				}
			}
		}
		
		//check that all file already uploaded for each claim document
		//int uploadedFileCount = fileUploadService.countUploadedTempFile(userId, fileIds);
		List<UserFile> userFiles = fileUploadService.selectUploadedTempFile(userId, fileIds);
		if(userFiles.size()!=claimDocCount){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error doc count not match>, exception:<{}>, found:<{}>, declared:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, userFiles.size(), claimDocCount);
			throw new AppBadRequestException(ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
		}
		Map<Long, UserFile> userFileMap = userFiles.stream().collect(Collectors.toMap(c -> c.getFileId(), c->c));
		
		if(!isPolicyClaimAccidentDetailIsValid(claimDto.getAccidentAddress())){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error address>, exception:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7008_CLAIM_DETAIL_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7008_CLAIM_DETAIL_INVALID, "Permintaan tidak dapat diproses, silahkan cek alamat Anda");
		}
		
		if(!isPolicyClaimBankAccountIsValid(claimDto.getClaimBankAccount())){
			logger.debug(
					"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error bank info>, exception:<{}>",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7009_CLAIM_BANK_ACCOUNT_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR7009_CLAIM_BANK_ACCOUNT_INVALID, "Permintaan tidak dapat diproses, silahkan info bank Anda");
		}
		
		if(!CollectionUtils.isEmpty(claimDto.getFamilies())){
			if(order.getIsFamily()==false){
				logger.debug(
						"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<error order has no family>, exception:<{}>",
						isValidateOnly, userId, claimDto, ErrorCode.ERR7010_CLAIM_ORDER_FAMILY);
				throw new AppBadRequestException(ErrorCode.ERR7010_CLAIM_ORDER_FAMILY, "Permintaan tidak dapat diproses, pilihan keluarga tidak tersedia");				
			}
		}
		
		//logger.debug("Hasil mapping {}", docTypeMap);
		
		//TODO: verify family
		
		PolicyClaim<PolicyClaimDetailAccident> claim = null;
		
		if(!isValidateOnly){
			claim = new PolicyClaim<>();
			claim.setCoverageCategoryId(order.getCoverageCategoryId());
			claim.setClaimId(claimService.generateClaimId());
			claim.setOrderId(claimDto.getOrder().getOrderId());
			claim.setUserId(userId);
			claim.setClaimDate(today);
			claim.setIncidentDateTime(claimDto.getAccidentDate());
			claim.setIncidentSummary(claimDto.getAccidentSummary());
			claim.setStatus(ClaimStatus.SUBMITTED);
			
			PolicyClaimDetailAccident claimDetail = new PolicyClaimDetailAccident();
			claimDetail.setClaimId(claim.getClaimId());
			claimDetail.setAccidentAddress(claimDto.getAccidentAddress().getAddress());
			claimDetail.setAccidentAddressCity(claimDto.getAccidentAddress().getCity());
			claimDetail.setAccidentAddressProvince(claimDto.getAccidentAddress().getProvince());
			claimDetail.setAccidentAddressCountry(claimDto.getAccidentAddress().getCountry());
			claim.setPolicyClaimDetail(claimDetail);
			
			PolicyClaimBankAccount claimAccount = new PolicyClaimBankAccount();
			claimAccount.setClaimId(claim.getClaimId());
			claimAccount.setAccountName(claimDto.getClaimBankAccount().getName());
			claimAccount.setAccountBankName(claimDto.getClaimBankAccount().getBankName());
			//claimAccount.setAccountBankSwift(claimDto.getClaimBankAccount().getBankSwitt());
			claimAccount.setAccountBankSwiftCode(claimDto.getClaimBankAccount().getBankSwiftCode());
			claimAccount.setAccountNumber(claimDto.getClaimBankAccount().getAccount());
			claim.setPolicyClaimBankAccount(claimAccount);
			
			List<PolicyClaimDocument> claimDocs = new ArrayList<>();
			for(ClaimDocumentDto c: claimDto.getClaimDocuments()){
				PolicyClaimDocument doc = new PolicyClaimDocument();
				doc.setClaimId(claim.getClaimId());
				doc.setClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId());
				doc.setClaimDocType(productService.fetchClaimDocTypeByClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId()));
				doc.setFileId(c.getFile().getFileId());
				doc.setUserFile(userFileMap.get(c.getFile().getFileId()));
				if(c.getExtra()!=null){
					doc.setExtra(c.getExtra().toString());
				}
				claimDocs.add(doc);
			}
			claim.setPolicyClaimDocuments(claimDocs);		
			
			List<PolicyClaimCoverage> claimCovs = new ArrayList<>();
			for(ClaimCoverageDto c: claimDto.getClaimCoverages()){
				PolicyClaimCoverage cov = new PolicyClaimCoverage();
				cov.setClaimId(claim.getClaimId());
				cov.setCoverageId(c.getCoverage().getCoverageId());			
				cov.setStatus(ClaimCoverageStatus.SUBMITTED);
				cov.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverage().getCoverageId()));
				claimCovs.add(cov);
			}
			claim.setPolicyClaimCoverages(claimCovs);
			

			/*
			 *	claim for family(TRAVEL related)
			 *	as Oct 2018, select one of these options:
			 *  1. User as claimant
			 *  2. 1 family member as claimant
			 *  3. All family member as claimant
			 */
			if(order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_DOMESTIC) ||
					order.getCoverageCategoryId().equals(CoverageCategoryId.TRAVEL_INTERNATIONAL)){
				if(isClaimHasFamily){					
					PolicyClaimFamilyDto famDto = claimDto.getFamilies().get(0);					
					List<PolicyClaimFamily> claimFamilies = new ArrayList<>();
					for(PolicyOrderFamily orderClaimFamily: order.getPolicyOrderFamilies()){
						if(orderClaimFamily.getSubId().equals(famDto.getSubId())){
							PolicyClaimFamily claimFamily = new PolicyClaimFamily();					
							claimFamily.setClaimId(claim.getClaimId());
							claimFamily.setSubId(orderClaimFamily.getSubId());
							claimFamily.setName(orderClaimFamily.getName());
							claimFamily.setBirthDate(orderClaimFamily.getBirthDate());
							claimFamily.setGender(orderClaimFamily.getGender());
							claimFamily.setRelationship(orderClaimFamily.getRelationship());
							claimFamilies.add(claimFamily);
						}
					}
					claim.setIsUserClaimant(false);
					claim.setHasFamily(true);
					claim.setPolicyClaimFamilies(claimFamilies);								
				}else{
					claim.setIsUserClaimant(true);
					claim.setHasFamily(false);
				}
			}
			
			
			policyClaimTrxService.registerPolicyClaim(claim);
			
			//move file from temp to claim
			claimService.updateClaimFiles(claim.getPolicyClaimDocuments());
			
			//TODO Process ASWATA claim (submit to QUEUE)
		}
		
		logger.debug(
				"Process claim isvalidationonly:<{}>, userId:<{}>, claim:<{}>, result:<success>, insert:<{}>",
				isValidateOnly, userId, claimDto, claim==null?"null":claim);
		
		return claim;
	}
	
	private boolean isPolicyClaimBankAccountIsValid(ClaimBankAccountDto cba) {
		if(cba == null
				||StringUtils.isEmpty(cba.getName())
				||StringUtils.isEmpty(cba.getAccount())
				||StringUtils.isEmpty(cba.getBankName())
				||StringUtils.isEmpty(cba.getBankSwiftCode())
				){
			return false;
		}
		
		return true;
	}
	protected boolean isPolicyClaimAccidentDetailIsValid(ClaimDetailAccidentAddressDto cd){
		if(cd == null
				|| StringUtils.isEmpty(cd.getAddress())
				|| StringUtils.isEmpty(cd.getCity())
				|| StringUtils.isEmpty(cd.getProvince())
				|| StringUtils.isEmpty(cd.getCountry())){
			return false;
		}
		
		return true;
	}
	
	protected Map<String, Boolean> extractDocTypeMap(List<ClaimCoverageDto> ccds, boolean isClaimHasFamily) {
		Map<String, Boolean> docTypeMap = new HashMap<>();
		for (ClaimCoverageDto ccd : ccds) {
			Coverage c = productService.fetchCoverageByCoverageId(ccd.getCoverage().getCoverageId());
			for (CoverageClaimDocType ccdt : c.getCoverageClaimDocTypes()) {
				if (!ccdt.getClaimDocType().getUsageType().equals(ClaimDocUsageType.FAMILY_CARD) || isClaimHasFamily) {
					//if it is family-card type then verify whether to include in document requirement by checking whether claim has family as claimant
					Boolean lastMandatoryCheck = docTypeMap.get(ccdt.getClaimDocTypeId());
					if (lastMandatoryCheck != null) {
						docTypeMap.put(ccdt.getClaimDocTypeId(), ccdt.getIsMandatory() || lastMandatoryCheck);
					} else {
						docTypeMap.put(ccdt.getClaimDocTypeId(), ccdt.getIsMandatory());
					}
				}
			}
		}
		return docTypeMap;
	}

}
