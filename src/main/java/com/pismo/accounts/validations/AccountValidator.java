package com.pismo.accounts.validations;

import com.pismo.exceptions.AccountIdNotProvidedException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class AccountValidator {

    /**
     * Validator method to validate if the account id exists.
     *
     * @param accountId AccountId
     * @throws AccountIdNotProvidedException If account id is not provided
     */
    public static void validateAccountIdNonNull(Long accountId) {
        if (Objects.isNull(accountId)) {
            log.error("Account Id missing. Provided : " + accountId);
            throw new AccountIdNotProvidedException("Account Id must be provided");
        }
    }
}
