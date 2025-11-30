package com.example.minha_api_vendas.service;
import com.example.minha_api_vendas.dto.venda.VendaInputDTO;
import com.example.minha_api_vendas.dto.venda.VendaOutputDTO;
import com.example.minha_api_vendas.exception.ApiException;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Venda;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import com.example.minha_api_vendas.repository.VendaRepository;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    public VendaOutputDTO registrarVenda(VendaInputDTO  vendaInputDTO) throws Exception {
        Vendedor vendedor = vendedorRepository.findById(vendaInputDTO.getVendedorId())
                .orElseThrow(() -> ApiException.notFound("Vendedor", vendaInputDTO.getVendedorId()));

        Veiculo veiculo = veiculoRepository.findById(vendaInputDTO.getVeiculoId())
                .orElseThrow(() -> ApiException.notFound("Veiculo", vendaInputDTO.getVeiculoId()));


        if (veiculo.getVendido())
        {
            throw ApiException.badRequest("Esse veiculo já foi vendido");
        }

        Venda venda = new Venda();
        venda.setVendedor(vendedor);
        venda.setVeiculo(veiculo);
        venda.setDataVenda(LocalDate.now());
        venda.setValorFinal(veiculo.getPreco());

        veiculo.setVendido(true);
        venda = vendaRepository.save(venda);
        veiculoRepository.save(veiculo);

        return new VendaOutputDTO(venda);

    }

    public List<VendaOutputDTO> listarVendas()
    {
        return vendaRepository.findAll()
                .stream()
                .map(this::mapearParaDTO)
                .toList();
    }

    protected VendaOutputDTO mapearParaDTO(Venda venda) {
        VendaOutputDTO dto = new VendaOutputDTO();

        dto.setId(venda.getId());
        dto.setDataVenda(venda.getDataVenda());
        dto.setValorFinal(venda.getValorFinal());
        dto.setVendedorNome(venda.getVendedor().getNome());
        dto.setVeiculoModelo(venda.getVeiculo().getModelo());

        return dto;
    }

    public Optional<VendaOutputDTO> buscarVendaPorId(Long id)
    {
        return vendaRepository.findById(id)
                .map(this::mapearParaDTO);
    }

}
