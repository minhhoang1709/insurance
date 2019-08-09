package com.ninelives.insurance.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.PolicyOrderProduct;
import com.ninelives.insurance.provider.insurance.pti.dto.PtiRequestInfo;
import com.ninelives.insurance.provider.insurance.pti.ref.PtiGender;
import com.ninelives.insurance.ref.Gender;

@Component
public class PtiObjectMapper {

	DateTimeFormatter ptiPolicyDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	DateTimeFormatter ptiPolicyDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public PtiRequestInfo toDto(PolicyOrder order, String personFileId, String policyNumber) {
		PtiRequestInfo dto = null;
		if (order != null) {
			dto = new PtiRequestInfo();
			dto.setRefId(order.getOrderId());
			dto.setName(order.getPolicyOrderUsers().getName());
			dto.setPersonId(personFileId);
			dto.setPhone(order.getPolicyOrderUsers().getPhone());
			dto.setMail(order.getPolicyOrderUsers().getEmail());
			dto.setFromTime(order.getPolicyStartDate().format(ptiPolicyDateFormatter) + " 00:00:00");
			dto.setToTime(order.getPolicyEndDate().format(ptiPolicyDateFormatter) + " 23:59:59");
			dto.setValue(order.getTotalPremi());
			dto.setBonus(0);
			dto.setFee(0);
			dto.setNgaySinh(order.getPolicyOrderUsers().getBirthDate().format(ptiPolicyDateFormatter));
			dto.setViaPartner("");
			dto.setMaHopDong(policyNumber);
			dto.setNgayTao(order.getOrderDate().format(ptiPolicyDateFormatter));

			if (order.getPolicyOrderUsers().getGender().equals(Gender.MALE)) {
				dto.setGioiTinh(PtiGender.MALE);
			} else {
				dto.setGioiTinh(PtiGender.FEMALE);
			}
			String goiSanPham = "";
			int i = 0;
			for (PolicyOrderProduct item : order.getPolicyOrderProducts()) {
				goiSanPham += item.getCoverageId();
				i++;
				if (i < order.getPolicyOrderProducts().size()) {
					goiSanPham += ", ";
				}
			}
			dto.setGoiSamPham(goiSanPham);
			dto.setTrangThaiHopDong(order.getStatus());

		}

		return dto;
	}
}
