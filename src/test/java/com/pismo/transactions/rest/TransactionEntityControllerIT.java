package com.pismo.transactions.rest;


import com.pismo.PismoBankingApplication;
import com.pismo.accounts.entity.AccountEntity;
import com.pismo.accounts.respository.AccountRepository;
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

        TransactionDto transactionDto = TransactionDto.builder().accountId(1L).amount(BigDecimal.ONE).operationsTypeId(4).build();

        HttpEntity<TransactionDto> entity = new HttpEntity<>(transactionDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/transactions"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
