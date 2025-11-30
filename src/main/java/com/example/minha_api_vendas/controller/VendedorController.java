package com.example.minha_api_vendas.controller;

import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorDetalhesDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorInputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorListagemDTO;
import com.example.minha_api_vendas.service.VendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @GetMapping
    public List<VendedorListagemDTO> listarVendedores()
    {
        return vendedorService.listarVendedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDetalhesDTO> obterVendedorPorId(@PathVariable long id)
    {
        return vendedorService.buscarVendedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<VendedorListagemDTO> cadastrarVendedor(@Valid @RequestBody VendedorInputDTO vendedor)
    {
        VendedorListagemDTO vendedorCriado = vendedorService.salvar(vendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendedorCriado);

    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorListagemDTO> AtualizarVendedor(@PathVariable long id, @Valid @RequestBody VendedorInputDTO vendedor)
    {
        return vendedorService.atualizar(id, vendedor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerVendedor(@PathVariable long id)
    {
        if (vendedorService.deletarVendedorPorId(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}/veiculos")
    public ResponseEntity<List<VeiculoOutputDTO>> listarVeiculosPorVendedorId(@PathVariable long id)
    {
        List<VeiculoOutputDTO> veiculos = vendedorService.listarVeiculos(id);
        return ResponseEntity.ok(veiculos);
    }
}
