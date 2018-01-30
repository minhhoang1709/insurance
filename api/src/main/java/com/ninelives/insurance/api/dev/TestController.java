package com.ninelives.insurance.api.dev;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.FluentProducerTemplate;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.UserMapper;
import com.ninelives.insurance.api.provider.insurance.AswataInsuranceProvider;
import com.ninelives.insurance.api.provider.storage.StorageException;
import com.ninelives.insurance.api.provider.storage.StorageProvider;
import com.ninelives.insurance.api.service.ClaimService;
import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.api.service.ProductService;
import com.ninelives.insurance.api.service.TestService;
import com.ninelives.insurance.api.service.UserService;
import com.ninelives.insurance.api.service.VoucherService;
import com.ninelives.insurance.api.util.GsonUtil;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.route.EndPointRef;

@Controller
@Profile("dev")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired ProductService productService;
	@Autowired OrderService orderService;
	@Autowired ClaimService claimService;
	@Autowired TestService testService;
	@Autowired VoucherService voucherService;
	@Autowired StorageProvider storageService;
	
	@Autowired AswataInsuranceProvider aswata;
	
	@Autowired ProductMapper productMapper;
	@Autowired PolicyOrderMapper policyOrderMapper;
	
	@Autowired UserService userService;
	@Autowired UserMapper userMapper;
	
	
	@Autowired FluentProducerTemplate producerTemplate;
	
	@Value("${ninelives.order.list-limmit:50}")
	int defaultFilterLimit;
	
	@Value("${ninelives.order.list-offset:0}")
	int defaultFilterOffset;
	
	
	
	@GetMapping("/test/aswata/order")
	@ResponseBody
	public String testAswataOrder(@RequestAttribute ("authUserId") String authUserId) throws ApiNotFoundException, IOException, StorageException{
		aswata.orderPolicy(null);
		return "ok";
	}
	

	
	@RequestMapping(value="/test/full/vouchers/{code}",
			method={ RequestMethod.GET })
	@ResponseBody
	public Voucher getVoucher(@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("code") String code) throws ApiNotFoundException{
		return voucherService.fetchVoucherByCode(code);
	}
	
	@RequestMapping("/test/error/generic")
	public String errorGeneric() throws Exception{
		
		throw new Exception("olalala");		
		//return "ok";
	}

	@RequestMapping("/test/error/login")
	public String errorLogin() throws ApiNotFoundException{
		
		throw new ApiNotFoundException(ErrorCode.ERR2001_LOGIN_FAILURE,"olalala login gagal");		
		//return "ok";
	}
	
	@RequestMapping("/test/products")
	@ResponseBody
	public List<Product> getProductActive(){
		return testService.fetchProductsWithStatusActive();
	}
	
	@RequestMapping("/test/products/{productId}")
	@ResponseBody
	public Product getProductById(@PathVariable("productId") String productId){
		return testService.fetchProduct(productId);
	}
	
	@PostMapping("/test/notifs")
	@ResponseBody
	public String sendNotif(@RequestAttribute("authUserId") String userId, @RequestBody FcmNotifMessageDto messageDto){
				
		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs("oi oi 2", String.class).send();
		producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		return "ok-2";
	}
	
//	@PostMapping("/test/notifs/simulate")
//	@ResponseBody
//	public String simulateNotif(@RequestBody String requestBody){
//				
//		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs("oi oi 2", String.class).send();
//		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
//		logger.debug("terima simulate dengan request <{}>",requestBody);
//		return "ok-2";
//	}
	
//	@GetMapping("/test/notifs/token")
//	@ResponseBody
//	public String getGoogleToken(@RequestAttribute("authUserId") String userId){
//		RestTemplate restTemplate = new RestTemplate();				
//		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9643/test/token", String.class);
//		return response.getBody();
//	}
//	
//	@RequestMapping(value="/test/products",
//			method={ RequestMethod.GET })
//	@ResponseBody
//	public List<Product> getProducts(@RequestAttribute ("authUserId") String authUserId){
//		return testService.fetchProducts();
//	}
	
//	@RequestMapping("/test/product/bylist")
//	@ResponseBody
//	public List<Product> getproductByList(){	
//		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
//		List<String> productIds = Arrays.asList("P10");
//		List<Product> products = productService.fetchProductByProductIds(productIds);
//		if(products==null){
//			System.out.println("NULL productlist");
//		}
//		return products;
//	}
	
