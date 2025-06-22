package com.pismo.exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

  /*  @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            logger.error("Validation error in field '{}': {}", field, message);
            errors.put(field, message);
        });

        return ResponseEntity.badRequest().body(errors);
    }*/

    @ExceptionHandler({AccountNotFoundException.class, InappropriateAmountException.class, OperationNotFoundException.class,
            TransactionNotFoundException.class, AccountIdNotProvidedException.class})
    public ResponseEntity<Map<String, List<String>>> handleCustomException(RuntimeException ex) {
        logger.error("Error occured while processing the request - " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage())), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, List<String>>> handleGeneralExceptions(RuntimeException ex) {
        logger.error("Error occured while processing the request - " + ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(List.of(ex.getMessage())), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
