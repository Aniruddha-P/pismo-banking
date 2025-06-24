package com.pismo.transactions.rest;


import com.pismo.PismoBankingApplication;
import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.transactions.constant.OperationType;
import com.pismo.transactions.dto.TransactionDto;
import com.pismo.transactions.respository.TransactionRepository;
import com.pismo.transactions.service.TransactionService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;

@SpringBootTest(classes = PismoBankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionEntityControllerIT {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void addTransaction() {
        String expectedResponse = "{\"transactionId\":1,\"accountId\":1,\"operationsTypeId\":4,\"amount\":1}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(OperationType.CREDIT_VOUCHER.getId()).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addTransaction_AccountId_Not_Found() {
        String expectedResponse = "{\"errors\":[\"Account with Id : 9223372036854775807 mentioned in Transaction : TransactionDto(transactionId=null, accountId=9223372036854775807, operationsTypeId=4, amount=1) does not exist\"]}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        Long missingAccountId = Long.MAX_VALUE;
        TransactionDto transactionDto = TransactionDto.builder().accountId(missingAccountId).amount(BigDecimal.ONE).operationsTypeId(OperationType.CREDIT_VOUCHER.getId()).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(422), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addTransaction_Amount_Missing() {
        String expectedResponse = "{\"amount\":\"Purchase/Withdrawal Amount is mandatory\"}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        BigDecimal missingAmount = null;
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(missingAmount).operationsTypeId(OperationType.CREDIT_VOUCHER.getId()).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addTransaction_Invalid_OperationType() {
        String expectedResponse = "{\"operationsTypeId\":\"Operations Type Id is mandatory. Possible values 1,2,3,4\"}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        Integer invalidOperationType = Integer.MAX_VALUE;
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(invalidOperationType).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addTransaction_Invalid_Amount_For_Valid_OperationType() {
        String expectedResponse = "{\"errors\":[\"For OperationType Id : 3 amount must be negative\"]}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        BigDecimal positiveAmountForWithdrawalOperationType = BigDecimal.ONE;
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(positiveAmountForWithdrawalOperationType).operationsTypeId(OperationType.WITHDRAWAL.getId()).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(422), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void addTransaction_Amount_Greater_Than_10000() {
        String expectedResponse = "{\"amount\":\"Purchase/Withdrawal Amount must not be 0 and must be less than 10000.00 and max upto 2 decimal points\"}";
        //Creating transaction related accountEntity
        AccountEntity accountEntity = AccountEntity.builder().documentNumber("123456").build();
        this.accountRepository.save(accountEntity);

        BigDecimal greaterThan10000Amount = BigDecimal.valueOf(10001);
        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(greaterThan10000Amount).operationsTypeId(OperationType.CREDIT_VOUCHER.getId()).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
