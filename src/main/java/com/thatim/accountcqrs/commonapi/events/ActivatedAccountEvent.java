package com.thatim.accountcqrs.commonapi.events;

import com.thatim.accountcqrs.commonapi.enums.AccountStatus;
import lombok.Getter;

public class ActivatedAccountEvent extends BaseEvent<String>{
    @Getter private AccountStatus status;
    public ActivatedAccountEvent(String id, AccountStatus status) {
        super(id);

        this.status = status;
    }
}
