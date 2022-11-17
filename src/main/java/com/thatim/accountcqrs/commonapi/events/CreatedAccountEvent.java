package com.thatim.accountcqrs.commonapi.events;


import lombok.Getter;

public class CreatedAccountEvent extends BaseEvent<String> {

    @Getter private double initialBalance;
    @Getter private String currency;

    public CreatedAccountEvent(String id, double initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
