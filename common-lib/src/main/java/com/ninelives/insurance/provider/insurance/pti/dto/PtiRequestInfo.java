package com.ninelives.insurance.provider.insurance.pti.dto;

import com.ninelives.insurance.provider.insurance.pti.ref.PtiGender;
import com.ninelives.insurance.ref.PolicyStatus;

public class PtiRequestInfo {
	private String refId;
	private String name;
	private String personId;
	private String phone;
	private String mail;
	private String fromTime;
	private String toTime;
	private int value;
	private int bonus;
	private int fee;
	private String ngaySinh;
	private String viaPartner;
	private String maHopDong;
	private String ngayTao;
	private PtiGender gioiTinh;
	private String goiSamPham;
	private PolicyStatus trangThaiHopDong;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(String ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getViaPartner() {
		return viaPartner;
	}

	public void setViaPartner(String viaPartner) {
		this.viaPartner = viaPartner;
	}

	public String getMaHopDong() {
		return maHopDong;
	}

	public void setMaHopDong(String maHopDong) {
		this.maHopDong = maHopDong;
	}

	public String getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(String ngayTao) {
		this.ngayTao = ngayTao;
	}

	public PtiGender getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(PtiGender gioiTinh) {
		this.gioiTinh = gioiTinh;
	}

	public String getGoiSamPham() {
		return goiSamPham;
	}

	public void setGoiSamPham(String goiSamPham) {
		this.goiSamPham = goiSamPham;
	}

	public PolicyStatus getTrangThaiHopDong() {
		return trangThaiHopDong;
	}

	public void setTrangThaiHopDong(PolicyStatus trangThaiHopDong) {
		this.trangThaiHopDong = trangThaiHopDong;
	}

}
