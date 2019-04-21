package com.ninelives.insurance.api.dev;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.ninelives.insurance.api.dto.AccidentClaimDto;
import com.ninelives.insurance.api.dto.ClaimCoverageDto;
import com.ninelives.insurance.api.dto.ClaimDocumentDto;
import com.ninelives.insurance.api.dto.CoverageDto;
import com.ninelives.insurance.api.dto.FilterDto;
import com.ninelives.insurance.api.dto.OrderDto;
import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.provider.account.GoogleAccountProvider;
import com.ninelives.insurance.api.service.ApiClaimService;
import com.ninelives.insurance.api.service.ApiNotificationService;
import com.ninelives.insurance.api.service.ApiOrderService;
import com.ninelives.insurance.api.service.TestService;
import com.ninelives.insurance.api.service.ApiUserService;
import com.ninelives.insurance.api.service.ApiVoucherService;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.mybatis.mapper.PolicyClaimDocumentMapper;
import com.ninelives.insurance.core.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.core.mybatis.mapper.ProductMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserMapper;
import com.ninelives.insurance.core.provider.insurance.AswataInsuranceProvider;
import com.ninelives.insurance.core.provider.insurance.InsuranceProviderException;
import com.ninelives.insurance.core.provider.insurance.OrderConfirmResult;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.core.service.ClaimService;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.core.service.InsuranceService;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.core.service.ProductService;
import com.ninelives.insurance.core.service.VoucherService;
import com.ninelives.insurance.core.support.pdf.PdfCreator;
import com.ninelives.insurance.core.util.GsonUtil;
import com.ninelives.insurance.model.Coverage;
import com.ninelives.insurance.model.CoverageCategory;
import com.ninelives.insurance.model.PolicyClaim;
import com.ninelives.insurance.model.PolicyClaimDetailAccident;
import com.ninelives.insurance.model.PolicyClaimDocument;
import com.ninelives.insurance.model.PolicyClaimDocumentExtra;
import com.ninelives.insurance.model.PolicyClaimFamily;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.Product;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.model.Voucher;
import com.ninelives.insurance.provider.insurance.aswata.dto.OrderConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FamilyRelationship;
import com.ninelives.insurance.route.EndPointRef;

