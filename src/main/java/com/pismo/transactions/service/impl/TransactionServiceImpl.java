package com.pismo.transactions.service.impl;

import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.validations.AccountValidator;
import com.pismo.exceptions.AccountNotFoundException;
import com.pismo.exceptions.InsufficientAccountBalanceException;
import com.pismo.exceptions.TransactionPersistenceException;
import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.entity.TransactionEntity;
import com.pismo.transactions.mapper.TransactionMapper;
import com.pismo.transactions.respository.TransactionRepository;
import com.pismo.transactions.service.TransactionService;
import com.pismo.transactions.validations.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * A transaction is created and associated with their respective account.
     * This method checks if Account mentioned in transaction exists and the OperationType is valid.
     * Only then, it creates the transaction.
     *
     * @param transactionDto Consists of accountId, operationType, amount
     * @return TransactionDto - Successfully created Transaction with transactionId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TransactionDto createTransaction(TransactionDto transactionDto) {

        // Validate if appropriate amount
        TransactionValidator.validateAmountLimit(transactionDto.getAmount());

        // Validate if appropriate operation type exists
        TransactionValidator.validateOperationType(transactionDto.getOperationsTypeId(), transactionDto.getAmount());

        // Validate if Account accountId exists
        log.info("Validating Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction : " + transactionDto);
        AccountValidator.validateAccountIdNonNull(transactionDto.getAccountId());

        AccountEntity account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> {
                    log.error("Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction : " + transactionDto + " does not exist");
                    throw new AccountNotFoundException("Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction does not exist");
                });

        // Validate Account balance and Overdraft limit
        BigDecimal existingBalance = account.getBalance();
        BigDecimal newBalance = existingBalance.add(transactionDto.getAmount());
        TransactionValidator.validateOverDraftLimit(transactionDto, account);

        try {
            // Set new Account balance
            AccountEntity accountEntityWithUpdatedBalance = account.toBuilder().balance(newBalance).build();

            // Save Account
            accountRepository.save(accountEntityWithUpdatedBalance);

            // Save Transaction
            log.info("Saving TransactionEntity with Account Id : " + transactionDto.getAccountId() + "\n" + transactionDto);
            TransactionEntity transactionEntity = TransactionMapper.dtoToEntity(transactionDto);
            transactionEntity = transactionRepository.save(transactionEntity);
            transactionDto.setTransactionId(transactionEntity.getTransactionId());
            log.info("Saved TransactionEntity with Account Id : " + transactionDto.getAccountId() + " successfully");
            return transactionDto;
        } catch (Exception e) {
            log.error("Error occurred while saving TransactionEntity with Account Id : " + transactionDto.getAccountId() + "\nError : " + e.getMessage());
            throw new TransactionPersistenceException("Error occurred while saving Transaction with Account Id : " + transactionDto.getAccountId());
        }
    }
}
