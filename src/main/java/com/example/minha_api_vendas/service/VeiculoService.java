package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository _veiculoRepository;

    public Veiculo salvar(Veiculo veiculo)
    {
        return _veiculoRepository.save(veiculo);
    }

    public List<Veiculo> listarVeiculos()
    {
        return _veiculoRepository.findAll();
    }

    public Optional<Veiculo> buscarVeiculoPorId(Long id)
    {
        return _veiculoRepository.findById(id);
    }

    public Optional<Veiculo> atualizar(long id, Veiculo veiculoAtualizado) {


        return _veiculoRepository.findById(id)
                .map(veiculoExistente -> {
                    veiculoExistente.setMarca(veiculoAtualizado.getMarca());
                    veiculoExistente.setModelo(veiculoAtualizado.getModelo());
                    veiculoExistente.setAno(veiculoAtualizado.getAno());
                    veiculoExistente.setPreco(veiculoAtualizado.getPreco());
                    veiculoExistente.setVendido(veiculoAtualizado.getVendido());

                    System.out.println(veiculoExistente);
                    return _veiculoRepository.save(veiculoExistente);
                });

    }

    public boolean deletarVeiculoPorId(Long id)
    {
        _veiculoRepository.deleteById(id);
        return true;
    }
}
