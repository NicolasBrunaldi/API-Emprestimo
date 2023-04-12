package com.minsait.apiEmprestimo.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Cliente {

    @NotBlank(message = "Este campo não pode ser nulo, nem vazio")
    private String nome;
    @Id
    @NotBlank(message = "Este campo não pode ser nulo, nem vazio")
    @CPF(message = "Insira um CPF válido")
    private String cpf;

    @NotBlank
    @Length(min = 10, max = 11, message = "O telefone deve seguir o padrão: 1199998888 ou  11988887777")
    private String telefone;

    @Embedded
    @Valid
    private Endereco endereco;

    @NotNull(message = "Este Campo não pode ser nulo")
    @Range(min = 0, message = "O valor precisa ser positivo")
    private BigDecimal rendimentoMensal;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Emprestimo> emprestimos;

    public Cliente(String nome, String cpf, String telefone, Endereco endereco, BigDecimal rendimentoMensal) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
        this.rendimentoMensal = rendimentoMensal;
    }

    public Cliente() {
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public BigDecimal getRendimentoMensal() {
        return rendimentoMensal;
    }

    public void setRendimentoMensal(BigDecimal rendimentoMensal) { this.rendimentoMensal = rendimentoMensal;}

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void adicionaEmprestimo(Emprestimo emprestimo) {
        this.emprestimos.add(emprestimo);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return cpf.equals(cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", cpf=" + cpf +
                ", endereco=" + endereco +
                ", rendimentoMensal=" + rendimentoMensal +
                '}';
    }
}
