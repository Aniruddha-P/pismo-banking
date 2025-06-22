package com.pismo.accounts.rest;

import com.pismo.PismoBankingApplication;
import com.pismo.accounts.dto.AccountDto;
import com.pismo.accounts.respository.AccountRepository;
import com.pismo.accounts.service.AccountService;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(classes = PismoBankingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountEntityControllerIT {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @Test
    @Order(value = 1)
    public void testCreateAccount() {
        String expectedResponse = "{\"accountId\":1,\"documentNumber\":\"123456\"}";

        AccountDto accountDto = AccountDto.builder().documentNumber("123456").build();

        HttpEntity<AccountDto> entity = new HttpEntity<>(accountDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(expectedResponse, response.getBody());
        Assert.assertNotNull(response.getBody());

    }

    @Test
    @Order(value = 2)
    public void testRetrieveAccount() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/accounts/1"),
                HttpMethod.GET, entity, String.class);

        String expectedResponse = "{\"accountId\":1,\"documentNumber\":\"123456\"}";

        Assert.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        JSONAssert.assertEquals(expectedResponse, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}