package com.ninelives.insurance.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.service.ApiClaimService;
import com.ninelives.insurance.api.service.ApiFileUploadService;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.util.GsonUtil;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class ClaimController {
	private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

	
	@Autowired ApiFileUploadService fileUploadService;
	@Autowired ApiClaimService apiClaimService;
			
//	@RequestMapping(value="/orders/{orderId}/claims",
//			method=RequestMethod.POST)	
//	@ResponseBody
//	public Map<String, String> claim(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
//		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
//		requestMap.put("claimId", "xxxxxxx");
//		
//		return requestMap;
//	}
//	@RequestMapping(value="/orders/{orderId}/claims",
//			method=RequestMethod.PUT)	
//	@ResponseBody
//	public Map<String, String> claimPut(@PathVariable("orderId") String orderId, @RequestBody Map<String, String> requestMap){
//		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, requestMap);
//		requestMap.put("claimId", "xxxxxxx");
//		
//		return requestMap;
//	}
//	
	
	@RequestMapping(value="/claimDocuments",
			method={ RequestMethod.POST })
	@ResponseBody
	public List<ClaimDocumentDto> listRequiredClaimDocumentDtos (@RequestAttribute ("authUserId") String authUserId,
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@RequestBody @Valid AccidentClaimDto claimDto) throws AppException{
		if(isValidateOnly){
			return apiClaimService.requiredClaimDocumentDtos(authUserId, claimDto);
		}else{
			throw new AppBadRequestException(ErrorCode.ERR1001_GENERIC_ERROR,"Parameter not supported");
		}
	}
	
	@RequestMapping(value="/claims",
			method={ RequestMethod.POST })
	@ResponseBody
	public AccidentClaimDto submitClaimAccident (@RequestAttribute ("authUserId") String authUserId,
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@RequestBody @Valid AccidentClaimDto claimDto) throws AppException{
		
		return apiClaimService.submitAccidentalClaim(authUserId, claimDto, isValidateOnly);
	}
	
	@RequestMapping(value="/orders/{orderId}/claims",
			method={ RequestMethod.POST })
	@ResponseBody
	public AccidentClaimDto submitClaimAccidentWithOrderId (@RequestAttribute ("authUserId") String authUserId,
			@RequestParam(value="test", defaultValue="false") boolean isValidateOnly,
			@PathVariable("orderId") String orderId,
			@RequestBody @Valid AccidentClaimDto claimDto) throws AppException{

		if (!StringUtils.isEmpty(orderId)) {
			if (claimDto != null){
				if(claimDto.getOrder()==null){
					OrderDto orderDto = new OrderDto();
					orderDto.setOrderId(orderId);
					claimDto.setOrder(orderDto);
				}else if(StringUtils.isEmpty(claimDto.getOrder().getOrderId())){
					claimDto.getOrder().setOrderId(orderId);
				}else if(!orderId.equals(claimDto.getOrder().getOrderId())){
					throw new AppBadRequestException(ErrorCode.ERR4000_ORDER_INVALID, "Transaksi tidak sesuai dengan data klaim");
				}
			}
		}
		return apiClaimService.submitAccidentalClaim(authUserId, claimDto, isValidateOnly);
	}
	
	@RequestMapping(value="/claims/{claimId}",
			method={ RequestMethod.GET })
	@ResponseBody
	public AccidentClaimDto getClaimAccidentByClaimId (@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("claimId") String claimId) throws AppNotFoundException{
//		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
//		logger.debug("param data: {}", claimId);
//		logger.debug("---");		
		
		AccidentClaimDto claimDto = apiClaimService.fetchClaimDtoByClaimId(authUserId, claimId);
		if(claimDto==null){
			throw new AppNotFoundException(ErrorCode.ERR7001_CLAIM_NOT_FOUND,"Klaim tidak ditemukan");
		}
		return claimDto;
	}
	
	@RequestMapping(value="/claims",
			method={ RequestMethod.GET })
	@ResponseBody
	public List<AccidentClaimDto> getClaimAccident (@RequestAttribute ("authUserId") String authUserId,  
			@RequestParam(value="filter",required=false) String filter){
//		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
//		logger.debug("param data: {}", filter);
//		logger.debug("---");
		
		FilterDto filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return apiClaimService.fetchClaimDtos(authUserId, filterDto);
	}
	
	@RequestMapping(value="/orders/{orderId}/claims",
			method={ RequestMethod.GET })
	@ResponseBody
	public List<AccidentClaimDto> getClaimAccidentByOrderId (@RequestAttribute ("authUserId") String authUserId,  
			@PathVariable("orderId") String orderId,
			@RequestParam(value="filter",required=false) String filter){
//		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
//		logger.debug("param data: {} and order: {}", filter, orderId);
//		logger.debug("---");
		
		FilterDto filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return apiClaimService.fetchClaimDtosByOrderId(authUserId, orderId, filterDto);
	}
	
	@RequestMapping(value="/claimDocumentFiles",
			method=RequestMethod.POST)
	@ResponseBody
	public UserFileDto uploadClaimDocumentFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws AppException{
		return fileUploadService.saveTemp(authUserId, file);
	}	
}
