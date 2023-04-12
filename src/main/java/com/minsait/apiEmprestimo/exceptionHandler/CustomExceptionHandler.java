package com.minsait.apiEmprestimo.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.minsait.apiEmprestimo.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity handleCpfJaCadastradoException(CpfJaCadastradoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(FaltouPreencherEnderecoExcepiton.class)
    public ResponseEntity handleFaltouPreencherEnderecoException(FaltouPreencherEnderecoExcepiton ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity handleDateTimeParseException() {

        return ResponseEntity.badRequest().body("Padrão de data errado, insira o padrão: dd/MM/yyyy");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException() {

        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleErrosDeValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).collect(Collectors.toList()));

    }

    @ExceptionHandler(CpfNaoEstaCadastradoException.class)
    public ResponseEntity handleCpfNaoEstaCadastradoException(CpfNaoEstaCadastradoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ClienteNaoTemEmprestimosException.class)
    public ResponseEntity handleClienteNaoTemEmprestimosException(ClienteNaoTemEmprestimosException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(LimiteDeValorDeEmprestimosExcedidoException.class)
    public ResponseEntity handleLimiteDeValorDeEmprestimosExcedidoException(LimiteDeValorDeEmprestimosExcedidoException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    private class DadosErroValidacao {
        @JsonProperty
        private String campo;
        @JsonProperty
        private String message;

        public DadosErroValidacao(FieldError errors) {
            this.campo = errors.getField();
            this.message = errors.getDefaultMessage();
        }
    }
}