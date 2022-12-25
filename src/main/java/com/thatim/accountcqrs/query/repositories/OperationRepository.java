package com.thatim.accountcqrs.query.repositories;

import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaParam;
import com.thatim.accountcqrs.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
