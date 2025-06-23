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
            log.info("Saving AccountEntity with Id : " + accountDto.getAccountId() + "\n" + accountDto);
            AccountEntity accountEntity = AccountMapper.dtoToEntity(accountDto);
            accountEntity = accountRepository.save(accountEntity);
            accountDto.setAccountId(accountEntity.getAccountId());
            log.info("Saved AccountEntity with Id : " + accountDto.getAccountId() + " successfully.");
            return accountDto;
        } catch (Exception e) {
            log.error("Error occurred while saving AccountEntity with Id : " + accountDto.getAccountId() + "\nError : " + e.getMessage());
            throw new AccountPersistenceException("Error occurred while saving AccountEntity with Id : " + accountDto.getAccountId());
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

        Optional<AccountEntity> accountEntity;
        try {
            log.info("Fetching AccountEntity with Id : " + accountId);
            accountEntity = accountRepository.findById(accountId);
        } catch (Exception e) {
            log.error("Error occurred while fetching AccountEntity with Id : " + accountId + "\nError : " + e.getMessage());
            throw new AccountPersistenceException("Error occurred while fetching AccountEntity with Id : " + accountId);
        }

        if (accountEntity.isPresent()) {
            log.info("Fetched AccountEntity with Id " + accountId + " successfully. \nAccountEntity : " + accountEntity.get());
            return AccountMapper.entityToDto(accountEntity.get());
        } else {
            log.error("AccountEntity with Id : " + accountId + " does not exist");
            throw new AccountNotFoundException("AccountEntity with Id : " + accountId + " does not exist");
        }
    }
}
