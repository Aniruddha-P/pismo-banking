package com.pismo.accounts.mapper;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.entity.AccountEntity;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AccountMapper {

    //Initial Overdraft Limit set to -1000
    public static final BigDecimal INITIAL_ACCOUNT_OVER_DRAFT_LIMIT = new BigDecimal(1000).negate();
    //Initial Balance set to 0
    public static final BigDecimal INITIAL_ACCOUNT_BALANCE = BigDecimal.ZERO;

    /**
     * Mapper method to map fields of AccountDTO to AccountEntity
     *
     * @param accountDto
     * @return AccountEntity
     */
    public static AccountEntity dtoToEntity(AccountDto accountDto) {
        return AccountEntity.builder()
                .accountId(accountDto.getAccountId())
                .documentNumber(accountDto.getDocumentNumber())
                .balance(INITIAL_ACCOUNT_BALANCE)
                .overdraftLimit(INITIAL_ACCOUNT_OVER_DRAFT_LIMIT)
                .build();
    }

    /**
     * Mapper method to map fields of AccountEntity to AccountDTO
     *
     * @param accountEntity
     * @return AccountDto
     */
    public static AccountDto entityToDto(AccountEntity accountEntity) {
        return AccountDto.builder()
                .accountId(accountEntity.getAccountId())
                .documentNumber(accountEntity.getDocumentNumber())
                .balance(accountEntity.getBalance())
                .build();
    }
}