//	@RequestMapping(value="/test/order", method=RequestMethod.POST)
//	@ResponseBody
//	public OrderDto order(@RequestAttribute("authUserId") String authUserId, 
//			@RequestBody(required=false) SubmitOrderDto submitOrderDto) throws ApiException{	
//		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
//		return orderService.submitOrder(authUserId, submitOrderDto);
//	}
//	
	@RequestMapping(value="/test/maxpolicyenddate",  method=RequestMethod.GET)
	@ResponseBody
	public String testGetMaxPolicyEndDate(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam("userId") String userId,
			@RequestParam("policyEndDate") String policyEndDate,
			@RequestParam("coverageId") String coverageId
			) throws Exception{		
		return testService.testGetMaxPolicyEndDate(userId, policyEndDate, coverageId);		
	}
	
	@RequestMapping(value="/test/order/status",  method=RequestMethod.PUT)
	@ResponseBody
	public OrderDto testChangeOrderStatus(@RequestAttribute("authUserId") String authUserId, 
			@RequestBody(required=false) OrderDto orderDto) throws Exception{		
		return testService.changeOrderStatus(authUserId, orderDto);		
	}
	
	@RequestMapping(value="/test/spend",  method=RequestMethod.PUT)
	@ResponseBody
	public String testChangeOrderStatus(@RequestAttribute("authUserId") String authUserId, 
			@RequestBody(required=false) Map<String, Integer> spend) throws Exception{		
		testService.updatespend(authUserId, spend.get("spend"));
		return "ok";
	}
	
	@RequestMapping(value="/test/fullorder", method=RequestMethod.POST)
	@ResponseBody
	public OrderDto order(@RequestAttribute("authUserId") String authUserId, 
			@RequestBody(required=false) OrderDto orderDto) throws ApiException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return orderService.submitOrder(authUserId, orderDto, false);
	}
	
	@RequestMapping(value="/test/fullorder", method=RequestMethod.GET)
	@ResponseBody
	public List<PolicyOrder> getTestFetchOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		FilterDto orderFilter = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return orderService.fetchOrders(authUserId, orderFilter); 
	}
	
	@RequestMapping(value="/test/order", method=RequestMethod.GET)
	@ResponseBody
	public List<OrderDto> getOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		FilterDto orderFilter = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return orderService.fetchOrderDtos(authUserId, orderFilter);
	}
	
	@RequestMapping(value="/test/order/{orderId}", method=RequestMethod.GET)
	@ResponseBody
	public PolicyOrder getOrderDetail(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId) throws ApiException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		return policyOrderMapper.selectByUserIdAndOrderId(authUserId, orderId);
	}
	
//	@RequestMapping("/test/conflictorder")
//	@ResponseBody
//	public List conflictOrder(@RequestAttribute("authUserId") String authUserId, @RequestBody(required=false) OrderDto submitOrder) throws ApiException{	
//		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
//		return orderService.testConflict(authUserId, submitOrder);
//	}
	
	@RequestMapping(value="/test/user/{userId}", method=RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable("userId") String userId){
		return userMapper.selectByUserId(userId);
	}
	
	@RequestMapping(value="/test/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers (@RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId, @RequestBody UserDto usersDto) throws ApiException{
		logger.debug("Terima /users PUT untuk authuser {} and user {}", authUserId, userId);
		logger.debug("put data: {}", usersDto);
		logger.debug("---");
		UserDto result = userService.getUserDto(userId);
		if(result==null){
			result = usersDto;
		}
		return result;
	}
	
