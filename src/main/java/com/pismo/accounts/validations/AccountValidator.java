package com.pismo.accounts.validations;

import com.pismo.exceptions.AccountIdNotProvidedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class AccountValidator {
    private static final Logger logger = LoggerFactory.getLogger(AccountValidator.class);

    /**
     * Validator method to validate if the account id exists.
     *
     * @param accountId AccountId
     * @throws AccountIdNotProvidedException If account id is not provided
     */
    public static void validateAccountIdNonNull(Long accountId) {
        if (Objects.isNull(accountId)) {
            logger.error("Account Id missing. Provided : " + accountId);
            throw new AccountIdNotProvidedException("Account Id must be provided");
        }
    }
}
