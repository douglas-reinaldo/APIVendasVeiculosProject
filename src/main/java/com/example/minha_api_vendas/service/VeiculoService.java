package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoInputDTO;
import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.exception.ApiException;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository _veiculoRepository;

    @Autowired
    private VendedorRepository _vendedorRepository;

    private void validarId(Long id)
    {
        if (id == null || id <= 0) {
            throw ApiException.badRequest("O ID do vendedor deve ser um valor positivo e não pode ser nulo.");
        }
    }

    public VeiculoDTO salvar(VeiculoInputDTO veiculoInputDTO) {

        if (veiculoInputDTO == null){
            throw ApiException.badRequest("O veiculo não pode ser nulo");
        }

        Vendedor vendedor = _vendedorRepository.findById(veiculoInputDTO.getVendedorId())
                .orElseThrow(() -> ApiException.notFound("Vendedor", veiculoInputDTO.getVendedorId()));

        Veiculo veiculo = new Veiculo();
        veiculo.setAno(veiculoInputDTO.getAno());
        veiculo.setMarca(veiculoInputDTO.getMarca());
        veiculo.setModelo(veiculoInputDTO.getModelo());
        veiculo.setPreco(veiculoInputDTO.getPreco());

        if (_veiculoRepository.existsByPlaca(veiculoInputDTO.getPlaca())) {
            throw ApiException.conflict("A placa '" + veiculoInputDTO.getPlaca() + "' já está cadastrada.");
        }
        veiculo.setPlaca(veiculoInputDTO.getPlaca());
        veiculo.setVendido(false);
        veiculo.setVendedor(vendedor);

        Veiculo salvo = _veiculoRepository.save(veiculo);

        return mapearParaDTO(salvo);
    }

    public List<VeiculoDTO> ListarVeiculos()
    {
        return _veiculoRepository.findAll().stream()
                .map(this::mapearParaDTO)
                .collect(Collectors.toList());

    }

    public Optional<VeiculoDTO> buscarVeiculoPorId(Long id)
    {
        validarId(id);
        return _veiculoRepository.findById(id)
                .map(this::mapearParaDTO);
    }

    public Optional<VeiculoDTO> atualizar(long id, VeiculoInputDTO dto) {
        validarId(id);
        if (dto == null) {
            throw ApiException.badRequest("O veículo não pode ser nulo");
        }

        return _veiculoRepository.findById(id)
                .map(veiculoExistente -> {

                    if (veiculoExistente.getVendido()){
                        throw ApiException.badRequest("veiculo já vendido");
                    }

                    if (!veiculoExistente.getPlaca().equals(dto.getPlaca())) {
                        if (_veiculoRepository.existsByPlacaAndIdNot(dto.getPlaca(), id)) {
                            throw ApiException.conflict("A nova placa '" + dto.getPlaca() + "' já está em uso por outro veículo.");
                        }
                        veiculoExistente.setPlaca(dto.getPlaca());
                    }

                    if (!veiculoExistente.getVendedor().getId().equals(dto.getVendedorId())) {
                        Vendedor novoVendedor = _vendedorRepository.findById(dto.getVendedorId())
                                .orElseThrow(() -> ApiException.notFound("Vendedor", dto.getVendedorId()));
                        veiculoExistente.setVendedor(novoVendedor);
                    }


                    veiculoExistente.setMarca(dto.getMarca());
                    veiculoExistente.setModelo(dto.getModelo());
                    veiculoExistente.setAno(dto.getAno());
                    veiculoExistente.setPreco(dto.getPreco());

                    Veiculo salvo = _veiculoRepository.save(veiculoExistente);
                    return mapearParaDTO(salvo);
                });
    }


    public boolean deletarVeiculoPorId(Long id)
    {
        validarId(id);
        _veiculoRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Veiculo", id));

        _veiculoRepository.deleteById(id);
        return true;
    }

    protected VeiculoDTO mapearParaDTO(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setMarca(veiculo.getMarca());
        dto.setModelo(veiculo.getModelo());
        dto.setAno(veiculo.getAno());
        dto.setPreco(veiculo.getPreco());
        dto.setVendido(veiculo.getVendido());
        dto.setPlaca(veiculo.getPlaca());

        return dto;
    }

}