//	@RequestMapping(value="/test/claims",
//			method={ RequestMethod.POST })
//	@ResponseBody
//	public PolicyClaim<PolicyClaimDetailAccident> submitClaimAccident (@RequestAttribute ("authUserId") String authUserId,  
//			@RequestBody AccidentClaimDto claimDto){
//		logger.debug("Terima /claims POST untuk authuser {} ", authUserId);
//		logger.debug("put data: {}", claimDto);
//		logger.debug("---");
//		
//		return claimService.submitAccidentalClaim(authUserId, claimDto);
//	}
//	
//	@RequestMapping(value="/test/claims",
//			method={ RequestMethod.GET })
//	@ResponseBody
//	public List<AccidentClaimDto> getClaimAccident (@RequestAttribute ("authUserId") String authUserId,  
//			@RequestParam(value="filter",required=false) String filter){
//		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
//		logger.debug("param data: {}", filter);
//		logger.debug("---");
//		
//		FilterDto filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
//		
//		return claimService.fetchClaimDtos(authUserId, filterDto);
//	}
	
	@RequestMapping(value="/test/claims",
			method={ RequestMethod.POST})
	@ResponseBody
	public PolicyClaim<PolicyClaimDetailAccident> registerClaimAccident (@RequestAttribute ("authUserId") String authUserId,  
			@RequestBody AccidentClaimDto claimDto) throws ApiException{
		
		return claimService.registerAccidentalClaim(authUserId, claimDto,true);
	}
	
	@RequestMapping(value="/test/covcat",
			method={ RequestMethod.GET })
	@ResponseBody
	public CoverageCategory getCoverageCategory(@RequestAttribute ("authUserId") String authUserId){
		logger.debug("Terima GET untuk authuser {} ", authUserId);
		//logger.debug("param data: {}", filter);
		logger.debug("---");
		
		//FilterDto filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return testService.fetchCoverageCategoryByCoverageCategoryId("101");
	}
	
	@RequestMapping(value="/test/coverages/{coverageId}",
			method={ RequestMethod.GET })
	@ResponseBody
	public Coverage getCoverage(@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("coverageId") String coverageId){
		
		return productService.fetchCoverageByCoverageId(coverageId);
	}
	
	@RequestMapping(value="/test/coverages",
			method={ RequestMethod.GET })
	@ResponseBody
	public List<Coverage> getCoverages(@RequestAttribute ("authUserId") String authUserId){
		
		return productService.fetchCoveragesWithStatusActive();
	}

	
	
	@RequestMapping(value="/test/claims",
			method={ RequestMethod.GET })
	@ResponseBody
	public List<PolicyClaim<PolicyClaimDetailAccident>> getClaimAccident (@RequestAttribute ("authUserId") String authUserId,  
			@RequestParam(value="filter",required=false) String filter){
		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
		logger.debug("param data: {}", filter);
		logger.debug("---");
		
		FilterDto filterDto = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return claimService.fetchClaims(authUserId, filterDto);
	}
	
//	@RequestMapping(value="/claims",
//			method=RequestMethod.GET)
//	@ResponseBody
//	public List<ClaimDto> getClaims(@RequestAttribute ("authUserId") String authUserId, 
//			@RequestParam(value="filter",required=false) String filter){
//		//DateTimeFormatter formatter = new DateTimeFormatter;
//		
//		List<ClaimDto> claimDtos = new ArrayList<>();
//		ClaimDto claimDto1 = new ClaimDto();
//		claimDto1.setClaimId("cb524037-67d6-45ca-8776-3eb39cb0f5fa");
//		claimDto1.setClaimDateTime("2017-11-21T15:30:00");
//		claimDto1.setAccidentDateTime("2017-11-10T15:30:00");
//		claimDto1.setAccidentSummary("The reason lorem ipsum dollar etsum");
//		
//		List<CoverageDto> coverageDtos = new ArrayList<>();
//		List<ProductDto> productDtos = productService.fetchActiveProductDtos();
//		for(ProductDto p: productDtos){
//			if(p.getProductId().equals("P101003103")){
//				coverageDtos.add(p.getCoverage());
//			}
//			if(p.getProductId().equals("P101002103")){
//				coverageDtos.add(p.getCoverage());
//			}
//		}		
//		claimDto1.setCoverages(coverageDtos);		
//		
//		ClaimAccidentAddressDto claimAddress = new ClaimAccidentAddressDto();
//		claimAddress.setCountry("Indonesia");
//		claimAddress.setProvince("Jawa Tengah");
//		claimAddress.setCity("Semarang");
//		claimAddress.setAddress("Jln. Kertajaya no.19");		
//		claimDto1.setAccidentAddress(claimAddress);
//		
//		ClaimAccountDto account = new ClaimAccountDto();
//		account.setName("Nama pelanggan");
//		account.setBankName("BCA");
//		account.setBankSwiftCode("014");
//		account.setBankSwitt("CENAIDJA");
//		account.setAccount("6227182391006");
//		claimDto1.setClaimAccount(account);
//		
//		OrderDto orderDto = orderService.fetchOrderDtoByOrderId("e3c0e93695ef4fd2bbf65f42a45fa207", "ec9dbb13-e4fe-45bf-871b-b503ad2445b0");
//		
//		claimDto1.setOrder(orderDto);
//		
//		List<ClaimDocumentDto> docs = new ArrayList<>();
//		
//		for(ProductDto p: productDtos){
//			if(p.getProductId().equals("P101003103")){
//				int i=1;
//				for(ClaimDocTypeDto docType: p.getCoverage().getClaimDocTypes()){
//					ClaimDocumentDto claimDocumentDto1 = new ClaimDocumentDto();
//					
//					claimDocumentDto1.setClaimDocumentId("123123123"+i);
//					claimDocumentDto1.setClaimDocType(docType);
//					UserFileDto userFileDto = new UserFileDto();
//					userFileDto.setFileId(12321312L+i);
//					claimDocumentDto1.setFile(userFileDto);
//					
//					docs.add(claimDocumentDto1);
//					i++;
//				}				
//			}
//			if(p.getProductId().equals("P101002103")){
//				coverageDtos.add(p.getCoverage());
//			}
//		}
//				
//		//docs.add(claimDocumentDto);		
//		claimDto1.setClaimDocuments(docs);
//		claimDto1.setStatus("INREVIEW");
//		
//		claimDtos.add(claimDto1);				
//		
//		return claimDtos;
//	}
//	
//	@RequestMapping(value="/orders/{orderId}/claims",
//			method=RequestMethod.POST)
//	@ResponseBody
//	public ClaimDto getClaim(@RequestAttribute ("authUserId") String authUserId, 
//			@RequestBody ClaimDto claimDto,
//			@PathVariable("orderId") String orderId){
//		logger.debug("Terima /order/claim POST untuk order {} dan data {}", orderId, claimDto);
//		if(claimDto!=null){
//			claimDto.setClaimId("cb524037-67d6-45ca-8776-3eb39cb0f5fa");
//			claimDto.setStatus("SUBMITTED");
//		}
//		return claimDto;
//		
//	}
	
