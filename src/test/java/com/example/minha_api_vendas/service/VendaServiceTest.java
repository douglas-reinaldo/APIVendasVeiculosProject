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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do VendaService")
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VendaService vendaService;

    private Vendedor vendedor;
    private Veiculo veiculo;
    private Venda venda;
    private VendaInputDTO vendaInputDTO;

    @BeforeEach
    void setUp() {
        vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setNome("João Silva");
        vendedor.setEmail("joao@example.com");
        vendedor.setTelefone("(11) 98888-7777");

        veiculo = new Veiculo();
        veiculo.setId(1L);
        veiculo.setMarca("Toyota");
        veiculo.setModelo("Corolla");
        veiculo.setAno(2024);
        veiculo.setPreco(new BigDecimal("120000.00"));
        veiculo.setPlaca("ABC-1234");
        veiculo.setVendido(false);
        veiculo.setVendedor(vendedor);

        venda = new Venda();
        venda.setId(1L);
        venda.setVendedor(vendedor);
        venda.setVeiculo(veiculo);
        venda.setDataVenda(LocalDate.now());
        venda.setValorFinal(new BigDecimal("120000.00"));

        vendaInputDTO = new VendaInputDTO();
        vendaInputDTO.setVendedorId(1L);
        vendaInputDTO.setVeiculoId(1L);
    }

    // ==================== TESTES DE REGISTRAR VENDA ====================

    @Test
    @DisplayName("Deve registrar venda com sucesso")
    void deveRegistrarVendaComSucesso() throws Exception {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        VendaOutputDTO resultado = vendaService.registrarVenda(vendaInputDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("Corolla", resultado.getVeiculoModelo());
        assertEquals("João Silva", resultado.getVendedorNome());
        assertEquals(new BigDecimal("120000.00"), resultado.getValorFinal());
        verify(vendaRepository, times(1)).save(any(Venda.class));
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar venda com vendedor inexistente")
    void deveLancarExcecaoAoRegistrarVendaComVendedorInexistente() {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendaService.registrarVenda(vendaInputDTO);
        });

        assertTrue(exception.getMessage().contains("Vendedor"));
        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(vendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar venda com veículo inexistente")
    void deveLancarExcecaoAoRegistrarVendaComVeiculoInexistente() {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendaService.registrarVenda(vendaInputDTO);
        });

        assertTrue(exception.getMessage().contains("Veiculo"));
        assertTrue(exception.getMessage().contains("não encontrado"));
        verify(vendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar venda de veículo já vendido")
    void deveLancarExcecaoAoRegistrarVendaDeVeiculoJaVendido() {
        // Arrange
        veiculo.setVendido(true);
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> {
            vendaService.registrarVenda(vendaInputDTO);
        });

        assertTrue(exception.getMessage().contains("já foi vendido"));
        verify(vendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve marcar veículo como vendido após registrar venda")
    void deveMacarVeiculoComoVendidoAposRegistrarVenda() throws Exception {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(invocation -> {
            Veiculo v = invocation.getArgument(0);
            assertTrue(v.getVendido());
            return v;
        });

        // Act
        vendaService.registrarVenda(vendaInputDTO);

        // Assert
        verify(veiculoRepository, times(1)).save(argThat(v -> v.getVendido()));
    }

    // ==================== TESTES DE LISTAR VENDAS ====================

    @Test
    @DisplayName("Deve listar todas as vendas")
    void deveListarTodasAsVendas() {
        // Arrange
        List<Venda> vendas = List.of(venda);
        when(vendaRepository.findAll()).thenReturn(vendas);

        // Act
        List<VendaOutputDTO> resultado = vendaService.listarVendas();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Corolla", resultado.get(0).getVeiculoModelo());
        verify(vendaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver vendas")
    void deveRetornarListaVaziaQuandoNaoHouverVendas() {
        // Arrange
        when(vendaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<VendaOutputDTO> resultado = vendaService.listarVendas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ==================== TESTES DE BUSCAR VENDA POR ID ====================

    @Test
    @DisplayName("Deve buscar venda por ID com sucesso")
    void deveBuscarVendaPorIdComSucesso() {
        // Arrange
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        // Act
        Optional<VendaOutputDTO> resultado = vendaService.buscarVendaPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("João Silva", resultado.get().getVendedorNome());
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar venda inexistente")
    void deveRetornarOptionalVazioAoBuscarVendaInexistente() {
        // Arrange
        when(vendaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<VendaOutputDTO> resultado = vendaService.buscarVendaPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve usar preço do veículo como valor final da venda")
    void deveUsarPrecoDoVeiculoComoValorFinal() throws Exception {
        // Arrange
        BigDecimal precoEsperado = new BigDecimal("120000.00");
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        VendaOutputDTO resultado = vendaService.registrarVenda(vendaInputDTO);

        // Assert
        assertEquals(precoEsperado, resultado.getValorFinal());
    }

    @Test
    @DisplayName("Deve definir data da venda como data atual")
    void deveDefinirDataDaVendaComoDataAtual() throws Exception {
        // Arrange
        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedor));
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        // Act
        VendaOutputDTO resultado = vendaService.registrarVenda(vendaInputDTO);

        // Assert
        assertEquals(LocalDate.now(), resultado.getDataVenda());
    }
}