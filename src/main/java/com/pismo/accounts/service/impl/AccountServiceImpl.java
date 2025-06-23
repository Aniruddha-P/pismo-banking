package com.pismo.accounts.service.impl;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.mapper.AccountMapper;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.service.AccountService;
import com.pismo.accounts.validations.AccountValidator;
import com.pismo.exceptions.AccountNotFoundException;
import com.pismo.exceptions.AccountPersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates an Account for the provided information - Document Number
     *
     * @param accountDto Consists of Document Number
     * @return AccountDto - Successfully created Account with accountId
     */
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        try {
            AccountEntity accountEntity = AccountMapper.dtoToEntity(accountDto);
            accountEntity = accountRepository.save(accountEntity);
            accountDto.setAccountId(accountEntity.getAccountId());
            log.error("Account saved successfully in DB. \nAccount : " + accountEntity);
            return accountDto;
        } catch (Exception e) {
            log.error("Error occured while saving Account : " + accountDto + "\nError : " + e.getMessage());
            throw new AccountPersistenceException("Error occured while saving Account : " + accountDto);
        }
    }

    /**
     * Return Account details for the provided accountId
     *
     * @param accountId Long value which is associated with the Account
     * @return AccountDto - Details for Account with accountId
     */
    @Override
    public AccountDto getAccount(Long accountId) {
        //Validate accountId
        AccountValidator.validateAccountIdNonNull(accountId);

        Optional<AccountEntity> account;
        try {
            account = accountRepository.findById(accountId);
        } catch (Exception e) {
            log.error("Error occured while fetching Account with Id : " + accountId + "\nError : " + e.getMessage());
            throw new AccountPersistenceException("Error occured while fetching Account with Id : " + accountId);
        }

        if (account.isPresent()) {
            log.error("Account with Id " + accountId + " fetched successfully. \nAccount : " + account.get());
            return AccountMapper.entityToDto(account.get());
        } else {
            log.error("AccountEntity with ID : " + accountId + " does not exist.");
            throw new AccountNotFoundException("AccountEntity with ID : " + accountId + " does not exist");
        }
    }
}