//	@RequestMapping(value="/claims/{claimId}",
//			method=RequestMethod.GET)
//	@ResponseBody
//	public ClaimDto getClaim(@RequestAttribute ("authUserId") String authUserId, 
//			@PathVariable("claimId") String claimId){
//		//DateTimeFormatter formatter = new DateTimeFormatter;
//						
//		ClaimDto claimDto1 = new ClaimDto();
//		claimDto1.setClaimId("cb524037-67d6-45ca-8776-3eb39cb0f5fa");
//		claimDto1.setClaimDateTime("2017-11-21T15:30:00");
//		claimDto1.setAccidentDateTime("2017-11-10T15:30:00");
//		claimDto1.setAccidentSummary("The reason lorem ipsum dollar etsum");
//		
//		List<CoverageDto> coverageDtos = new ArrayList<>();
//		List<ProductDto> productDtos = productService.fetchActiveProductDtos();
//		for(ProductDto p: productDtos){
//			if(p.getProductId().equals("P101003103")){
//				coverageDtos.add(p.getCoverage());
//			}
//			if(p.getProductId().equals("P101002103")){
//				coverageDtos.add(p.getCoverage());
//			}
//		}		
//		claimDto1.setCoverages(coverageDtos);		
//		
//		ClaimAccidentAddressDto claimAddress = new ClaimAccidentAddressDto();
//		claimAddress.setCountry("Indonesia");
//		claimAddress.setProvince("Jawa Tengah");
//		claimAddress.setCity("Semarang");
//		claimAddress.setAddress("Jln. Kertajaya no.19");		
//		claimDto1.setAccidentAddress(claimAddress);
//		
//		ClaimAccountDto account = new ClaimAccountDto();
//		account.setName("Nama pelanggan");
//		account.setBankName("BCA");
//		account.setBankSwiftCode("014");
//		account.setBankSwitt("CENAIDJA");
//		account.setAccount("6227182391006");
//		claimDto1.setClaimAccount(account);
//		
//		OrderDto orderDto = orderService.fetchOrderDtoByOrderId("e3c0e93695ef4fd2bbf65f42a45fa207", "ec9dbb13-e4fe-45bf-871b-b503ad2445b0");
//		
//		claimDto1.setOrder(orderDto);
//		
//		List<ClaimDocumentDto> docs = new ArrayList<>();
//		
//		for(ProductDto p: productDtos){
//			if(p.getProductId().equals("P101003103")){
//				int i=1;
//				for(ClaimDocTypeDto docType: p.getCoverage().getClaimDocTypes()){
//					ClaimDocumentDto claimDocumentDto1 = new ClaimDocumentDto();
//					
//					claimDocumentDto1.setClaimDocumentId("123123123"+i);
//					claimDocumentDto1.setClaimDocType(docType);
//					UserFileDto userFileDto = new UserFileDto();
//					userFileDto.setFileId(12321312L+i);
//					claimDocumentDto1.setFile(userFileDto);
//					
//					docs.add(claimDocumentDto1);
//					i++;
//				}
//				
//			}
//			if(p.getProductId().equals("P101002103")){
//				coverageDtos.add(p.getCoverage());
//			}
//		}
//				
//		//docs.add(claimDocumentDto);		
//		claimDto1.setClaimDocuments(docs);
//		claimDto1.setStatus("INREVIEW");
//
//		
//		return claimDto1;
//	}
}
