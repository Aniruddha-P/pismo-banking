package com.pismo.transactions.service;

import com.pismo.transactions.dto.TransactionDto;

public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionDto);
}
