package com.pismo.transactions.validations;

import com.pismo.exceptions.InappropriateAmountException;
import com.pismo.exceptions.OperationNotFoundException;
import com.pismo.transactions.constant.OperationType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class TransactionValidator {

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
                throw new InappropriateAmountException("For OperationType Id : " + operationTypeId + " amount must be negative");
            }
            // Check if totalamount - amount >= 0 else throw exception
        } else if (OperationType.isCreditOperationType(operationTypeId)) {
            log.info("OperationType is Credit with Id : " + operationTypeId);
            // Check if amount is positive else throw exception
            if (amount.signum() != 1) {
                log.error("For OperationType Id : " + operationTypeId + " amount must be positive");
                throw new InappropriateAmountException("For OperationType Id : " + operationTypeId + " amount must be positive");
            }
        } else {
            log.error("OperationYype with Id : " + operationTypeId + " does not exist");
            throw new OperationNotFoundException("OperationType with Id : " + operationTypeId + " does not exist");
        }
    }
}
