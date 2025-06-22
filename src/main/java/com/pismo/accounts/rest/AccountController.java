package com.pismo.accounts.rest;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.service.AccountService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountService accountService;

    @Operation(
            summary = " Add a new Account",
            description = " Creates account with Document number.")
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
                                    @ExampleObject("Document Number is mandatory, " +
                                            "Account Id cannot be pre-selected"))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples =
                                    @ExampleObject(
                                            "An error occurred processing your request. Please try again.")))
            })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid AccountDto accountDto) {
        logger.info("Received request to create account for : " + accountDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(accountDto));
    }

    @Operation(
            summary = " Find Account by Id",
            description = " Retrieve accounts details for Account Id.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples =
                                    @ExampleObject("Invalid format"))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class),
                                    examples =
                                    @ExampleObject(
                                            "Account with the provided Id does not exist"))),
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
    @GetMapping(value = "/{accountId}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        logger.info("Received request to fetch details for account with id : " + accountId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.getAccount(accountId));
    }
}
