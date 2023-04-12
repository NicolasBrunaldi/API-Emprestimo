package com.minsait.apiEmprestimo.repository;

import com.minsait.apiEmprestimo.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findAllByCpfCliente(String cpf);
}
