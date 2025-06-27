package com.pismo.transactions.validations;

import com.pismo.accounts.entity.AccountEntity;
import com.pismo.exceptions.InappropriateAmountException;
import com.pismo.exceptions.InsufficientAccountBalanceException;
import com.pismo.exceptions.OperationNotFoundException;
import com.pismo.exceptions.OverdraftLimitReachedException;
import com.pismo.transactions.constant.OperationType;
import com.pismo.transactions.dto.TransactionDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public class TransactionValidator {

    private static final BigDecimal TRANSACTION_AMOUNT_LIMIT = BigDecimal.valueOf(10000.00);

    /**
     * Validator method to validate if the Operation Type and Amount provided by the customer are aligned.
     *
     * @param operationTypeId
     * @param amount
     * @throws InappropriateAmountException If the OperationTypeId is for Normal Purchase, Purchase with installment or Debit and the Amount is Positive
     *                                      and if the OperationTypeId Credit and the Amount is negative
     * @throws OperationNotFoundException   If the OperationTypeId is not valid
     */
    public static void validateOperationType(int operationTypeId, BigDecimal amount) {
        log.info("Validating OperationType with Id : " + operationTypeId);
        if (OperationType.isDebitOperationType(operationTypeId)) {
            log.info("OperationType is Debit with Id : " + operationTypeId);
            // Check if amount is negative else throw exception
            if (amount.signum() != -1) {
                log.error("For OperationType Id : " + operationTypeId + ", amount must be negative");
                throw new InappropriateAmountException("For OperationType Id : " + operationTypeId + ", amount must be negative");
            }
            // Check if totalamount - amount >= 0 else throw exception
        } else if (OperationType.isCreditOperationType(operationTypeId)) {
            log.info("OperationType is Credit with Id : " + operationTypeId);
            // Check if amount is positive else throw exception
            if (amount.signum() != 1) {
                log.error("For OperationType Id : " + operationTypeId + ", amount must be positive");
                throw new InappropriateAmountException("For OperationType Id : " + operationTypeId + ", amount must be positive");
            }
        } else {
            log.error("OperationType with Id : " + operationTypeId + " does not exist");
            throw new OperationNotFoundException("OperationType with Id : " + operationTypeId + " does not exist");
        }
    }

    /**
     * Validator method to validate if amount is within limit
     *
     * @param amount
     * @throws InappropriateAmountException If amount is off the limits
     */
    public static void validateAmountLimit(BigDecimal amount) {
        log.info("Validating amount for transaction : " + amount);
        if (BigDecimal.ZERO.compareTo(amount) == 0 ||
                TRANSACTION_AMOUNT_LIMIT.compareTo(amount) <= 0 ||
                TRANSACTION_AMOUNT_LIMIT.negate().compareTo(amount) >= 0) {
            log.error("Amount for transaction off the limit : " + amount);
            throw new InappropriateAmountException("Amount must not be 0 and must be less than 10000.00 and max upto 2 decimal points");
        }
        log.info("Amount for transaction within limit : " + amount);
    }

    public static void validateOverDraftLimit(TransactionDto transactionDto, AccountEntity account) {
        BigDecimal existingBalance = account.getBalance();
        BigDecimal newBalance = existingBalance.add(transactionDto.getAmount());
        log.info("Existing balance : {} \nPost-transaction New balance : {}", existingBalance, newBalance);

        //If account-level overdraft limit is set, validate post-transaction new balance does not breach that
        //Else validate sufficient funds available
        if(isAccountOverdraftLimitAvailable(account) && isAccountOverdraftLimitBreached(account, newBalance)) {
            log.error("Overdraft limit {} for account with id {} breached", account.getOverdraftLimit(), account.getAccountId());
            throw new OverdraftLimitReachedException("Overdraft limit " + account.getOverdraftLimit() + " breached for account with id " + account.getAccountId());
        } else if (!isAccountOverdraftLimitAvailable(account) && isInsufficientBalance(newBalance)) {
            log.error("Insufficient Balance for account with Id : " + account.getAccountId());
            throw new InsufficientAccountBalanceException("Insufficient Balance for account with Id : " + account.getAccountId());
        }
    }

    private static boolean isInsufficientBalance(BigDecimal newBalance) {
        return BigDecimal.ZERO.compareTo(newBalance) > 0;
    }

    private static boolean isAccountOverdraftLimitBreached(AccountEntity account, BigDecimal newBalance) {
        return account.getOverdraftLimit().compareTo(newBalance) > 0 ? true : false;
    }

    private static boolean isAccountOverdraftLimitAvailable(AccountEntity account) {
        return Objects.nonNull(account.getOverdraftLimit()) && BigDecimal.ZERO.compareTo(account.getOverdraftLimit()) != 0;
    }
}
