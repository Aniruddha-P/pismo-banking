package com.pismo.transactions.rest;

import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = " Creates a new transaction and associates it with respective Account",
            description = "Creates a new transaction with amount & Operation Type and associates it with respective Account")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Long.class))
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples =
                                    @ExampleObject("Account Id is mandatory, " +
                                            "Transaction Id cannot be pre-selected, " +
                                            "Operations Type ID is mandatory. Possible values 1,2,3,4., " +
                                            "Purchase/Withdrawal Amount should be less than 10000.00 and max upto 2 decimal points"))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error \n",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples =
                                    @ExampleObject(
                                            "An error occurred processing your request. Please try again.")))
            })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        logger.info("Received request to create transaction for : " + transactionDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionService.createTransaction(transactionDto));

    }
}
