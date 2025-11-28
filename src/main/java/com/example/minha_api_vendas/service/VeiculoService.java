package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoInputDTO;
import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository _veiculoRepository;

    @Autowired
    private VendedorRepository _vendedorRepository;

    public VeiculoDTO Salvar(VeiculoInputDTO veiculoInputDTO) {

        Vendedor vendedor = _vendedorRepository.findById(veiculoInputDTO.getVendedorId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendedor não encontrado")
                );

        Veiculo veiculo = new Veiculo();
        veiculo.setAno(veiculoInputDTO.getAno());
        veiculo.setMarca(veiculoInputDTO.getMarca());
        veiculo.setModelo(veiculoInputDTO.getModelo());
        veiculo.setPreco(veiculoInputDTO.getPreco());
        veiculo.setVendido(veiculoInputDTO.getVendido());

        veiculo.setVendedor(vendedor);

        Veiculo salvo = _veiculoRepository.save(veiculo);

        return MapearParaDTO(salvo);
    }





    public List<VeiculoDTO> ListarVeiculos()
    {
        return _veiculoRepository.findAll().stream()
                .map(this::MapearParaDTO)
                .collect(Collectors.toList());

    }

    public Optional<VeiculoDTO> BuscarVeiculoPorId(Long id)
    {
        return _veiculoRepository.findById(id)
                .map(this::MapearParaDTO);
    }

    public Optional<VeiculoDTO> Atualizar(long id, VeiculoInputDTO dto) {

        return _veiculoRepository.findById(id)
                .map(veiculoExistente -> {
                    veiculoExistente.setMarca(dto.getMarca());
                    veiculoExistente.setModelo(dto.getModelo());
                    veiculoExistente.setAno(dto.getAno());
                    veiculoExistente.setPreco(dto.getPreco());
                    veiculoExistente.setVendido(dto.getVendido());

                    Veiculo salvo = _veiculoRepository.save(veiculoExistente);

                    return MapearParaDTO(salvo);
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

    protected VeiculoDTO MapearParaDTO(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setMarca(veiculo.getMarca());
        dto.setModelo(veiculo.getModelo());
        dto.setAno(veiculo.getAno());
        dto.setPreco(veiculo.getPreco());
        dto.setVendido(veiculo.getVendido());

        return dto;
    }

}
