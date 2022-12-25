package com.thatim.accountcqrs.commonapi.events;


import com.thatim.accountcqrs.commonapi.enums.AccountStatus;
import lombok.Getter;

public class CreatedAccountEvent extends BaseEvent<String> {

    @Getter private double initialBalance;
    @Getter private String currency;
    @Getter private AccountStatus status;

    public CreatedAccountEvent(String id, double initialBalance, String currency, AccountStatus status) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
        this.status = status;
    }
}
