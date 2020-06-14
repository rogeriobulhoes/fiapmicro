package br.com.fiap.servicos.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChamadoNotFoundException extends RuntimeException {
    public ChamadoNotFoundException(String message) {
        super(message);
    }
}
