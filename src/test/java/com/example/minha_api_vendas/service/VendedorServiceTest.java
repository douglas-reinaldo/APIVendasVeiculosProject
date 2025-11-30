package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorDetalhesDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorInputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorListagemDTO;
import com.example.minha_api_vendas.exception.ApiException;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.repository.VendedorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do VendedorService")
class VendedorServiceTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VeiculoService veiculoService;

    @InjectMocks
    private VendedorService vendedorService;

    private Vendedor vendedor;
    private VendedorInputDTO vendedorInputDTO;
    private Veiculo veiculo;

    @BeforeEach
    void setUp() {
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("Carlos Mendes");
        vendedor.setEmail("carlos@example.com");
        vendedor.setTelefone("(11) 97777-8888");
        vendedor.setVeiculos(new ArrayList<>());

        vendedorInputDTO = new VendedorInputDTO();
        vendedorInputDTO.setNome("Carlos Mendes");
        vendedorInputDTO.setEmail("carlos@example.com");
        vendedorInputDTO.setTelefone("(11) 97777-8888");

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setModelo("Corolla");
        veiculo.setMarca("Toyota");
    }

    // ==================== TESTES DE LISTAR ====================

    @Test
    @DisplayName("Deve listar todos os vendedores")
    void deveListarTodosOsVendedores() {
        when(vendedorRepository.findAll()).thenReturn(List.of(vendedor));

        List<VendedorListagemDTO> resultado = vendedorService.listarVendedores();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Carlos Mendes", resultado.get(0).getNome());
        verify(vendedorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver vendedores")
    void deveRetornarListaVaziaQuandoNaoHouverVendedores() {
        when(vendedorRepository.findAll()).thenReturn(new ArrayList<>());

        List<VendedorListagemDTO> resultado = vendedorService.listarVendedores();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== TESTES DE BUSCAR POR ID ====================

    @Test
    @DisplayName("Deve buscar vendedor por ID com sucesso")
    void deveBuscarVendedorPorIdComSucesso() {
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));

        Optional<VendedorDetalhesDTO> resultado = vendedorService.buscarVendedorPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Carlos Mendes", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar vendedor inexistente")
    void deveRetornarOptionalVazioAoBuscarVendedorInexistente() {
        when(vendedorRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<VendedorDetalhesDTO> resultado = vendedorService.buscarVendedorPorId(999L);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar vendedor com ID nulo")
    void deveLancarExcecaoAoBuscarVendedorComIdNulo() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.buscarVendedorPorId(null);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar vendedor com ID inválido")
    void deveLancarExcecaoAoBuscarVendedorComIdInvalido() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.buscarVendedorPorId(-5L);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }

    // ==================== TESTES DE SALVAR ====================

    @Test
    @DisplayName("Deve salvar vendedor com sucesso")
    void deveSalvarVendedorComSucesso() {
        // nesse teste só stubbamos o que vamos usar
        when(vendedorRepository.existsByEmail(anyString())).thenReturn(false);
        when(vendedorRepository.existsByTelefone(anyString())).thenReturn(false);
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedor);

        VendedorListagemDTO resultado = vendedorService.salvar(vendedorInputDTO);

        assertNotNull(resultado);
        assertEquals("Carlos Mendes", resultado.getNome());
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar vendedor nulo")
    void deveLancarExcecaoAoSalvarVendedorNulo() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.salvar(null);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("nulo"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar vendedor com email duplicado")
    void deveLancarExcecaoAoSalvarVendedorComEmailDuplicado() {
        // só o stub necessário: existsByEmail retorna true
        when(vendedorRepository.existsByEmail("carlos@example.com")).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.salvar(vendedorInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está cadastrado"));
        verify(vendedorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar vendedor com telefone duplicado")
    void deveLancarExcecaoAoSalvarVendedorComTelefoneDuplicado() {
        when(vendedorRepository.existsByEmail(anyString())).thenReturn(false);
        when(vendedorRepository.existsByTelefone("(11) 97777-8888")).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.salvar(vendedorInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está cadastrado"));
        verify(vendedorRepository, never()).save(any());
    }

    // ==================== TESTES DE ATUALIZAR ====================

    @Test
    @DisplayName("Deve atualizar vendedor com sucesso")
    void deveAtualizarVendedorComSucesso() {
        // Stubbings realmente usados
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedor);

        // Executa
        Optional<VendedorListagemDTO> resultado = vendedorService.atualizar(1L, vendedorInputDTO);

        // Verificações
        assertTrue(resultado.isPresent());
        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
    }


    @Test
    @DisplayName("Deve retornar Optional vazio ao atualizar vendedor inexistente")
    void deveRetornarOptionalVazioAoAtualizarVendedorInexistente() {
        when(vendedorRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<VendedorListagemDTO> resultado = vendedorService.atualizar(999L, vendedorInputDTO);

        assertFalse(resultado.isPresent());
        verify(vendedorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar vendedor nulo")
    void deveLancarExcecaoAoAtualizarVendedorNulo() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.atualizar(1L, null);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("nulo"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar vendedor com email já existente")
    void deveLancarExcecaoAoAtualizarVendedorComEmailDuplicado() {

        // Simula alteração de email - necessário para entrar na validação
        vendedorInputDTO.setEmail("novoemail@example.com");

        when(vendedorRepository.findById(1L))
                .thenReturn(Optional.of(vendedor));

        when(vendedorRepository.existsByEmailAndIdNot(anyString(), anyLong()))
                .thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.atualizar(1L, vendedorInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está em uso"));
        verify(vendedorRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve lançar exceção ao atualizar vendedor com telefone já existente")
    void deveLancarExcecaoAoAtualizarVendedorComTelefoneDuplicado() {

        vendedorInputDTO.setTelefone("(11) 96666-5555"); // <--- telefone diferente para ativar a validação

        when(vendedorRepository.findById(1L))
                .thenReturn(Optional.of(vendedor));

        when(vendedorRepository.existsByTelefoneAndIdNot(anyString(), anyLong()))
                .thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.atualizar(1L, vendedorInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está em uso"));
        verify(vendedorRepository, never()).save(any());
    }



    // ==================== TESTES DE DELETAR ====================

    @Test
    @DisplayName("Deve deletar vendedor com sucesso")
    void deveDeletarVendedorComSucesso() {
        vendedor.setVeiculos(new ArrayList<>());

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        doNothing().when(vendedorRepository).deleteById(1L);

        boolean resultado = vendedorService.deletarVendedorPorId(1L);

        assertTrue(resultado);
        verify(vendedorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar vendedor inexistente")
    void deveLancarExcecaoAoDeletarVendedorInexistente() {
        when(vendedorRepository.findById(999L)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.deletarVendedorPorId(999L);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(vendedorRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar vendedor que possui veículos")
    void deveLancarExcecaoAoDeletarVendedorComVeiculos() {
        vendedor.getVeiculos().add(veiculo);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));

        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.deletarVendedorPorId(1L);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("veiculos"));
        verify(vendedorRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar vendedor com ID nulo")
    void deveLancarExcecaoAoDeletarVendedorComIdNulo() {
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendedorService.deletarVendedorPorId(null);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }

    // ==================== TESTES DE LISTAR VEÍCULOS ====================

    @Test
    @DisplayName("Deve listar veículos do vendedor")
    void deveListarVeiculosDoVendedor() {
        vendedor.getVeiculos().add(veiculo);

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoService.mapearParaDTO(any())).thenReturn(new VeiculoOutputDTO());

        List<VeiculoOutputDTO> resultado = vendedorService.listarVeiculos(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao listar veículos de vendedor inexistente")
    void deveRetornarListaVaziaAoListarVeiculosDeVendedorInexistente() {
        when(vendedorRepository.findById(999L)).thenReturn(Optional.empty());

        List<VeiculoOutputDTO> resultado = vendedorService.listarVeiculos(999L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
