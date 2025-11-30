package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorDetalhesDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorInputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorListagemDTO;
import com.example.minha_api_vendas.exception.ApiException;
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
    private VendedorRepository vendedorRepository;

    @Autowired
    private VeiculoService veiculoService;

    private void validarId(Long id)
    {
        if (id == null || id <= 0) {
            throw ApiException.badRequest("O ID do vendedor deve ser um valor positivo e não pode ser nulo.");
        }
    }

    public List<VendedorListagemDTO> listarVendedores()
    {
        return vendedorRepository.findAll()
                .stream()
                .map(this::mapearParaListagemDTO)
                .toList();
    }

    public Optional<VendedorDetalhesDTO> buscarVendedorPorId(Long id)
    {
        validarId(id);
        return vendedorRepository.findById(id)
                .map(this::mapearParaDetalhesDTO);
    }

    public VendedorListagemDTO salvar(VendedorInputDTO vendedor)
    {
        if (vendedor == null) {
            throw ApiException.badRequest("Objeto vendedor não pode ser nulo.");
        }

        Vendedor vendedorSalvo = mapearParaEntidade(vendedor);
        if (vendedorRepository.existsByEmail(vendedorSalvo.getEmail())) {
            throw ApiException.conflict("O Email '" + vendedor.getEmail() + "' já está cadastrado");
        }

        if (vendedorRepository.existsByTelefone(vendedorSalvo.getTelefone())) {
            throw ApiException.conflict("O Telefone '" + vendedor.getTelefone() + "' já está cadastrado");
        }
        vendedorRepository.save(vendedorSalvo);
        return mapearParaListagemDTO(vendedorSalvo);
    }

    public Optional<VendedorListagemDTO> atualizar(Long id, VendedorInputDTO vendedor) {
        if (vendedor == null) {
            throw ApiException.badRequest("Objeto vendedor não pode ser nulo.");
        }

        validarId(id);

        return vendedorRepository.findById(id)
                .map(vendedorExistente -> {

                    if (!vendedorExistente.getEmail().equals(vendedor.getEmail()) &&
                            vendedorRepository.existsByEmailAndIdNot(vendedor.getEmail(), id)) {
                        throw ApiException.conflict("O novo e-mail já está em uso por outro vendedor.");
                    }

                    if (!vendedorExistente.getTelefone().equals(vendedor.getTelefone()) &&
                            vendedorRepository.existsByTelefoneAndIdNot(vendedor.getTelefone(), id)) {
                        throw ApiException.conflict("O novo telefone já está em uso por outro vendedor.");
                    }
                    vendedorExistente.setNome(vendedor.getNome());
                    vendedorExistente.setEmail(vendedor.getEmail());
                    vendedorExistente.setTelefone(vendedor.getTelefone());

                    Vendedor salvo = vendedorRepository.save(vendedorExistente);

                    return mapearParaListagemDTO(salvo);
                });
    }


    public boolean deletarVendedorPorId(Long id) {

        validarId(id);

        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Vendedor", id));

        if (!vendedor.getVeiculos().isEmpty()) {
            throw ApiException.conflict("Não foi possivel deletar vendedor com veiculos cadastrados.");
        }

        vendedorRepository.deleteById(id);
        return true;
    }

    public List<VeiculoOutputDTO> listarVeiculos(Long id)
    {
        validarId(id);

        return vendedorRepository.findById(id)
                .map(this::mapearParaDetalhesDTO)
                .map(VendedorDetalhesDTO::getVeiculos)
                .orElse(Collections.emptyList());
    }

    private VendedorListagemDTO mapearParaListagemDTO(Vendedor vendedor)
    {
        if (vendedor == null) {
            throw ApiException.badRequest("Vendedor não pode ser nulo ao mapear para listagem.");
        }
        VendedorListagemDTO vendedorDTO = new VendedorListagemDTO();
        vendedorDTO.setNome(vendedor.getNome());
        vendedorDTO.setEmail(vendedor.getEmail());
        vendedorDTO.setTelefone(vendedor.getTelefone());
        vendedorDTO.setId(vendedor.getId());
        vendedorDTO.setNumeroVeiculos(vendedor.getVeiculos() == null ? 0 : vendedor.getVeiculos().size());
        return vendedorDTO;
    }

    private VendedorDetalhesDTO mapearParaDetalhesDTO(Vendedor vendedor)
    {
        if (vendedor == null) {
            throw ApiException.badRequest("Vendedor não pode ser nulo ao mapear para detalhes.");
        }
        VendedorDetalhesDTO vendedorDTO = new VendedorDetalhesDTO();
        vendedorDTO.setNome(vendedor.getNome());
        vendedorDTO.setEmail(vendedor.getEmail());
        vendedorDTO.setTelefone(vendedor.getTelefone());
        vendedorDTO.setId(vendedor.getId());

        List<VeiculoOutputDTO> veiculoOutputDTOS = vendedor.getVeiculos() == null
                ? List.of()
                : vendedor.getVeiculos()
                .stream()
                .map(v -> veiculoService.mapearParaDTO(v))
                .toList();

        vendedorDTO.setVeiculos(veiculoOutputDTOS);
        return vendedorDTO;
    }

    protected Vendedor mapearParaEntidade(VendedorInputDTO dto)
    {
        if (dto == null) {
            return null;
        }
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(dto.getNome());
        vendedor.setEmail(dto.getEmail());
        vendedor.setTelefone(dto.getTelefone());
        return vendedor;
    }

}
