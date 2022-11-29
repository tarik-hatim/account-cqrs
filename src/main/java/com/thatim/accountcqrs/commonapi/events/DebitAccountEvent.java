package com.thatim.accountcqrs.commonapi.events;

import lombok.Getter;

public class DebitAccountEvent extends BaseEvent<String>{
    @Getter
    private double amount;
    @Getter
    private String currency;
    public DebitAccountEvent(String id, double amount, String currency) {
        super(id);
        this.amount = amount;
        this.currency = currency;
    }
}
