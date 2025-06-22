package com.pismo.transactions.mapper;

import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.entity.TransactionEntity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TransactionMapper {
    /**
     * Mapper method to map fields of TransactionDTO to TransactionEntity
     *
     * @param transactionDto
     * @return TransactionEntity
     */
    public static TransactionEntity dtoToEntity(TransactionDto transactionDto) {
        return TransactionEntity.builder()
                .accountId(transactionDto.getAccountId())
                .operationsTypeId(transactionDto.getOperationsTypeId())
                .amount(transactionDto.getAmount())
                .eventDate(LocalDateTime.now())
                .build();
    }
}
