package com.minsait.apiEmprestimo.repository;

import com.minsait.apiEmprestimo.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

}
