package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.B2bReportDto;
import com.ninelives.insurance.apigateway.dto.B2bReportListDto;
import com.ninelives.insurance.apigateway.dto.FormObjectDto;
import com.ninelives.insurance.apigateway.dto.FormObjectListDto;
import com.ninelives.insurance.apigateway.dto.FreeInsuranceReportDto;
import com.ninelives.insurance.apigateway.dto.FreeInsuranceReportListDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportBenefitPeriodDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportBenefitPeriodListDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportDatePeriodDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportDatePeriodListDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportInsuranceTypeDto;
import com.ninelives.insurance.apigateway.dto.TransactionReportInsuranceTypeListDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;

@Controller
@RequestMapping("/api-gateway")
public class CmsReportController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(CmsReportController.class);
	
	@Autowired 
	ApiCmsService apiB2BService;
	
	
	@RequestMapping(value="/getB2bReportList", method=RequestMethod.GET)	
	@ResponseBody
	public B2bReportListDto getB2BReportList(){
		
		B2bReportListDto b2bReportListDto = new B2bReportListDto();
		List<B2bReportDto> listB2bReport = apiB2BService.getListB2BReport();
		
		b2bReportListDto.setiTotalRecords(listB2bReport.size());
		b2bReportListDto.setiTotalDisplayRecords(11);
		b2bReportListDto.setsEcho(0);
		b2bReportListDto.setsColumns("");
		b2bReportListDto.setAaData(listB2bReport);
		
		logger.info("listB2bReport : "+ listB2bReport.toString());
		
		return b2bReportListDto;
	}
	
	
	@RequestMapping(value="/getB2BReportListByDate", method=RequestMethod.GET)	
	@ResponseBody
	public B2bReportListDto getB2BReportListByDate(
			@RequestParam String startDate,
			@RequestParam String endDate
			){		
		
		B2bReportListDto b2bReportListDto = new B2bReportListDto();
		List<B2bReportDto> listB2bReport = apiB2BService.getListB2BReportByDate(startDate, endDate);
		
		b2bReportListDto.setiTotalRecords(listB2bReport.size());
		b2bReportListDto.setiTotalDisplayRecords(11);
		b2bReportListDto.setsEcho(0);
		b2bReportListDto.setsColumns("");
		b2bReportListDto.setAaData(listB2bReport);
		
		logger.info("listB2bReport : "+ listB2bReport.toString());
		
		return b2bReportListDto;
	}
	
	
	@RequestMapping(value="/getTransactionListByInsuranceType", method=RequestMethod.GET)	
	@ResponseBody
	public TransactionReportInsuranceTypeListDto getTransactionListByInsuranceType(){
		
		TransactionReportInsuranceTypeListDto transactionReportInsuranceTypeListDto = new TransactionReportInsuranceTypeListDto();
		List<TransactionReportInsuranceTypeDto> listTrxByInsuranceType = apiB2BService.getListTransactionReportInsuranceType();
		
		transactionReportInsuranceTypeListDto.setiTotalRecords(listTrxByInsuranceType.size());
		transactionReportInsuranceTypeListDto.setiTotalDisplayRecords(11);
		transactionReportInsuranceTypeListDto.setsEcho(0);
		transactionReportInsuranceTypeListDto.setsColumns("");
		transactionReportInsuranceTypeListDto.setAaData(listTrxByInsuranceType);
		
		logger.info("listTrxByInsuranceType : "+ listTrxByInsuranceType.toString());
		
		return transactionReportInsuranceTypeListDto;
	}
	
	
	@RequestMapping(value="/getTransactionListByInsuranceTypeAndDate", method=RequestMethod.GET)	
	@ResponseBody
	public TransactionReportInsuranceTypeListDto getTransactionListByInsuranceTypeAnDate(
			@RequestParam String startDate,
			@RequestParam String endDate
			){		
		
		TransactionReportInsuranceTypeListDto transactionReportInsuranceTypeListDto = new TransactionReportInsuranceTypeListDto();
		List<TransactionReportInsuranceTypeDto> listTrxByInsuranceType = apiB2BService.getListTransactionReportInsuranceTypeByDate(startDate, endDate);
		
		transactionReportInsuranceTypeListDto.setiTotalRecords(listTrxByInsuranceType.size());
		transactionReportInsuranceTypeListDto.setiTotalDisplayRecords(11);
		transactionReportInsuranceTypeListDto.setsEcho(0);
		transactionReportInsuranceTypeListDto.setsColumns("");
		transactionReportInsuranceTypeListDto.setAaData(listTrxByInsuranceType);
		
		logger.info("listTrxByInsuranceType : "+ listTrxByInsuranceType.toString());
		
		return transactionReportInsuranceTypeListDto;
	}
	
	
	@RequestMapping(value="/getTransactionListByDatePeriod", method=RequestMethod.GET)	
	@ResponseBody
	public TransactionReportDatePeriodListDto getTransactionListByDatePeriod(){
		
		TransactionReportDatePeriodListDto transactionReportDatePeriodListDto = new TransactionReportDatePeriodListDto();
		List<TransactionReportDatePeriodDto> listTrxByDatePeriod = apiB2BService.getListTransactionReportDatePeriod();
		
		transactionReportDatePeriodListDto.setiTotalRecords(listTrxByDatePeriod.size());
		transactionReportDatePeriodListDto.setiTotalDisplayRecords(11);
		transactionReportDatePeriodListDto.setsEcho(0);
		transactionReportDatePeriodListDto.setsColumns("");
		transactionReportDatePeriodListDto.setAaData(listTrxByDatePeriod);
		
		logger.info("listTrxByDatePeriod : "+ listTrxByDatePeriod.toString());
		
		return transactionReportDatePeriodListDto;
	}
	
	@RequestMapping(value="/getTransactionListDatePeriodByDate", method=RequestMethod.GET)	
	@ResponseBody
	public TransactionReportDatePeriodListDto getTransactionListDatePeriodByDate(
			@RequestParam String startDate,
			@RequestParam String endDate
			){
		
		TransactionReportDatePeriodListDto transactionReportDatePeriodListDto = new TransactionReportDatePeriodListDto();
		List<TransactionReportDatePeriodDto> listTrxByDatePeriod = 
				apiB2BService.getListTransactionReportDatePeriodByDate(startDate, endDate);
		
		transactionReportDatePeriodListDto.setiTotalRecords(listTrxByDatePeriod.size());
		transactionReportDatePeriodListDto.setiTotalDisplayRecords(11);
		transactionReportDatePeriodListDto.setsEcho(0);
		transactionReportDatePeriodListDto.setsColumns("");
		transactionReportDatePeriodListDto.setAaData(listTrxByDatePeriod);
		
		logger.info("listTrxByDatePeriod : "+ listTrxByDatePeriod.toString());
		
		return transactionReportDatePeriodListDto;
	}
	
	
	@RequestMapping(value="/getInviterInviteeReport", method=RequestMethod.GET)	
	@ResponseBody
	public FreeInsuranceReportListDto getInviterInviteeReportList(){
		
		FreeInsuranceReportListDto listDto = new FreeInsuranceReportListDto();
		
		String voucherType="INVITE";
		List<FreeInsuranceReportDto> listFreeInsuranceReport = 
				apiB2BService.getListFreeInsuranceReportByVoucherType(voucherType);
		
		listDto.setiTotalRecords(listFreeInsuranceReport.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listFreeInsuranceReport);
		
		logger.info("listFreeInsuranceReport : "+ listFreeInsuranceReport.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/getFreePromoReport", method=RequestMethod.GET)	
	@ResponseBody
	public FreeInsuranceReportListDto getFreePromoReportList(){
		
		FreeInsuranceReportListDto listDto = new FreeInsuranceReportListDto();
		
		String voucherType="FREE_PROMO_NEW_USER";
		List<FreeInsuranceReportDto> listFreeInsuranceReport = 
				apiB2BService.getListFreeInsuranceReportByVoucherType(voucherType);
		
		listDto.setiTotalRecords(listFreeInsuranceReport.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listFreeInsuranceReport);
		
		logger.info("listFreeInsuranceReport : "+ listFreeInsuranceReport.toString());
		
		return listDto;
	}
	
	
	@RequestMapping(value="/getTransactionListByBenefitPeriod", method=RequestMethod.GET)	
	@ResponseBody
	public TransactionReportBenefitPeriodListDto getTransactionListByBenefitPeriod(){
		
		TransactionReportBenefitPeriodListDto listDto = new TransactionReportBenefitPeriodListDto();
		List<TransactionReportBenefitPeriodDto> listTrxByBenefitPeriod = 
				apiB2BService.getListTransactionReportBenefitPeriod();
		
		listDto.setiTotalRecords(listTrxByBenefitPeriod.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listTrxByBenefitPeriod);
		
		logger.info("listTrxByBenefitPeriod : "+ listTrxByBenefitPeriod.toString());
		
		return listDto;
	}
	
	
	@RequestMapping(value="/listCoverage", method=RequestMethod.GET)	
	@ResponseBody
	public FormObjectListDto getListCoverage(){
		
		FormObjectListDto listDto = new FormObjectListDto();
		List<FormObjectDto> listCoverage = apiB2BService.getListCoverage();
		listDto.setListObj(listCoverage);
		logger.info("listCoverage : "+ listCoverage.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/listCoverageByInsuranceType", method=RequestMethod.GET)	
	@ResponseBody
	public FormObjectListDto getListCoverageByInsurancetype(@RequestParam String insuranceTypeId){
		
		FormObjectListDto listDto = new FormObjectListDto();
		List<FormObjectDto> listCoverage = apiB2BService.getListCoverageByInsuranceType(insuranceTypeId);
		listDto.setListObj(listCoverage);
		logger.info("listCoverage : "+ listCoverage.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/listPeriod", method=RequestMethod.GET)	
	@ResponseBody
	public FormObjectListDto getListPeriod(){
		
		FormObjectListDto listDto = new FormObjectListDto();
		List<FormObjectDto> listPeriod = apiB2BService.getListPeriod();
		listDto.setListObj(listPeriod);
		logger.info("listPeriod : "+ listPeriod.toString());
		
		return listDto;
	}
	
	@RequestMapping(value="/listInsuranceType", method=RequestMethod.GET)	
	@ResponseBody
	public FormObjectListDto getInsuranceType(){
		
		FormObjectListDto listDto = new FormObjectListDto();
		List<FormObjectDto> listInsuranceType = apiB2BService.getListInsuranceType();
		listDto.setListObj(listInsuranceType);
		logger.info("listInsuranceType : "+ listInsuranceType.toString());
		
		return listDto;
	}
	
}
