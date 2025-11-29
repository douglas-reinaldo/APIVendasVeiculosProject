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
    private VendaService _vendaService;


    @GetMapping
    public List<VendaOutputDTO> ListarVendas(){
        return _vendaService.ListarVendas();
    }

    @PostMapping
    public ResponseEntity<VendaOutputDTO> RegistrarVenda(@Valid @RequestBody VendaInputDTO vendaInputDTO)
    {
        try {
            VendaOutputDTO novaVenda = _vendaService.RegistrarVenda(vendaInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVenda);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<VendaOutputDTO> ObterVendaPorId(@PathVariable long id)
    {
        return _vendaService.BuscarVendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
