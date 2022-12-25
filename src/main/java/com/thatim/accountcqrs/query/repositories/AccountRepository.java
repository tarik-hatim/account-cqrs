package com.thatim.accountcqrs.query.repositories;

import com.thatim.accountcqrs.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
