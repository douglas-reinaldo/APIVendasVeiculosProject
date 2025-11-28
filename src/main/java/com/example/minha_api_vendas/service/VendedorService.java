package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorDetalhesDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorInputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorListagemDTO;
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

    @Autowired
    private VeiculoService _veiculoService;

    public List<VendedorListagemDTO> ListarVendedores()
    {
        return _vendedorRepository.findAll()
                .stream()
                .map(this::MapearParaListagemDTO)
                .toList();
    }

    public Optional<VendedorDetalhesDTO> BuscarVendedorPorId(Long id)
    {
        return _vendedorRepository.findById(id)
                .map(this::MapearParaDetalhesDTO);
    }

    public Optional<Vendedor> BuscarVendedorEntidade(Long id)
    {
        return _vendedorRepository.findById(id);
    }

    public VendedorListagemDTO salvar(VendedorInputDTO vendedor)
    {
        Vendedor vendedorSalvo = MapearParaEntidade(vendedor);
        _vendedorRepository.save(vendedorSalvo);
        return MapearParaListagemDTO(vendedorSalvo);
    }

    public Optional<VendedorListagemDTO> atualizar(Long id, VendedorInputDTO vendedor) {

        return _vendedorRepository.findById(id)
                .map(vendedorExistente -> {

                    vendedorExistente.setNome(vendedor.getNome());
                    vendedorExistente.setEmail(vendedor.getEmail());
                    vendedorExistente.setTelefone(vendedor.getTelefone());

                    Vendedor salvo = _vendedorRepository.save(vendedorExistente);

                    return MapearParaListagemDTO(salvo);
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

    public List<VeiculoDTO> ListarVeiculos(long id)
    {
        return _vendedorRepository.findById(id)
                .map(this::MapearParaDetalhesDTO)
                .map(VendedorDetalhesDTO::getVeiculos)
                .orElse(Collections.emptyList());
    }

    private VendedorListagemDTO MapearParaListagemDTO(Vendedor vendedor)
    {
        VendedorListagemDTO vendedorDTO = new VendedorListagemDTO();
        vendedorDTO.setNome(vendedor.getNome());
        vendedorDTO.setEmail(vendedor.getEmail());
        vendedorDTO.setTelefone(vendedor.getTelefone());
        vendedorDTO.setId(vendedor.getId());
        return vendedorDTO;
    }

    private VendedorDetalhesDTO MapearParaDetalhesDTO(Vendedor vendedor)
    {
        VendedorDetalhesDTO vendedorDTO = new VendedorDetalhesDTO();
        vendedorDTO.setNome(vendedor.getNome());
        vendedorDTO.setEmail(vendedor.getEmail());
        vendedorDTO.setTelefone(vendedor.getTelefone());
        vendedorDTO.setId(vendedor.getId());

        List<VeiculoDTO> veiculoDTOS = vendedor.getVeiculos()
                .stream()
                .map(v -> _veiculoService.MapearParaDTO(v))
                .toList();

        vendedorDTO.setVeiculos(veiculoDTOS);
        return vendedorDTO;
    }

    protected Vendedor MapearParaEntidade(VendedorInputDTO dto)
    {
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(dto.getNome());
        vendedor.setEmail(dto.getEmail());
        vendedor.setTelefone(dto.getTelefone());
        return vendedor;
    }




}
