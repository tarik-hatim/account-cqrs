package com.thatim.accountcqrs.commonapi.events;

import lombok.Getter;

import java.util.Date;

public class CreditedAccountEvent extends BaseEvent<String>{
    @Getter private double amount;
    @Getter private String currency;
    @Getter private Date date;
    public CreditedAccountEvent(String id, double amount, String currency, Date date) {
        super(id);
        this.amount = amount;
        this.currency = currency;
        this.date = date;
    }
}
