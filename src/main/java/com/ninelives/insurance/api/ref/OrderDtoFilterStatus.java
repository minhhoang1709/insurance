package com.ninelives.insurance.api.ref;

public enum OrderDtoFilterStatus {
	ALL(1),
	UNPAID(2),
	APPROVED(3),
	ACTIVE(4),
	EXPIRED(5)
	;
	
	private final int intValue;

    private OrderDtoFilterStatus(final int newValue) {
        intValue = newValue;
    }
    public int getValue() { return intValue; }
}
