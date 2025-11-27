package com.example.minha_api_vendas.repository;

import com.example.minha_api_vendas.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

}