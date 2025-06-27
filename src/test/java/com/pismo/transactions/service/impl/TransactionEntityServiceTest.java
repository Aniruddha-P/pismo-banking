package com.pismo.transactions.service.impl;

import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.exceptions.AccountNotFoundException;
import com.pismo.exceptions.InappropriateAmountException;
import com.pismo.exceptions.OperationNotFoundException;
import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.entity.TransactionEntity;
import com.pismo.transactions.respository.TransactionRepository;
import com.pismo.transactions.service.TransactionService;
import com.pismo.transactions.validations.TransactionValidator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
public class TransactionEntityServiceTest {
    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private TransactionValidator transactionValidator;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void testCreateTransaction_Ok() {
        AccountEntity accountEntity = AccountEntity.builder().accountId(1L).documentNumber("12345").balance(BigDecimal.ZERO).build();
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(4).build();
        TransactionEntity transactionEntity = TransactionEntity.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(4).build();
        TransactionEntity savedtransaction = TransactionEntity.builder().transactionId(1L).accountId(1L).amount(BigDecimal.ONE).operationsTypeId(4).build();

        Mockito.when(accountRepository.findById(transactionDto.getAccountId())).thenReturn(Optional.of(accountEntity));
        Mockito.when(transactionRepository.save(Mockito.any(TransactionEntity.class))).thenReturn(savedtransaction);

        TransactionDto savedTransactionDto = transactionService.createTransaction(transactionDto);

        Assert.assertEquals(Long.valueOf(1), savedTransactionDto.getTransactionId());
    }

    @Test
    void testCreateTransaction_AccountNotFound() {
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(4).build();

        Mockito.when(accountRepository.findById(transactionDto.getAccountId())).thenReturn(Optional.empty());

        Assert.assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    void testCreateTransaction_InvalidOperationTypeInvalidAmount() {
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(2).build();

        Assert.assertThrows(InappropriateAmountException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    void testCreateTransaction_InvalidOperationTypeOperationNotFound() {
        int invalidOperationType = 5;
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(invalidOperationType).build();

        Assert.assertThrows(OperationNotFoundException.class, () -> transactionService.createTransaction(transactionDto));
    }
}

