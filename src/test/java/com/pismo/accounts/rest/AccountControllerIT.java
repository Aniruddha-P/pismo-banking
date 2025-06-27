package com.pismo.accounts.rest;

import com.pismo.PismoBankingApplication;
import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.service.AccountService;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Collections;

@SpringBootTest(classes = PismoBankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIT {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Test
    public void testCreateAccount_Ok() {
        String expectedResponse = "{\"accountId\":1,\"documentNumber\":\"123456\"}";

        AccountDto accountDto = AccountDto.builder().documentNumber("123456").build();

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());

    }

    @Test
    public void testCreateAccount_DocumentNumber_Not_Provided() {
        String documentNumberMissingResponse = "{\"documentNumber\":\"Document number is mandatory\"}";
        String emptyDocumentNumber = "";

        AccountDto accountDto = AccountDto.builder().documentNumber(emptyDocumentNumber).build();

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(documentNumberMissingResponse, response.getBody());

    }

    @Test
    public void testCreateAccount_AccountId_Already_Provided() {
        String documentNumberMissingResponse = "{\"accountId\":\"Account Id cannot be pre-selected\"}";
        Long alreadyProvidedAccoutId = 123456L;

        AccountDto accountDto = AccountDto.builder().accountId(alreadyProvidedAccoutId).documentNumber("123456").build();

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(documentNumberMissingResponse, response.getBody());


    }

    @Test
    public void testRetrieveAccount_Ok() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts/1"),
                HttpMethod.GET, entity, String.class);

        String expectedResponse = "{\"accountId\":1,\"documentNumber\":\"123456\"}";

        Assert.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
    }

    @Test
    public void testRetrieveAccount_AccountId_Not_Found() throws JSONException {
        Long missingAccountId = Long.MAX_VALUE;
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts/{accountId}"),
                HttpMethod.GET, entity, String.class, Collections.singletonMap("accountId", missingAccountId));

        String expectedResponse = "{\"errors\":[\"Account with Id : 9223372036854775807 does not exist\"]}";

        Assert.assertEquals(HttpStatusCode.valueOf(422), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(expectedResponse, response.getBody());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}