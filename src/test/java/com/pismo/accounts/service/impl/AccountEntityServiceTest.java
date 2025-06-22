package com.pismo.accounts.service.impl;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.service.AccountService;
import com.pismo.exceptions.AccountNotFoundException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureTestDatabase
class AccountEntityServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    AccountRepository accountRepository;

    @Test
    void createAccount_Ok() {
        AccountDto accountDto = AccountDto.builder().documentNumber("12345").build();
        AccountEntity savedAccountEntity = AccountEntity.builder().accountId(1L).documentNumber("12345").build();

        Mockito.when(accountRepository.save(Mockito.any(AccountEntity.class))).thenReturn(savedAccountEntity);

        AccountDto savedAccountDto = accountService.createAccount(accountDto);

        Assert.assertEquals(Long.valueOf(1), savedAccountDto.getAccountId());
    }

    @Test
    void testGetAccount_Ok() {
        AccountEntity accountEntity = AccountEntity.builder().accountId(1L).documentNumber("12345").build();

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.ofNullable(accountEntity));

        AccountDto accountDto = accountService.getAccount(1L);

        Assert.assertNotNull(accountDto);
    }

    @Test
    void testGetAccount_AccountNotFound() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Assert.assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(1L));
    }
}