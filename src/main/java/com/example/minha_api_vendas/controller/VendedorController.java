package com.example.minha_api_vendas.controller;

import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorDetalhesDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorInputDTO;
import com.example.minha_api_vendas.dto.vendedor.VendedorListagemDTO;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.model.Vendedor;
import com.example.minha_api_vendas.service.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService _vendedorService;

    @GetMapping
    public List<VendedorListagemDTO> ListarVendedores()
    {
        return _vendedorService.ListarVendedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDetalhesDTO> ObterVendedorPorId(@PathVariable long id)
    {
        return _vendedorService.BuscarVendedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<VendedorListagemDTO> cadastrarVendedor(@RequestBody VendedorInputDTO vendedor)
    {
        try {
            VendedorListagemDTO vendedorCriado = _vendedorService.salvar(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(vendedorCriado);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorListagemDTO> AtualizarVendedor(@PathVariable long id, @RequestBody VendedorInputDTO vendedor)
    {
        return _vendedorService.atualizar(id, vendedor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity removerVendedor(@PathVariable long id)
    {
        if (_vendedorService.DeletarVendedorPorId(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}/veiculos")
    public ResponseEntity<List<VeiculoDTO>> listarVeiculosPorVendedorId(@PathVariable long id)
    {
        if (_vendedorService.BuscarVendedorPorId(id).isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        List<VeiculoDTO> veiculos = _vendedorService.ListarVeiculos(id);
        return ResponseEntity.ok(veiculos);
    }
}
