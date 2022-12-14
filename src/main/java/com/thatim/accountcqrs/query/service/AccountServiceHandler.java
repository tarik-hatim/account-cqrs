package com.thatim.accountcqrs.query.service;

import com.thatim.accountcqrs.commonapi.enums.OperationType;
import com.thatim.accountcqrs.commonapi.events.ActivatedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.CreatedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.CreditedAccountEvent;
import com.thatim.accountcqrs.commonapi.events.DebitAccountEvent;
import com.thatim.accountcqrs.commonapi.queries.GetAccountByIdQuery;
import com.thatim.accountcqrs.commonapi.queries.GetAllAccountsQuery;
import com.thatim.accountcqrs.query.entities.Account;
import com.thatim.accountcqrs.query.entities.Operation;
import com.thatim.accountcqrs.query.repositories.AccountRepository;
import com.thatim.accountcqrs.query.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AccountServiceHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    @EventHandler
    public void on(CreatedAccountEvent event) {
        log.info("**************************");
        log.info("CreatedAccountEvent received");
        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getInitialBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(ActivatedAccountEvent event) {
        log.info("***************************");
        log.info("ActivatedAccountEvent received");
        Account account = accountRepository.findById(event.getId()).get();
        account.setStatus(event.getStatus());
        accountRepository.save(account);
    }

    @EventHandler
    public void on(DebitAccountEvent event) {
        log.info("***************************");
        log.info("DebitedAccountEvent received");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setAmount(event.getAmount());
        operation.setType(OperationType.DEBIT);
        operation.setDate(event.getDate());
        account.setBalance(account.getBalance() - event.getAmount());
        operationRepository.save(operation);

    }

    @EventHandler
    public void on(CreditedAccountEvent event) {
        log.info("*****************************");
        log.info("CreditedAccountEvent received");
        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setDate(event.getDate());
        operation.setAccount(account);
        operation.setType(OperationType.CREDIT);
        operation.setAmount(event.getAmount());
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query) {
        return accountRepository.findById(query.getId()).get();
    }
}
