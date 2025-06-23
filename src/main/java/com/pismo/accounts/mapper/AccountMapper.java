package com.pismo.accounts.mapper;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.entity.AccountEntity;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AccountMapper {

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
                .build();
    }
}
