package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.DTO.VeiculoDTO;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository _veiculoRepository;

    @Autowired
    private VendedorService _vendedorService;

    public Veiculo Salvar(VeiculoDTO veiculoDTO) throws Exception {
        Vendedor vendedor = _vendedorService.BuscarVendedorPorId(veiculoDTO.getVendedorId())
                .orElseThrow(() -> new Exception("Vendedor não encontrado"));


        Veiculo veiculo = new Veiculo();
        veiculo.setAno(veiculoDTO.getAno());
        veiculo.setMarca(veiculoDTO.getMarca());
        veiculo.setModelo(veiculoDTO.getModelo());
        veiculo.setPreco(veiculoDTO.getPreco());
        veiculo.setVendido(veiculoDTO.getVendido());

        veiculo.setVendedor(vendedor);
        return _veiculoRepository.save(veiculo);
    }

    public List<Veiculo> ListarVeiculos()
    {
        return _veiculoRepository.findAll();
    }

    public Optional<Veiculo> BuscarVeiculoPorId(Long id)
    {
        return _veiculoRepository.findById(id);
    }

    public Optional<Veiculo> Atualizar(long id, Veiculo veiculoAtualizado) {


        return _veiculoRepository.findById(id)
                .map(veiculoExistente -> {
                    veiculoExistente.setMarca(veiculoAtualizado.getMarca());
                    veiculoExistente.setModelo(veiculoAtualizado.getModelo());
                    veiculoExistente.setAno(veiculoAtualizado.getAno());
                    veiculoExistente.setPreco(veiculoAtualizado.getPreco());
                    veiculoExistente.setVendido(veiculoAtualizado.getVendido());

                    return _veiculoRepository.save(veiculoExistente);
                });

    }

    public boolean DeletarVeiculoPorId(Long id)
    {
        if (_veiculoRepository.existsById(id))
        {
            _veiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
