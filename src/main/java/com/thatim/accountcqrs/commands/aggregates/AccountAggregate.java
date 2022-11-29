package com.thatim.accountcqrs.commands.aggregates;

import com.thatim.accountcqrs.commonapi.commands.CreateAccountCommand;
import com.thatim.accountcqrs.commonapi.commands.CreditAccountCommand;
import com.thatim.accountcqrs.commonapi.commands.DebitAccountCommand;
import com.thatim.accountcqrs.commonapi.enums.AccountStatus;
import com.thatim.accountcqrs.commonapi.events.ActivatedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.CreatedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.CreditedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.DebitAccountEvent;
import com.thatim.accountcqrs.exceptions.InsufficientFundsException;
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
            throw new NegativeAmountException("negative amount is not authorized.");
        AggregateLifecycle.apply(new CreatedAccountEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency()
        ));
    }

    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand) throws NegativeAmountException {
        if(creditAccountCommand.getAmount() < 0)
            throw new NegativeAmountException("negative amount is not authorized");
        AggregateLifecycle.apply(new CreditedAccountEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));
    }
    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand) {
        if(this.balance < debitAccountCommand.getAmount())
            throw new InsufficientFundsException("Insufficient funds : " + this.balance);
        AggregateLifecycle.apply(new DebitAccountEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
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

    @EventSourcingHandler
    public void on(CreditedAccountEvent event) {
        this.balance += event.getAmount();
    }

    @EventSourcingHandler
    public void on(DebitAccountEvent event) {
        this.balance -= event.getAmount();
    }
}
