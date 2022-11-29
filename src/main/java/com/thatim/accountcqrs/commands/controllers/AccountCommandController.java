package com.thatim.accountcqrs.commands.controllers;


import com.thatim.accountcqrs.commonapi.commands.CreateAccountCommand;
import com.thatim.accountcqrs.commonapi.commands.CreditAccountCommand;
import com.thatim.accountcqrs.commonapi.commands.DebitAccountCommand;
import com.thatim.accountcqrs.commonapi.dtos.CreateAccountRequestDTO;
import com.thatim.accountcqrs.commonapi.dtos.CreditAccountRequestDTO;
import com.thatim.accountcqrs.commonapi.dtos.DebitAccountRequestDTO;
import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;

    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO requestDTO) {
        return commandGateway.send(new CreditAccountCommand(
                requestDTO.getId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO requestDTO) {
        return commandGateway.send(new DebitAccountCommand(
                requestDTO.getId(),
                requestDTO.getAmount(),
                requestDTO.getCurrency()
        ));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception) {
        return new ResponseEntity<>(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();

    }
}
