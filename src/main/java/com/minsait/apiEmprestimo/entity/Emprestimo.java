package com.minsait.apiEmprestimo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minsait.apiEmprestimo.enums.Relacionamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpf_Cliente",insertable = false, updatable = false)
    private String cpfCliente;

    @NotNull
    private BigDecimal valorInicial;

    private BigDecimal valorFinal;

    @NotNull
    @PastOrPresent(message = "Insira a data de hoje ou passada")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataInicial;

    @NotNull
    @Future(message = "Somente datas acima de hoje")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataFinal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Relacionamento relacionamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_Cliente", referencedColumnName = "cpf")
    private Cliente cliente;

    public Emprestimo(BigDecimal valorInicial,LocalDate dataInicial, LocalDate dataFinal, Relacionamento relacionamento, Cliente cliente) {
        this.valorInicial = valorInicial;
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.relacionamento = relacionamento;
        this.cliente = cliente;
    }

    public Emprestimo(){}

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public BigDecimal getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Relacionamento getRelacionamento() {
        return relacionamento;
    }

    public void setRelacionamento(Relacionamento relacionamento) {
        this.relacionamento = relacionamento;
    }

    public BigDecimal getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(){
        this.valorFinal = this.relacionamento.calculoDoRelacionamento(valorInicial, new BigDecimal(cliente.getEmprestimos().size()));
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataInicial() {
        return this.dataInicial;
    }
}
