package com.minsait.apiEmprestimo.exceptions;

public class CpfNaoEstaCadastradoException extends RuntimeException {
    public CpfNaoEstaCadastradoException(String message) {
        super(message);
    }
}
