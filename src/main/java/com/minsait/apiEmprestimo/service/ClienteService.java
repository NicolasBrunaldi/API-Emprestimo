package com.minsait.apiEmprestimo.service;

import com.minsait.apiEmprestimo.exceptions.CpfJaCadastradoException;
import com.minsait.apiEmprestimo.exceptions.CpfNaoEstaCadastradoException;
import com.minsait.apiEmprestimo.exceptions.FaltouPreencherEnderecoExcepiton;
import com.minsait.apiEmprestimo.repository.ClienteRepository;
import com.minsait.apiEmprestimo.entity.Endereco;
import com.minsait.apiEmprestimo.entity.Cliente;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public Optional<Cliente> localizaClientePeloCpf(String cpf) {

        Optional<Cliente> clienteEncontrado = clienteRepository.findById(cpf);
        if (!clienteEncontrado.isPresent()) {
            throw new CpfNaoEstaCadastradoException("Este CPF não está cadastrado");
        }
        return clienteEncontrado;
    }

    public Cliente salvaCliente(Cliente cliente) {

        if (!clienteRepository.existsById(cliente.getCpf())) {
            Cliente clienteCriado = criaCliente(cliente);
            return clienteRepository.save(clienteCriado);

        }
        throw new CpfJaCadastradoException("CPF já cadastrado");
    }

    public List<Cliente> retornaListaDeClientes() {

        return clienteRepository.findAll();
    }

    public void deletaCliente(String cpf) {

        Optional<Cliente> clienteOptional = localizaClientePeloCpf(cpf);
        Cliente cliente = clienteOptional.orElse(null);

        clienteRepository.delete(cliente);
    }

    public Cliente alteraDadosDoCliente(String cpf, Cliente cliente) {


        Cliente clienteEncontradoNoBD = clienteRepository.getReferenceById(cpf);
        if (clienteEncontradoNoBD != null) {
            Cliente clienteAtualizado = validaEhAtualizaDadosDoCliente(cliente, clienteEncontradoNoBD);

            return clienteAtualizado;
        }

        throw new EntityNotFoundException();
    }

    private Cliente validaEhAtualizaDadosDoCliente(Cliente cliente, Cliente clienteEncontradoNoBD) {


        clienteEncontradoNoBD.setNome(cliente.getNome() != null && !cliente.getNome().isBlank()
                ? cliente.getNome()
                : clienteEncontradoNoBD.getNome());

        clienteEncontradoNoBD.setTelefone(cliente.getTelefone() != null && !cliente.getTelefone().isBlank()
                ? cliente.getTelefone()
                : clienteEncontradoNoBD.getTelefone());

        BigDecimal rendimentoMensalAtualizado = cliente.getRendimentoMensal() != null && !cliente.getRendimentoMensal().toString().isBlank()
                ? new BigDecimal(String.valueOf(cliente.getRendimentoMensal()))
                : clienteEncontradoNoBD.getRendimentoMensal();
        clienteEncontradoNoBD.setRendimentoMensal(rendimentoMensalAtualizado);


//  ------------------------ENDEREÇO -----------------------------------------------------

        if (cliente.getEndereco() != null) {
            clienteEncontradoNoBD.getEndereco().setCep(cliente.getEndereco().getCep() != null && !cliente.getEndereco().getCep().isBlank()
                    ? cliente.getEndereco().getCep()
                    : clienteEncontradoNoBD.getEndereco().getCep());

            clienteEncontradoNoBD.getEndereco().setRua(cliente.getEndereco().getRua() != null && !cliente.getEndereco().getRua().isBlank()
                    ? cliente.getEndereco().getRua()
                    : clienteEncontradoNoBD.getEndereco().getRua());

            clienteEncontradoNoBD.getEndereco().setNumero(cliente.getEndereco().getNumero() != null && cliente.getEndereco().getNumero() > 0
                    ? cliente.getEndereco().getNumero()
                    : clienteEncontradoNoBD.getEndereco().getNumero());

        }

        return clienteEncontradoNoBD;
    }

    private Cliente criaCliente(Cliente cliente) {

        try {

            Cliente clienteCriado = new Cliente(

                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    new Endereco(
                            cliente.getEndereco().getRua(),
                            cliente.getEndereco().getNumero(),
                            cliente.getEndereco().getCep()),
                    new BigDecimal(String.valueOf(cliente.getRendimentoMensal()))
            );
            return clienteCriado;
        } catch (NullPointerException nullPointerException) {
            throw new FaltouPreencherEnderecoExcepiton("Faltou preencher o endereco");
        }
    }

}
