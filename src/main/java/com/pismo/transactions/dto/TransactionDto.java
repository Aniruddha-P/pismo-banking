package com.pismo.transactions.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDto {

    @Schema(hidden = true)
    @Null(message = "Bad request - Transaction Id cannot be pre-selected")
    private Long transactionId;

    @NotNull(message = "Account Id is mandatory")
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Account Id is mandatory")
    @Schema(type = "integer", requiredMode = Schema.RequiredMode.REQUIRED, description = "Account Id", example = "1")
    private Long accountId;

    @NotNull(message = "Operations Type Id is mandatory. Possible values 1,2,3,4")
    @Min(value = 1, message = "Operations Type Id is mandatory. Possible values 1,2,3,4")
    @Max(value = 4, message = "Operations Type Id is mandatory. Possible values 1,2,3,4")
    @Digits(integer = 1, fraction = 0, message = "Operations Type Id is mandatory. Possible values 1,2,3,4")
    @Schema(type = "integer", requiredMode = Schema.RequiredMode.REQUIRED, description = "Operation Type ID", example = "1,2,3,4")
    private int operationsTypeId;

    @NotNull(message = "Purchase/Withdrawal Amount is mandatory")
    @Digits(integer = 4, fraction = 2, message = "Purchase/Withdrawal Amount must not be 0 and must be less than 10000.00 and max upto 2 decimal points")
    @Schema(type = "BigDecimal", requiredMode = Schema.RequiredMode.REQUIRED, description = "Amount mentioned upto 2 decimal points max", example = "9999.99")
    private BigDecimal amount;
}
