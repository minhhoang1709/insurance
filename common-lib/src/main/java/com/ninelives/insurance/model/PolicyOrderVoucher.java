package com.ninelives.insurance.model;

import java.io.Serializable;

public class PolicyOrderVoucher implements Serializable{
    private static final long serialVersionUID = 838880159192804470L;

	private Long id;

    private String orderId;

//    private Integer voucherId;
//
//    private String code;
//
//    private String inviterUserId;
    
    private Voucher voucher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

	public Voucher getVoucher() {
		return voucher;
	}

	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

//    public Integer getVoucherId() {
//        return voucherId;
//    }
//
//    public void setVoucherId(Integer voucherId) {
//        this.voucherId = voucherId;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getInviterUserId() {
//        return inviterUserId;
//    }
//
//    public void setInviterUserId(String inviterUserId) {
//        this.inviterUserId = inviterUserId;
//    }
}