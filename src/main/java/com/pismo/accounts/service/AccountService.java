package com.pismo.accounts.service;

import com.pismo.accounts.dto.AccountDto;

public interface AccountService {
    AccountDto createAccount(AccountDto account);

    AccountDto getAccount(Long accoundId);
}
