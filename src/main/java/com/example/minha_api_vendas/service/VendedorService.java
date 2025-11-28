package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {
    @Autowired
    private VendedorRepository _vendedorRepository;

    public List<Vendedor> ListarVendedores()
    {
        return _vendedorRepository.findAll();
    }

    public Optional<Vendedor> BuscarVendedorPorId(Long id)
    {
        return _vendedorRepository.findById(id);
    }

    public Vendedor salvar(Vendedor vendedor)
    {
        return _vendedorRepository.save(vendedor);
    }

    public Optional<Vendedor> atualizar(Long id, Vendedor vendedor)
    {
        return _vendedorRepository.findById(id)
                .map(vendedorExistente -> {
                    vendedorExistente.setNome(vendedor.getNome());
                    vendedorExistente.setEmail(vendedor.getEmail());
                    vendedorExistente.setTelefone(vendedor.getTelefone());
                    return _vendedorRepository.save(vendedorExistente);
                });
    }

    public boolean DeletarVendedorPorId(Long id)
    {
        if (_vendedorRepository.existsById(id)) {
            _vendedorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Veiculo> ListarVeiculos(long id)
    {
        return _vendedorRepository.findById(id)
                .map(Vendedor::getVeiculos)
                .orElse(Collections.emptyList());

    }



}
