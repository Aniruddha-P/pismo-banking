package com.pismo.transactions.mapper;

import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.entity.TransactionEntity;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TransactionMapper {

    private static final String EVENT_DATETIME_UTC_STANDARD_ZONE = "UTC";

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
                .eventDate(ZonedDateTime.now(ZoneId.of(EVENT_DATETIME_UTC_STANDARD_ZONE)))
                .build();
    }
}
