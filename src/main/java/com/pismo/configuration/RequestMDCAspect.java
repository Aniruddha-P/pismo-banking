package com.pismo.configuration;

import com.pismo.accounts.dto.AccountDto;
import com.pismo.transactions.dto.TransactionDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Aspect
@Component
public class RequestMDCAspect {

    @Pointcut("execution(* com.pismo..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object injectMDC(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder requestContext = new StringBuilder();

        //For getAccount and getTransaction with only 1 arg - Lond accountId/transactionId
        if(joinPoint.getArgs().length==1 && joinPoint.getArgs()[0] instanceof Long id) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            if ("accountId".equals(paramNames[0])) {
                requestContext.append("accountId='").append(id).append("' ");
            } else if ("transactionId".equals(paramNames[0])) {
                requestContext.append("transactionId='").append(id).append("' ");
            }
        } else {
            //For createAccount/createTransaction
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
                if (arg instanceof AccountDto accountDto && Objects.nonNull(accountDto.getDocumentNumber())) {
                    requestContext.append("documentNumber='").append(accountDto.getDocumentNumber().toString()).append("' ");
                }
                if (arg instanceof TransactionDto transactionDto && Objects.nonNull(transactionDto.getAccountId())) {
                    requestContext.append("accountId='").append(transactionDto.getAccountId().toString()).append("' ");
                }
            });
        }

        if(Objects.nonNull(MDC.getCopyOfContextMap())) {
            //Always have the requestId populated in RequestMDCFilter as first value in requestContext
            requestContext.insert(0, MDC.get("requestContext"));
            MDC.put("requestContext", requestContext.toString());
        }
        return joinPoint.proceed();
    }
}

