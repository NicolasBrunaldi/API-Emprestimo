package com.minsait.apiEmprestimo.controller;

import com.minsait.apiEmprestimo.entity.Emprestimo;
import com.minsait.apiEmprestimo.exceptions.CpfNaoEstaCadastradoException;
import com.minsait.apiEmprestimo.service.EmprestimosService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes/{cpf}/emprestimos")
public class EmprestimosController {

    @Autowired
    private EmprestimosService emprestimosService;


    @PostMapping
    @Transactional
    public ResponseEntity cadastraEmprestimoParaCliente(@Valid @RequestBody Emprestimo emprestimo,@PathVariable String cpf, UriComponentsBuilder uriBuilder) {

        isCpfCadastrado(cpf);

        Emprestimo emprestimoCriado = emprestimosService.salvaEmprestimoDoCliente(cpf, emprestimo);

        URI uri = uriBuilder.path("/clientes/{cpf}/emprestimos/{id}").buildAndExpand(cpf, emprestimoCriado.getId()).toUri();
        return ResponseEntity.created(uri).body(emprestimoCriado);

    }

    @GetMapping
    public ResponseEntity listaDehEmprestimosDoCliente(@PathVariable String cpf) {

        isCpfCadastrado(cpf);

        List<Emprestimo> emprestimos = emprestimosService.listaEmprestimosDoCliente(cpf);

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/{id}")
    public ResponseEntity retornaEmprestimoSelecionado(@PathVariable Long id, @PathVariable String cpf){

        isCpfCadastrado(cpf);


        Emprestimo emprestimoEncontrado = emprestimosService.encontraEmprestimoPeloId(cpf, id);

        return ResponseEntity.ok(emprestimoEncontrado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletaEmprestimoSelecionado(@PathVariable Long id, @PathVariable String cpf){

        isCpfCadastrado(cpf);

        emprestimosService.deletaEmprestimoPeloId(cpf, id);

        return ResponseEntity.noContent().build();
    }


    private void isCpfCadastrado(@PathVariable String cpf) {

        if (!emprestimosService.isCpfCadastrado(cpf)) {
            throw new CpfNaoEstaCadastradoException("O CPF passado não está cadastrado no sistema");
        }
    }
}
