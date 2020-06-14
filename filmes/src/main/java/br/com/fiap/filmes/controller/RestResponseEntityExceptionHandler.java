package br.com.fiap.filmes.controller;


import br.com.fiap.filmes.repository.ErrorDetails;
import br.com.fiap.filmes.service.FilmeNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(FilmeNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleBotNotFoundException(FilmeNotFoundException ex,
                                                                         WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<ErrorDetails> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            ErrorDetails errorDetails =
                    new ErrorDetails(new Date(),
                            violation.getMessage(),
                            request.getDescription(false));
            errors.add(errorDetails);
        }

        return new ResponseEntity<Object>(
                errors, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }
}
