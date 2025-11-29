package com.example.minha_api_vendas.repository;

import com.example.minha_api_vendas.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    boolean existsByEmail(String email);
    boolean existsByTelefone(String telefone);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByTelefoneAndIdNot(String telefone, Long id);
}