package com.pismo.transactions.respository;

import com.pismo.transactions.entity.TransactionEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

@Hidden
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

}

