package com.pismo.transactions.validations;

import com.pismo.exceptions.InappropriateAmountException;
import com.pismo.exceptions.OperationNotFoundException;
import com.pismo.transactions.constant.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class TransactionValidator {

    private static final Logger logger = LoggerFactory.getLogger(TransactionValidator.class);

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
        logger.info("Validating Operation type with id : " + operationTypeId);
        if (OperationType.isDebitOperationType(operationTypeId)) {
            logger.info("Operation type is Debit with id : " + operationTypeId);
            // Check if amount is negative else throw exception
            if (amount.signum() != -1) {
                logger.info("For Operation type id : " + operationTypeId + " amount must be negative");
                throw new InappropriateAmountException("For Operation type id : " + operationTypeId + " amount must be negative");
            }
            // Check if totalamount - amount >= 0 else throw exception
        } else if (OperationType.isCreditOperationType(operationTypeId)) {
            logger.info("Operation type is Credit with id : " + operationTypeId);
            // Check if amount is positive else throw exception
            if (amount.signum() != 1) {
                logger.info("For Operation type id : " + operationTypeId + " amount must be positive");
                throw new InappropriateAmountException("For Operation type id : " + operationTypeId + " amount must be positive");
            }
        } else {
            logger.info("Operation type with Id : " + operationTypeId + " does not exit.");
            throw new OperationNotFoundException("Operation type with Id : " + operationTypeId + " does not exit.");
        }
    }
}
