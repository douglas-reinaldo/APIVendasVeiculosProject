package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.dto.venda.VendaInputDTO;
import com.example.minha_api_vendas.dto.venda.VendaOutputDTO;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Venda;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import com.example.minha_api_vendas.repository.VendaRepository;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository _vendaRepository;

    @Autowired
    private VendedorRepository _vendedorRepository;

    @Autowired
    private VeiculoRepository _veiculoRepository;

    public VendaOutputDTO RegistrarVenda(VendaInputDTO  vendaInputDTO) throws Exception {
        Vendedor vendedor = _vendedorRepository.findById(vendaInputDTO.getVendedorId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Veiculo veiculo = _veiculoRepository.findById(vendaInputDTO.getVeiculoId())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);


        Venda venda = new Venda();
        venda.setVendedor(vendedor);
        venda.setVeiculo(veiculo);
        venda.setDataVenda(LocalDate.now());
        venda.setValorFinal(veiculo.getPreco());

        veiculo.setVendido(true);
        venda = _vendaRepository.save(venda);
        _veiculoRepository.save(veiculo);

        return new VendaOutputDTO(venda);

    }

    public List<VendaOutputDTO> ListarVendas()
    {
        return _vendaRepository.findAll()
                .stream()
                .map(this::MapearParaDTO)
                .toList();
    }

    protected VendaOutputDTO MapearParaDTO(Venda venda) {
        VendaOutputDTO dto = new VendaOutputDTO();

        dto.setId(venda.getId());
        dto.setDataVenda(venda.getDataVenda());
        dto.setValorFinal(venda.getValorFinal());
        dto.setVendedorNome(venda.getVendedor().getNome());
        dto.setVeiculoModelo(venda.getVeiculo().getModelo());

        return dto;
    }

}
