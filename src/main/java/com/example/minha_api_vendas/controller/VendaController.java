package com.example.minha_api_vendas.controller;

import com.example.minha_api_vendas.dto.venda.VendaInputDTO;
import com.example.minha_api_vendas.dto.venda.VendaOutputDTO;
import com.example.minha_api_vendas.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;


    @GetMapping
    public List<VendaOutputDTO> listarVendas(){
        return vendaService.listarVendas();
    }

    @PostMapping
    public ResponseEntity<VendaOutputDTO> registrarVenda(@Valid @RequestBody VendaInputDTO vendaInputDTO) throws Exception {
        VendaOutputDTO novaVenda = vendaService.registrarVenda(vendaInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
    }


    @GetMapping("/{id}")
    public ResponseEntity<VendaOutputDTO> obterVendaPorId(@PathVariable long id)
    {
        return vendaService.buscarVendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
