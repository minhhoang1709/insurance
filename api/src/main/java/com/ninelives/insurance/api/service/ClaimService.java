package com.ninelives.insurance.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimBankAccountDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDetailAccidentAddressDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.mybatis.mapper.PolicyClaimMapper;
import com.ninelives.insurance.api.service.trx.PolicyClaimTrxService;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageClaimDocType;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimBankAccount;
import com.ninelives.insurance.model.PolicyClaimCoverage;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ClaimCoverageStatus;
import com.ninelives.insurance.ref.ClaimStatus;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.PolicyStatus;
import com.ninelives.insurance.ref.UserFileStatus;

@Service
public class ClaimService {
	private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@Autowired FileUploadService fileUploadService;
	@Autowired PolicyClaimTrxService policyClaimTrxService;
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired PolicyClaimMapper policyClaimMapper;
	
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Value("${ninelives.claim.filter-limit:100}")
	int defaultFilterLimit;
	
	@Value("${ninelives.claim.filter-max-limit:100}")
	int maxFilterLimit;
	
	@Value("${ninelives.claim.filter-offset:0}")
	int defaultFilterOffset;

	
	public AccidentClaimDto submitAccidentalClaim(final String userId, final AccidentClaimDto claimDto, boolean isValidateOnly) throws ApiException{
		PolicyClaim<PolicyClaimDetailAccident> claim = registerAccidentalClaim(userId, claimDto, isValidateOnly);		
		return modelMapperAdapter.toDto(claim);
	}	
	public AccidentClaimDto fetchClaimDtoByClaimId(String userId, String claimId){
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = fetchClaimByClaimId(userId, claimId);
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
		logger.debug("dapat policy claims size {}",policyClaims.size());
		List<AccidentClaimDto> dtoList = new ArrayList<>();
		if(policyClaims!=null){
			for(PolicyClaim<PolicyClaimDetailAccident> c: policyClaims){
				AccidentClaimDto dto = modelMapperAdapter.toDto(c);
				dtoList.add(dto);
			}
		}
		logger.debug("dapat dto size {}",dtoList.size());
		return dtoList;
	}
	
