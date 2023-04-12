package com.minsait.apiEmprestimo.service;

import com.minsait.apiEmprestimo.entity.Cliente;
import com.minsait.apiEmprestimo.entity.Emprestimo;
import com.minsait.apiEmprestimo.exceptions.ClienteNaoTemEmprestimosException;
import com.minsait.apiEmprestimo.exceptions.LimiteDeValorDeEmprestimosExcedidoException;
import com.minsait.apiEmprestimo.repository.ClienteRepository;
import com.minsait.apiEmprestimo.repository.EmprestimoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimosService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public Emprestimo salvaEmprestimoDoCliente(String cpf, Emprestimo emprestimo) {

        Cliente cliente = clienteRepository.getReferenceById(cpf);

        if (!validaSeJaUtrapassouLimiteDeEmprestimos(cliente.getRendimentoMensal(), cpf)) {

            Emprestimo emprestimoCriado = criaEmprestimo(cliente, emprestimo);
            cliente.adicionaEmprestimo(emprestimoCriado);
            emprestimoCriado.setValorFinal();
            return emprestimo;

        }
        throw new LimiteDeValorDeEmprestimosExcedidoException("Seu empréstimo está ultrapassando o valor disponibilizado baseado na sua renda mensal.");
    }

    public List<Emprestimo> listaEmprestimosDoCliente(String cpf) {

        List<Emprestimo> listaEmprestimos = emprestimoRepository.findAllByCpfCliente(cpf);
        if (listaEmprestimos.size() <= 0) {
            throw new ClienteNaoTemEmprestimosException("Este cliente não tem empréstimos no momento");
        }
        return listaEmprestimos;
    }

    public boolean isCpfCadastrado(String cpf) {
        return clienteRepository.existsById(cpf);
    }

    public Emprestimo encontraEmprestimoPeloId(String cpf, Long id) {

        Cliente clienteEncontrado = clienteRepository.getReferenceById(cpf);
        Emprestimo emprestimoEncontrado = clienteEncontrado.getEmprestimos().stream().filter(emprestimo -> emprestimo.getId() == id).findFirst().orElse(null);
        if (emprestimoEncontrado == null){

        throw new EntityNotFoundException();
        }
        return emprestimoEncontrado;

    }


    public void deletaEmprestimoPeloId(String cpf,Long id) {

        Cliente clienteEncontrado = clienteRepository.getReferenceById(cpf);
        Emprestimo emprestimoEncontrado = clienteEncontrado.getEmprestimos().stream().filter(emprestimo -> emprestimo.getId() == id).findFirst().orElse(null);
        if (emprestimoEncontrado == null){

            throw new EntityNotFoundException();
        }
        clienteEncontrado.getEmprestimos().remove(emprestimoEncontrado);
    }

    private boolean validaSeJaUtrapassouLimiteDeEmprestimos(BigDecimal rendimentoMensal, String cpf) {

        List<Emprestimo> emprestimos = emprestimoRepository.findAllByCpfCliente(cpf);
        BigDecimal totalEmprestimos = emprestimos.stream().map(emprestimo -> emprestimo.getValorInicial()).reduce(emprestimos.size() >0 ? emprestimos.get(0).getValorInicial() : BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorLimite = rendimentoMensal.multiply(BigDecimal.valueOf(10));


        if (totalEmprestimos.compareTo(valorLimite) > 0) {
            return true;
        }
        return false;
    }

    private Emprestimo criaEmprestimo(Cliente cliente, Emprestimo emprestimo){

        Emprestimo emprestimoCriado = new Emprestimo(
                emprestimo.getValorInicial(),
                emprestimo.getDataInicial(),
                emprestimo.getDataFinal(),
                emprestimo.getRelacionamento(),
                cliente);
        return emprestimoCriado;
    }
}
