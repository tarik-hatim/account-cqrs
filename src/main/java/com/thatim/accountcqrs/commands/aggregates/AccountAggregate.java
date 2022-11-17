package com.thatim.accountcqrs.commands.aggregates;

import com.thatim.accountcqrs.commonapi.commands.CreateAccountCommand;
import com.thatim.accountcqrs.commonapi.enums.AccountStatus;
import com.thatim.accountcqrs.commonapi.events.ActivatedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.CreatedAccountEvent;
import com.thatim.accountcqrs.exceptions.NegativeAmountException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus accountStatus;

    public AccountAggregate() {
        //Required By AXON Framework
    }
    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) throws NegativeAmountException {
        if (createAccountCommand.getInitialBalance() < 0)
            throw new NegativeAmountException("a negative amount is not authorized.");
        AggregateLifecycle.apply(new CreatedAccountEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(CreatedAccountEvent event) {
        this.accountId = event.getId();
        this.currency = event.getCurrency();
        this.accountStatus = AccountStatus.CREATED;
        this.balance = event.getInitialBalance();
        AggregateLifecycle.apply(new ActivatedAccountEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));
    }

    @EventSourcingHandler
    public void on(ActivatedAccountEvent event) {
        this.accountStatus = event.getStatus();
    }

}
