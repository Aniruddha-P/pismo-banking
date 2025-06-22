package com.pismo.transactions.service.impl;

import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.validations.AccountValidator;
import com.pismo.exceptions.AccountNotFoundException;
import com.pismo.exceptions.TransactionPersistenceException;
import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.entity.TransactionEntity;
import com.pismo.transactions.mapper.TransactionMapper;
import com.pismo.transactions.respository.TransactionRepository;
import com.pismo.transactions.service.TransactionService;
import com.pismo.transactions.validations.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * A transaction is created and associated with their respective account.
     * This method checks if Account mentioned in transaction exists and the OperationType is valid.
     * Only then, it creates the transaction.
     *
     * @param transactionDto Consists of accountId, operationType, amount
     * @return TransactionDto - Successfully created Transaction with transactionId
     */
    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        // Validate if appropriate operation type exist
        TransactionValidator.validateOperationType(transactionDto.getOperationsTypeId(), transactionDto.getAmount());

        // Validate if Account accountId exists
        logger.error("Validating Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction : " + transactionDto);
        AccountValidator.validateAccountIdNonNull(transactionDto.getAccountId());

        AccountEntity account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> {
                    // We can throw an Exception here, to let user know that account with accountId does not exist.
                    // OR without letting the user know that exact cause, just log it, we can return just the empty response(Security purpose)
                    logger.error("Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction : " + transactionDto + " does not exist.");
                    throw new AccountNotFoundException("Account with Id : " + transactionDto.getAccountId() + " mentioned in Transaction : " + transactionDto + " does not exist.");
                });

        try {
            // Save Transaction
            logger.error("Saving Transaction with Account Id : " + transactionDto.getAccountId() + "\n" + transactionDto);
            TransactionEntity transactionEntity = TransactionMapper.dtoToEntity(transactionDto);
            transactionEntity = transactionRepository.save(transactionEntity);
            transactionDto.setTransactionId(transactionEntity.getTransactionId());
            return transactionDto;
        } catch (Exception e) {
            logger.error("Error occurred while saving Transaction : " + transactionDto + "\nError : " + e.getMessage());
            throw new TransactionPersistenceException("Error occurred while saving Transaction : " + transactionDto);
        }
    }
}
