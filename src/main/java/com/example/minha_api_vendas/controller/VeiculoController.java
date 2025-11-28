package com.example.minha_api_vendas.controller;

import com.example.minha_api_vendas.dto.veiculo.VeiculoInputDTO;
import com.example.minha_api_vendas.dto.veiculo.VeiculoDTO;
import com.example.minha_api_vendas.model.Veiculo;
import com.example.minha_api_vendas.service.VeiculoService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService _veiculoService;

    @GetMapping
    public List<VeiculoDTO> ListarVeiculos()
    {
        return _veiculoService.ListarVeiculos();
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> criarVeiculo(@RequestBody VeiculoInputDTO veiculoInputDTO) {
        try {
            VeiculoDTO novoVeiculo = _veiculoService.Salvar(veiculoInputDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> buscarPorId(@PathVariable Long id)
    {
        return _veiculoService.BuscarVeiculoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizarVeiculo(@PathVariable Long id, @RequestBody VeiculoInputDTO veiculoInputDTO) {
        return _veiculoService.Atualizar(id, veiculoInputDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        if (_veiculoService.DeletarVeiculoPorId(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