@Controller
@Profile("dev")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired ProductService productService;
	@Autowired ApiOrderService apiOrderService;
	@Autowired ApiClaimService apiClaimService;
	@Autowired TestService testService;
	@Autowired ApiVoucherService apiVoucherService;
	@Autowired ApiNotificationService apiNotificationService;
	@Autowired NotificationService notificationService;
	@Autowired VoucherService voucherService;
	@Autowired InsuranceService insuranceService;
	@Autowired OrderService orderService;
	@Autowired ClaimService claimService;
	@Autowired StorageProvider storageService;
	@Autowired FileUploadService fileService;
	
	@Autowired AswataInsuranceProvider aswata;
	@Autowired GoogleAccountProvider google;
	
	@Autowired ProductMapper productMapper;
	@Autowired PolicyOrderMapper policyOrderMapper;
	
	@Autowired ApiUserService apiUserService;
	@Autowired UserMapper userMapper;
	
	@Autowired PolicyClaimDocumentMapper docMapper;
	
	@Autowired FluentProducerTemplate producerTemplate;
	
	@Value("${ninelives.order.list-limmit:50}")
	int defaultFilterLimit;
	
	@Value("${ninelives.order.list-offset:0}")
	int defaultFilterOffset;
	
	
	
	@RequestMapping(value="/test/orders/{orderId}/policy",
			method=RequestMethod.GET)	
	@ResponseBody
	public ResponseEntity<Resource> downloadPolicy(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId,
			HttpServletResponse response) throws AppException, StorageException{
		
		logger.debug("GET download policy test, userId <{}>, orderId: <{}>", authUserId, orderId);
		
		Resource file = storageService.loadAsResource("POLICY/2019/pdfTest-3.pdf");
		
//		Resource file = null;
		
//		URI uri = URI.create("file:/POLICY/2019/pdfTest-3.pdf");
//		System.out.println("Uri is "+uri.toString());
//		System.out.println(String.format("Path is %s and scheme is %s", uri.getPath(), uri.getScheme()));
//		
//		if("file".equals(uri.getScheme())){
//			file = storageService.loadAsResource(uri.getPath());			
//		}
//	    
//		
		try {
			System.out.println("File is "+file.getURI().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(file);
				
	}
	
//	@GetMapping("/test/claimdoccheck")
//	@ResponseBody
//	public List<ClaimDocumentDto> testClaimDocCheck() throws AppBadRequestException{
//		PolicyOrder order = orderService.fetchOrderByOrderId("1a122c4d03a047f191438a34d283ac78", "5df28d70-d553-4618-8dd2-d2a7107ec805");
//		
//		AccidentClaimDto dto = new AccidentClaimDto();
//		dto.setClaimCoverages(new ArrayList<ClaimCoverageDto>());
//		dto.getClaimCoverages().add(new ClaimCoverageDto());
//		dto.getClaimCoverages().get(0).setCoverage(new CoverageDto());
//		dto.getClaimCoverages().get(0).getCoverage().setCoverageId("103003");
//		return apiClaimService.requiredClaimDocumentDtos(dto, order);
//	}
	
	@RequestMapping(value="/test/fullcoveragecategory/{coverageCategoryId}",
			method={ RequestMethod.GET })
	@ResponseBody
	public CoverageCategory getCoverageCategory (@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("coverageCategoryId") String id){
		return productService.fetchCoverageCategoryByCoverageCategoryId(id);
	}
	
	@RequestMapping(value="/test/fullclaim/{claimId}",
			method={ RequestMethod.GET })
	@ResponseBody
	public PolicyClaim<PolicyClaimDetailAccident> getClaimAccidentByClaimId (@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("claimId") String claimId) throws AppNotFoundException{
//		logger.debug("Terima /claims GET untuk authuser {} ", authUserId);
//		logger.debug("param data: {}", claimId);
//		logger.debug("---");		
		
		PolicyClaim<PolicyClaimDetailAccident> claimDto = claimService.fetchClaimByClaimId(authUserId, claimId);
		if(claimDto==null){
			throw new AppNotFoundException(ErrorCode.ERR7001_CLAIM_NOT_FOUND,"Klaim tidak ditemukan");
		}
		return claimDto;
	}
	@RequestMapping("/test/claimdoc")
	@ResponseBody
	public String testInsertClaimDoc(){
		List<PolicyClaimDocument> docs = new ArrayList<>();
		docs.add(new PolicyClaimDocument());
		docs.get(0).setClaimId("test-002");
		docs.get(0).setClaimDocTypeId("DT004");
		docs.get(0).setFileId(12321314L);
		//docs.get(0).setExtra("{\"familySubId\": 2, \"familyName\": \"Anak ketiga\"}");
		docs.get(0).setExtra(new PolicyClaimDocumentExtra());
		docs.get(0).getExtra().setFamily(new PolicyClaimFamily());
		docs.get(0).getExtra().getFamily().setSubId(1);
		docs.get(0).getExtra().getFamily().setName("Test nama family");
		docs.get(0).getExtra().getFamily().setRelationship(FamilyRelationship.ANAK);
		docs.get(0).getExtra().getFamily().setBirthDate(LocalDate.now());
		
		docMapper.insertList(docs);
		
		return "test insert";
	}
	
	@PostMapping("/test/aswata/orderconfirm")
	@ResponseBody
	public String testOrderConfirm(@RequestAttribute ("authUserId") String authUserId, @RequestParam("providerOrderNum") String orderNum ) throws Exception{
		
		logger.debug("dapat ordernum <{}>", orderNum);
		
		PolicyOrder po = new PolicyOrder();
		po.setUserId(authUserId);
		po.setProviderOrderNumber(orderNum);
		OrderConfirmResult result = aswata.orderConfirm(po);
		
//		return orderNum;
		return result.toString();
	}
	
	@PostMapping("/test/insurance/orderconfirm")
	@ResponseBody
	public String testOrderConfirmInsurance(@RequestAttribute ("authUserId") String authUserId, @RequestParam("providerOrderNum") String orderNum ) throws Exception{
		
		logger.debug("dapat ordernum <{}>", orderNum);
		
		PolicyOrder po = new PolicyOrder();
		po.setUserId(authUserId);
		po.setProviderOrderNumber(orderNum);
		
		insuranceService.orderConfirm(po);
		
//		return orderNum;
		return po.toString();
	}
	
	@PostMapping("/test/order/orderconfirm")
	@ResponseBody
	public String testOrderConfirmOrder(@RequestAttribute("authUserId") String authUserId,
			@RequestParam("providerOrderNum") String orderNum,
			@RequestParam("orderId") String orderId) throws Exception {
		
		logger.debug("dapat ordernum <{}>", orderNum);
		
		PolicyOrder po = new PolicyOrder();
		po.setOrderId(orderId);
		po.setUserId(authUserId);
		po.setProviderOrderNumber(orderNum);
		
		orderService.orderConfirm(po);
		
		return po.toString();
	}
	
	@PostMapping("/test/google")
	@ResponseBody
	public String testGoogleVerification(@RequestBody RegistrationDto registrationDto) throws Exception{
		String result = google.verifyEmail(registrationDto);
		return result;
	}
	
	@GetMapping("/test/aswata/order")
	@ResponseBody
	public String testAswataOrder(@RequestAttribute ("authUserId") String authUserId) throws AppNotFoundException, IOException, StorageException{
		try {
			aswata.orderPolicy(null);
		} catch (InsuranceProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ok";
	}
	

	
	@RequestMapping(value="/test/full/vouchers/{code}",
			method={ RequestMethod.GET })
	@ResponseBody
	public Voucher getVoucher(@RequestAttribute ("authUserId") String authUserId,
			@PathVariable("code") String code) throws AppNotFoundException{
		return voucherService.fetchVoucherByCode(code);
	}
	
	@RequestMapping("/test/error/generic")
	public String errorGeneric() throws Exception{
		
		throw new Exception("olalala");		
		//return "ok";
	}

	@RequestMapping("/test/error/login")
	public String errorLogin() throws AppNotFoundException{
		
		throw new AppNotFoundException(ErrorCode.ERR2001_LOGIN_FAILURE,"olalala login gagal");		
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
	
	@RequestMapping("/test/fullcoverages/{coverageId}")
	@ResponseBody
	public Coverage getCoverageById(@PathVariable("coverageId") String coverageId){
		System.out.print("fullcoverages");
		return testService.fetchCoverage(coverageId);
	}
	
	@PostMapping("/test/notifs")
	@ResponseBody
	public String sendNotif(@RequestAttribute("authUserId") String userId, @RequestBody FcmNotifMessageDto messageDto) throws Exception{
				
		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs("oi oi 2", String.class).send();
		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		notificationService.sendFcmNotification(userId, messageDto, EndPointRef.QUEUE_FCM_NOTIFICATION);
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
			@RequestBody(required=false) OrderDto orderDto) throws AppException{	
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		return apiOrderService.submitOrder(authUserId, orderDto, false);
	}
	
	@RequestMapping(value="/test/fullorder", method=RequestMethod.GET)
	@ResponseBody
	public List<PolicyOrder> getTestFetchOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws AppException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		FilterDto orderFilter = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return apiOrderService.fetchOrders(authUserId, orderFilter); 
	}
	
	@RequestMapping(value="/test/order", method=RequestMethod.GET)
	@ResponseBody
	public List<OrderDto> getOrder(@RequestAttribute("authUserId") String authUserId, 
			@RequestParam(value="filter",required=false) String filter) throws AppException{
		//List<String> productIds = Arrays.asList("P101004102","P101003102","P101006102");
		
		//return orderService.submitOrder(authUserId, submitOrder);
		//policyOrderMapper.selectByUserId(authUserId, 100, 0);
		//orderFilter = new OrderFilterDto();
		//orderFilter.setLimit(limit);
		
		FilterDto orderFilter = GsonUtil.gson.fromJson(filter, FilterDto.class);
		
		return apiOrderService.fetchOrderDtos(authUserId, orderFilter);
	}
	
	@RequestMapping(value="/test/order/{orderId}", method=RequestMethod.GET)
	@ResponseBody
	public PolicyOrder getOrderDetail(@RequestAttribute("authUserId") String authUserId,
			@PathVariable("orderId") String orderId) throws AppException{
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
	
	@RequestMapping(value="/test/userfilemap/{fileId}", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, UserFile> getUserFileMap(@PathVariable("fileId") Long fileId){
		UserFile userfile = fileService.fetchUserFileById(fileId);
		
		Map<String, UserFile> resultMap = new HashMap<>();
		resultMap.put("file", userfile);
		
		return resultMap; 
	}
	
	@RequestMapping(value="/test/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers (@RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId, @RequestBody UserDto usersDto) throws AppException{
		logger.debug("Terima /users PUT untuk authuser {} and user {}", authUserId, userId);
		logger.debug("put data: {}", usersDto);
		logger.debug("---");
		UserDto result = apiUserService.getUserDto(userId);
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
			@RequestBody AccidentClaimDto claimDto) throws AppException{
		
		return apiClaimService.registerAccidentalClaim(authUserId, claimDto,true);
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
		
		return apiClaimService.fetchClaims(authUserId, filterDto);
	}
	
	@RequestMapping(value="/test/temp/pdfprint",
			method={ RequestMethod.GET })
	@ResponseBody
	public String printpdf(){
		
		PdfCreator pdfCreator = new PdfCreator("D:\\local\\sts\\9lives\\template\\online_policy_certificate_dav04.pdf",
				"D:\\local\\sts\\9lives\\template\\arial.ttf");
		
		Map<String, String> fieldMap = new HashMap<>();
		fieldMap.put("buyer_name","Smith");
		fieldMap.put("buyer_contact_no","12345678");
		fieldMap.put("policy_number","PA0000021/9Lives/2019");
		fieldMap.put("date_of_issue","26/03/2019");
		fieldMap.put("insured_name","Smith");
		fieldMap.put("insured_idcard","123124214124");
		fieldMap.put("insured_birthdate","26/03/2019");
		fieldMap.put("insured_buyer_relation","");
		fieldMap.put("policy_start_date","26/03/2019");
		fieldMap.put("policy_end_date","26/03/2020");

		fieldMap.put("coverage_1","Tử vong do tai nạn");
		fieldMap.put("coverage_2","Thương tật do tai nạn");
		fieldMap.put("coverage_3","Nằm viện trên 3 ngày do tai nạn (tối đa 2 lần/năm)");
		fieldMap.put("coverage_4","Chi phí mai táng khi tử vong do tai nạn");
		fieldMap.put("coverage_5","Trợ cấp thu nhập do tử vong hoặc thương tật vĩnh viễn do tai nạn");
		fieldMap.put("coverage_6","Điều trị tai nạn tại phòng cấp cứu (tối đa 2 lần/năm)");
			
		fieldMap.put("limit_1","40.000.000");
		fieldMap.put("limit_2","40.000.000");
		fieldMap.put("limit_3","3.000.000");
		fieldMap.put("limit_4","4.000.000");
		fieldMap.put("limit_5","5.000.000");
		fieldMap.put("limit_6","1.000.000");

		fieldMap.put("premi","12.345");
		fieldMap.put("period","1 năm");
		
		try {
			pdfCreator.printFieldMap(fieldMap, "D:\\local\\sts\\9lives\\test\\pdfTest.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "ok";
	}
	
}
