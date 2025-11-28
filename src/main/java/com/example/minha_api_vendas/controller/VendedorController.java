package com.example.minha_api_vendas.controller;

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
    public List<Vendedor> ListarVendedores()
    {
        return _vendedorService.ListarVendedores();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> ObterVendedorPorId(@PathVariable long id)
    {
        return _vendedorService.BuscarVendedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Vendedor> cadastrarVendedor(@RequestBody Vendedor vendedor)
    {
        try {
            Vendedor vendedorCriado = _vendedorService.salvar(vendedor);
            return ResponseEntity.ok().body(vendedorCriado);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> AtualizarVendedor(@PathVariable long id, @RequestBody Vendedor vendedor)
    {
        return _vendedorService.atualizar(id, vendedor)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vendedor> removerVendedor(@PathVariable long id)
    {
        if (_vendedorService.DeletarVendedorPorId(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/{id}/veiculos")
    public ResponseEntity<List<Veiculo>> listarVeiculosPorVendedorId(@PathVariable long id)
    {
        if (_vendedorService.BuscarVendedorPorId(id).isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        List<Veiculo> veiculos = _vendedorService.ListarVeiculos(id);
        return ResponseEntity.ok(veiculos);
    }
}
