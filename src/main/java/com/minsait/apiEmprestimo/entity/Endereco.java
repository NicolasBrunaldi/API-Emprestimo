package com.minsait.apiEmprestimo.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;

@Embeddable
public class Endereco {

    @NotBlank(message = "Este campo não pode ser nulo, nem vazio")
    private String rua;

    @NotNull(message = "Este campo não pode ser nulo")
    @Min(value = 1, message = "O número mínimo é 1")
    @Max(value = 999999, message = "Ultrapassou o limite, coloque um número menor")
    private Integer numero;

    @NotBlank(message = "Este campo não pode ser nulo, nem vazio")
    private String cep;

    public Endereco(String rua, Integer numero, String cep) {
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
    }

    public Endereco() {
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
}
