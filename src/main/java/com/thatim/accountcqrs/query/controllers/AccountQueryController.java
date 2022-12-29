package com.thatim.accountcqrs.query.controllers;


import com.thatim.accountcqrs.commonapi.queries.GetAllAccountsQuery;
import com.thatim.accountcqrs.commonapi.queries.GetAccountByIdQuery;
import com.thatim.accountcqrs.query.entities.Account;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/query/accounts")
@AllArgsConstructor
public class AccountQueryController {

    private QueryGateway queryGateway;

    @GetMapping("/all")
    public List<Account> list() {
        List<Account> accounts = queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();
        return accounts;
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) {
        return queryGateway.query(new GetAccountByIdQuery(id), ResponseTypes.instanceOf(Account.class)).join();
    }
}
