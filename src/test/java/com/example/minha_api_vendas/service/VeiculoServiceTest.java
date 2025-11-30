package com.example.minha_api_vendas.service;

import com.example.minha_api_vendas.dto.veiculo.VeiculoInputDTO;
import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import com.example.minha_api_vendas.exception.ApiException;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.repository.VeiculoRepository;
import com.example.minha_api_vendas.repository.VendedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do VeiculoService")
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    private Vendedor vendedor;
    private Veiculo veiculo;
    private VeiculoInputDTO veiculoInputDTO;

    @BeforeEach
    void setUp() {
        // Arrange - Setup comum para todos os testes
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("João Silva");
        vendedor.setEmail("joao@example.com");
        vendedor.setTelefone("(11) 98888-7777");
        vendedor.setVeiculos(new ArrayList<>());

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2024);
        veiculo.setPreco(new BigDecimal("120000.00"));
        veiculo.setPlaca("ABC-1234");
        veiculo.setVendido(false);
        veiculo.setVendedor(vendedor);

        veiculoInputDTO = new VeiculoInputDTO();
        veiculoInputDTO.setMarca("Toyota");
        veiculoInputDTO.setModelo("Corolla");
        veiculoInputDTO.setAno(2024);
        veiculoInputDTO.setPreco(new BigDecimal("120000.00"));
        veiculoInputDTO.setPlaca("ABC-1234");
        veiculoInputDTO.setVendedorId(1L);
    }

    // ==================== TESTES DE SALVAR ====================

    @Test
    @DisplayName("Deve salvar veículo com sucesso")
    void deveSalvarVeiculoComSucesso() {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.existsByPlaca("ABC-1234")).thenReturn(false);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        VeiculoOutputDTO resultado = veiculoService.salvar(veiculoInputDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Toyota", resultado.getMarca());
        assertEquals("Corolla", resultado.getModelo());
        assertEquals("ABC-1234", resultado.getPlaca());
        assertFalse(resultado.getVendido());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar veículo nulo")
    void deveLancarExcecaoAoSalvarVeiculoNulo() {
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.salvar(null);
        });

        assertEquals("O veiculo não pode ser nulo", exception.getMessage());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar veículo sem vendedorId")
    void deveLancarExcecaoAoSalvarVeiculoSemVendedorId() {
        // Arrange
        veiculoInputDTO.setVendedorId(null);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.salvar(veiculoInputDTO);
        });

        assertEquals("Vendedor com ID null não encontrado", exception.getMessage());
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar veículo com placa duplicada")
    void deveLancarExcecaoAoSalvarVeiculoComPlacaDuplicada() {
        // Arrange
        when(veiculoRepository.existsByPlaca("ABC-1234")).thenReturn(true);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.salvar(veiculoInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está cadastrada"));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar veículo com vendedor inexistente")
    void deveLancarExcecaoAoSalvarVeiculoComVendedorInexistente() {
        // Arrange
        when(veiculoRepository.existsByPlaca("ABC-1234")).thenReturn(false);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.salvar(veiculoInputDTO);
        });

        assertTrue(exception.getMessage().contains("Vendedor"));
        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(veiculoRepository, never()).save(any());
    }

    // ==================== TESTES DE LISTAR ====================

    @Test
    @DisplayName("Deve listar todos os veículos")
    void deveListarTodosOsVeiculos() {
        // Arrange
        List<Veiculo> veiculos = List.of(veiculo);
        when(veiculoRepository.findAll()).thenReturn(veiculos);

        // Act
        List<VeiculoOutputDTO> resultado = veiculoService.ListarVeiculos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Toyota", resultado.get(0).getMarca());
        verify(veiculoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver veículos")
    void deveRetornarListaVaziaQuandoNaoHouverVeiculos() {
        // Arrange
        when(veiculoRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<VeiculoOutputDTO> resultado = veiculoService.ListarVeiculos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== TESTES DE BUSCAR POR ID ====================

    @Test
    @DisplayName("Deve buscar veículo por ID com sucesso")
    void deveBuscarVeiculoPorIdComSucesso() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.buscarVeiculoPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("Toyota", resultado.get().getMarca());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar veículo inexistente")
    void deveRetornarOptionalVazioAoBuscarVeiculoInexistente() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.buscarVeiculoPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar veículo com ID nulo")
    void deveLancarExcecaoAoBuscarVeiculoComIdNulo() {
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.buscarVeiculoPorId(null);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar veículo com ID inválido")
    void deveLancarExcecaoAoBuscarVeiculoComIdInvalido() {
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.buscarVeiculoPorId(-1L);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }

    // ==================== TESTES DE ATUALIZAR ====================

    @Test
    @DisplayName("Deve atualizar veículo com sucesso")
    void deveAtualizarVeiculoComSucesso() {
        // Arrange
        VeiculoInputDTO dtoAtualizado = new VeiculoInputDTO();
        dtoAtualizado.setMarca("Toyota");
        dtoAtualizado.setModelo("Corolla XEI");
        dtoAtualizado.setAno(2025);
        dtoAtualizado.setPreco(new BigDecimal("130000.00"));
        dtoAtualizado.setPlaca("ABC-1234");

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.atualizar(1L, dtoAtualizado);

        // Assert
        assertTrue(resultado.isPresent());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve atualizar veículo mantendo vendedor quando vendedorId for null")
    void deveAtualizarVeiculoMantendoVendedorQuandoVendedorIdForNull() {
        // Arrange
        VeiculoInputDTO dtoSemVendedorId = new VeiculoInputDTO();
        dtoSemVendedorId.setMarca("Honda");
        dtoSemVendedorId.setModelo("Civic");
        dtoSemVendedorId.setAno(2024);
        dtoSemVendedorId.setPreco(new BigDecimal("110000.00"));
        dtoSemVendedorId.setPlaca("ABC-1234");
        dtoSemVendedorId.setVendedorId(null);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.atualizar(1L, dtoSemVendedorId);

        // Assert
        assertTrue(resultado.isPresent());
        verify(vendedorRepository, never()).findById(anyLong());
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar veículo já vendido")
    void deveLancarExcecaoAoAtualizarVeiculoJaVendido() {
        // Arrange
        veiculo.setVendido(true);
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.atualizar(1L, veiculoInputDTO);
        });

        assertTrue(exception.getMessage().contains("vendido"));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar veículo com placa duplicada")
    void deveLancarExcecaoAoAtualizarVeiculoComPlacaDuplicada() {
        // Arrange
        veiculoInputDTO.setPlaca("XYZ-9999");
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.existsByPlacaAndIdNot("XYZ-9999", 1L)).thenReturn(true);

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.atualizar(1L, veiculoInputDTO);
        });

        assertTrue(exception.getMessage().contains("já está em uso"));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar vendedor quando vendedorId for diferente")
    void deveAtualizarVendedorQuandoVendedorIdForDiferente() {
        // Arrange
        Vendedor novoVendedor = new Vendedor();
        novoVendedor.setId(2L);
        novoVendedor.setNome("Maria Santos");

        veiculoInputDTO.setVendedorId(2L);

        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendedorRepository.findById(2L)).thenReturn(Optional.of(novoVendedor));
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.atualizar(1L, veiculoInputDTO);

        // Assert
        assertTrue(resultado.isPresent());
        verify(vendedorRepository, times(1)).findById(2L);
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao atualizar veículo inexistente")
    void deveRetornarOptionalVazioAoAtualizarVeiculoInexistente() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<VeiculoOutputDTO> resultado = veiculoService.atualizar(999L, veiculoInputDTO);

        // Assert
        assertFalse(resultado.isPresent());
        verify(veiculoRepository, never()).save(any());
    }

    // ==================== TESTES DE DELETAR ====================

    @Test
    @DisplayName("Deve deletar veículo com sucesso")
    void deveDeletarVeiculoComSucesso() {
        // Arrange
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        doNothing().when(veiculoRepository).deleteById(1L);

        // Act
        boolean resultado = veiculoService.deletarVeiculoPorId(1L);

        // Assert
        assertTrue(resultado);
        verify(veiculoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar veículo inexistente")
    void deveLancarExcecaoAoDeletarVeiculoInexistente() {
        // Arrange
        when(veiculoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.deletarVeiculoPorId(999L);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(veiculoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar veículo com ID nulo")
    void deveLancarExcecaoAoDeletarVeiculoComIdNulo() {
        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            veiculoService.deletarVeiculoPorId(null);
        });

        assertTrue(exception.getMessage().contains("ID"));
    }
}