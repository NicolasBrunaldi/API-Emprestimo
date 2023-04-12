package com.minsait.apiEmprestimo.controller;

import com.minsait.apiEmprestimo.entity.Cliente;
import com.minsait.apiEmprestimo.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @PostMapping
    @Transactional
    public ResponseEntity registraCliente(@Valid @RequestBody Cliente cliente, UriComponentsBuilder uriBuilder) {

        Cliente clienteCriado = clienteService.salvaCliente(cliente);

        URI uri = uriBuilder.path("/clientes/{cpf}").buildAndExpand(clienteCriado.getCpf()).toUri();
        return ResponseEntity.created(uri).body(clienteCriado);

    }

    @GetMapping("/{cpf}")
    public ResponseEntity retornaClienteSelecionado(@PathVariable String cpf) {

        Optional<Cliente> clienteAchado = clienteService.localizaClientePeloCpf(cpf);

        return ResponseEntity.ok(clienteAchado);
    }

    @GetMapping
    public ResponseEntity retornaListaDeClientes() {

        List<Cliente> listaDeClientes = clienteService.retornaListaDeClientes();

        if (listaDeClientes.size() <= 0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaDeClientes);
    }

    @PutMapping("/{cpf}")
    @Transactional
    public ResponseEntity alteraCliente(@PathVariable String cpf, @RequestBody Cliente cliente) {

        Cliente clienteAtualizado = clienteService.alteraDadosDoCliente(cpf, cliente);
        return ResponseEntity.ok(clienteAtualizado);

    }

    @DeleteMapping("/{cpf}")
    @Transactional
    public ResponseEntity deletaCliente(@PathVariable String cpf) {

        clienteService.deletaCliente(cpf);

        return ResponseEntity.noContent().build();
    }

}
