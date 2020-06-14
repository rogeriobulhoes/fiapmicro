package br.com.fiap.filmes.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmeNotFoundException extends RuntimeException {

    public FilmeNotFoundException(String message) {
        super(message);
    }
}