	public PolicyClaim<PolicyClaimDetailAccident> fetchClaimByClaimId(String userId, String claimId){		
		PolicyClaim<PolicyClaimDetailAccident> policyClaim = policyClaimMapper.selectByUserIdAndClaimId(userId, claimId);
		if(policyClaim!=null){
			for(PolicyClaimCoverage c: policyClaim.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));		
			}
			if(policyClaim.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(policyClaim.getPolicyOrder(), LocalDate.now());
			}
		}
		
		return policyClaim;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaimsByOrderId(String userId, String orderId, FilterDto filterDto){
		int offset = this.defaultFilterOffset;
		int limit = this.defaultFilterLimit;
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			if(filterDto.getLimit() > this.maxFilterLimit){
				limit = this.maxFilterLimit;
			}else{
				limit = filterDto.getLimit();
			}
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndOrderIdAndStatusSet(userId, orderId, statusSet, limit, offset);
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			for(PolicyClaimCoverage c: p.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));				
			}
			if(p.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(p.getPolicyOrder(), today);
			}
		}
		return policyClaims;
	}
	
	public List<PolicyClaim<PolicyClaimDetailAccident>> fetchClaims(String userId, FilterDto filterDto){
		int offset = this.defaultFilterOffset;
		int limit = this.defaultFilterLimit;
		Set<String> statusSet = null;
		if(filterDto!=null){
			offset = filterDto.getOffset();
			if(filterDto.getLimit() > this.maxFilterLimit){
				limit = this.maxFilterLimit;
			}else{
				limit = filterDto.getLimit();
			}
			if(filterDto.getStatus()!=null && filterDto.getStatus().length > 0){
				statusSet = Arrays.stream(filterDto.getStatus()).collect(Collectors.toSet());				
			}
		}
		
		LocalDate today = LocalDate.now();
		List<PolicyClaim<PolicyClaimDetailAccident>> policyClaims = policyClaimMapper.selectByUserIdAndStatusSet(userId, statusSet, limit, offset);
		logger.debug("Size nya sebelum di loop {}",policyClaims.size());
		
		for(PolicyClaim<PolicyClaimDetailAccident> p: policyClaims){
			for(PolicyClaimCoverage c: p.getPolicyClaimCoverages()){
				c.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverageId()));				
			}
			if(p.getPolicyOrder()!=null){
				orderService.mapPolicyOrderStatus(p.getPolicyOrder(), today);
			}
		}
		return policyClaims;
	}
	
	public PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto, final boolean isValidateOnly) throws ApiException{

		logger.debug("Process isvalidationonly {} claim for user {} with claim {}", isValidateOnly, userId, claimDto);
		
		LocalDate today = LocalDate.now();
		
		if(claimDto==null || claimDto.getOrder() == null || StringUtils.isEmpty(claimDto.getOrder().getOrderId())){
			logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7000_CLAIM_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7000_CLAIM_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali data klaim Anda");
		}
		
		PolicyOrder order = orderService.fetchOrderByOrderId(userId, claimDto.getOrder().getOrderId());
		
		if(order==null){
			logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7002_CLAIM_ORDER_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7002_CLAIM_ORDER_INVALID, "Permintaan tidak dapat diproses, asuransi Anda tidak ditemukan");
		}
		
		if(!order.getStatus().equals(PolicyStatus.ACTIVE)
				&& !order.getStatus().equals(PolicyStatus.EXPIRED)){
			logger.debug(
					"Process isvalidationonly {} claim for user {} with claim {} result: exception {}, status order not valid",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7002_CLAIM_ORDER_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7002_CLAIM_ORDER_INVALID, "Permintaan tidak dapat diproses, status asuransi Anda tidak valid");
		}
		
		if(CollectionUtils.isEmpty(claimDto.getClaimCoverages())){
			logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali pilihan jaminan Anda");
		}
		
		//all selected claim coverage should match policy
		Set<String> orderCoverageSet = order.getPolicyOrderProducts().stream().map(PolicyOrderProduct::getCoverageId).collect(Collectors.toSet());
		for(ClaimCoverageDto c: claimDto.getClaimCoverages()){
			if(c.getCoverage()==null
					|| !orderCoverageSet.contains(c.getCoverage().getCoverageId())){
				logger.debug(
						"Process isvalidationonly {} claim for user {} with claim {} result: exception {}, selected coverage is not in order",
						isValidateOnly, userId, claimDto, ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID);
				throw new ApiBadRequestException(ErrorCode.ERR7003_CLAIM_COVERAGE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali pilihan jaminan Anda");
			}
		}
		
		if(CollectionUtils.isEmpty(claimDto.getClaimDocuments())){
			logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7004_CLAIM_DOCUMENT_EMPTY);
			throw new ApiBadRequestException(ErrorCode.ERR7004_CLAIM_DOCUMENT_EMPTY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
		}
		//logger.debug("Policyproducts from order adalah {}", order.getPolicyOrderProducts());
		
		//check that document/file is in claim
		Set<String> docFromClaimSet = new HashSet<>();
		List<Long> fileIds = new ArrayList<>();
		int claimDocCount = 0;
		for(ClaimDocumentDto cd: claimDto.getClaimDocuments()){
			if(cd.getClaimDocType()==null || StringUtils.isEmpty(cd.getClaimDocType().getClaimDocTypeId())){
				logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7005_CLAIM_DOCUMENT_INVALID);
				throw new ApiBadRequestException(ErrorCode.ERR7005_CLAIM_DOCUMENT_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
			}
			else if(cd.getFile()==null||cd.getFile().getFileId()==null){
				logger.debug("Process isvalidationonly {} claim for user {} with claim {} result: exception {} ", isValidateOnly, userId, claimDto, ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID);
				throw new ApiBadRequestException(ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
			}
			docFromClaimSet.add(cd.getClaimDocType().getClaimDocTypeId());
			fileIds.add(cd.getFile().getFileId());
			claimDocCount++;
		}
		
		//mandatory doc check exists
		Map<String, Boolean> docTypeMap = extractDocTypeMap(claimDto.getClaimCoverages());		
		for(Map.Entry<String, Boolean> doc: docTypeMap.entrySet()){
			if(doc.getValue()==true){
				if(!docFromClaimSet.contains(doc.getKey())){
					logger.debug(
							"Process isvalidationonly {} claim for user {} with claim {} result: exception {}, missing doc {} ",
							isValidateOnly, userId, claimDto, ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, doc.getKey());
					throw new ApiBadRequestException(ErrorCode.ERR7006_CLAIM_DOCUMENT_MANDATORY, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
				}
			}
		}
		
		//check that all file already uploaded for each claim document
		//int uploadedFileCount = fileUploadService.countUploadedTempFile(userId, fileIds);
		List<UserFile> userFiles = fileUploadService.selectUploadedTempFile(userId, fileIds);
		if(userFiles.size()!=claimDocCount){
			logger.debug(
					"Process isvalidationonly {} claim for user {} with claim {} result: exception {} found {} declared {}",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, userFiles.size(), claimDocCount);
			throw new ApiBadRequestException(ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, "Permintaan tidak dapat diproses, silahkan cek kembali dokumen Anda");
		}
		Map<Long, UserFile> userFileMap = userFiles.stream().collect(Collectors.toMap(c -> c.getFileId(), c->c));
		
		if(!isPolicyClaimAccidentDetailIsValid(claimDto.getAccidentAddress())){
			logger.debug(
					"Process isvalidationonly {} claim for user {} with claim {} result: exception {}",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7008_CLAIM_DETAIL_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7008_CLAIM_DETAIL_INVALID, "Permintaan tidak dapat diproses, silahkan cek alamat Anda");
		}
		
		if(!isPolicyClaimBankAccountIsValid(claimDto.getClaimBankAccount())){
			logger.debug(
					"Process isvalidationonly {} claim for user {} with claim {} result: exception {}",
					isValidateOnly, userId, claimDto, ErrorCode.ERR7009_CLAIM_BANK_ACCOUNT_INVALID);
			throw new ApiBadRequestException(ErrorCode.ERR7009_CLAIM_BANK_ACCOUNT_INVALID, "Permintaan tidak dapat diproses, silahkan info bank Anda");
		}
		
		//logger.debug("Hasil mapping {}", docTypeMap);
		

		PolicyClaim<PolicyClaimDetailAccident> claim = null;
		
		if(!isValidateOnly){
			claim = new PolicyClaim<>();
			claim.setCoverageCategoryId(order.getCoverageCategoryId());
			claim.setClaimId(generateClaimId());
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
			
			policyClaimTrxService.registerPolicyClaim(claim);
			
			//move file from temp to claim
			updateClaimFiles(claim.getPolicyClaimDocuments());
			
			//TODO Process ASWATA claim (submit to QUEUE)
		}
		
		logger.debug(
				"Process isvalidationonly {} claim for user {} with claim {}, insert {} result: sucess",
				isValidateOnly, userId, claimDto, claim==null?"null":claim);
		
		return claim;
	}
	
	private void updateClaimFiles(List<PolicyClaimDocument> docs) throws ApiException{
		for(PolicyClaimDocument doc: docs){
			fileUploadService.moveTemp(doc.getUserFile(), FileUseType.CLAIM);
		}
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
	
	protected Map<String, Boolean> extractDocTypeMap (List<ClaimCoverageDto> ccds) {
		Map<String, Boolean> docTypeMap = new HashMap<>();
		for(ClaimCoverageDto ccd: ccds){
			Coverage c = productService.fetchCoverageByCoverageId(ccd.getCoverage().getCoverageId());
			for(CoverageClaimDocType ccdt: c.getCoverageClaimDocTypes()){
				Boolean current = docTypeMap.get(ccdt.getClaimDocTypeId()); 
				if(current!=null){
					docTypeMap.put(ccdt.getClaimDocTypeId(), ccdt.getIsMandatory()||current);
				}else{
					docTypeMap.put(ccdt.getClaimDocTypeId(), ccdt.getIsMandatory());
				}				
			}

		}
		return docTypeMap;
	}
//	protected Map<String, Boolean> extractDocTypeMap (List<PolicyOrderProduct> pops) {
//		Map<String, Boolean> docTypeMap = pops.stream()
//			.flatMap(c -> c.getCoverageClaimDocTypes().stream())
//			.collect(Collectors.toMap(
//				CoverageClaimDocType::getClaimDocTypeId, CoverageClaimDocType::getIsMandatory, (v1, v2) -> v1 || v2));
//		
//		return docTypeMap;
//	}		
	
//	private PolicyClaim<PolicyClaimDetailAccident> registerAccidentalClaim(final String userId, final AccidentClaimDto claimDto){
//		//check if coverage require such doctype
//		//check if doc is complete for mandatory case
//		//check if order status is valid (not submitted or inpayment)
//		
//		LocalDate today = LocalDate.now();
//		
//		PolicyClaim<PolicyClaimDetailAccident> claim = new PolicyClaim<>();
//		claim.setCoverageCategoryId("101"); //TODO: remove hardcoded
//		claim.setClaimId(generateClaimId());
//		claim.setOrderId(claimDto.getOrder().getOrderId());
//		claim.setUserId(userId);
//		claim.setClaimDate(today);
//		claim.setIncidentDateTime(claimDto.getAccidentDate());
//		claim.setIncidentSummary(claimDto.getAccidentSummary());
//		claim.setStatus(ClaimStatus.SUBMITTED);
//		
//		PolicyClaimDetailAccident claimDetail = new PolicyClaimDetailAccident();
//		claimDetail.setClaimId(claim.getClaimId());
//		claimDetail.setAccidentAddress(claimDto.getAccidentAddress().getAddress());
//		claimDetail.setAccidentAddressCity(claimDto.getAccidentAddress().getCity());
//		claimDetail.setAccidentAddressProvince(claimDto.getAccidentAddress().getProvince());
//		claimDetail.setAccidentAddressCountry(claimDto.getAccidentAddress().getCountry());
//		claim.setPolicyClaimDetail(claimDetail);
//		
//		PolicyClaimBankAccount claimAccount = new PolicyClaimBankAccount();
//		claimAccount.setClaimId(claim.getClaimId());
//		claimAccount.setAccountName(claimDto.getClaimBankAccount().getName());
//		claimAccount.setAccountBankName(claimDto.getClaimBankAccount().getBankName());
//		claimAccount.setAccountBankSwift(claimDto.getClaimBankAccount().getBankSwitt());
//		claimAccount.setAccountBankSwiftCode(claimDto.getClaimBankAccount().getBankSwiftCode());
//		claimAccount.setAccountNumber(claimDto.getClaimBankAccount().getAccount());
//		claim.setPolicyClaimBankAccount(claimAccount);
//		
//		List<PolicyClaimDocument> claimDocs = new ArrayList<>();
//		for(ClaimDocumentDto c: claimDto.getClaimDocuments()){
//			PolicyClaimDocument doc = new PolicyClaimDocument();
//			doc.setClaimId(claim.getClaimId());
//			doc.setClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId());
//			doc.setClaimDocType(productService.fetchClaimDocTypeByClaimDocTypeId(c.getClaimDocType().getClaimDocTypeId()));
//			doc.setFileId(c.getFile().getFileId());
//			claimDocs.add(doc);
//		}
//		claim.setPolicyClaimDocuments(claimDocs);		
//		
//		List<PolicyClaimCoverage> claimCovs = new ArrayList<>();
//		for(ClaimCoverageDto c: claimDto.getClaimCoverages()){
//			PolicyClaimCoverage cov = new PolicyClaimCoverage();
//			cov.setClaimId(claim.getClaimId());
//			cov.setCoverageId(c.getCoverage().getCoverageId());			
//			cov.setStatus(ClaimCoverageStatus.SUBMITTED);
//			cov.setCoverage(productService.fetchCoverageByCoverageId(c.getCoverage().getCoverageId()));
//			claimCovs.add(cov);
//		}
//		claim.setPolicyClaimCoverages(claimCovs);		
//		
//		policyClaimTrxService.registerPolicyClaim(claim);
//		
//		
//		return claim;
//	}
	
	
	
	private String generateClaimId(){
		return UUID.randomUUID().toString();
	}
	

}
