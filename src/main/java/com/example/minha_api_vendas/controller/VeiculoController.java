package com.example.minha_api_vendas.controller;

import com.example.minha_api_vendas.dto.veiculo.VeiculoInputDTO;
import com.example.minha_api_vendas.dto.veiculo.VeiculoOutputDTO;
import com.example.minha_api_vendas.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public List<VeiculoOutputDTO> listarVeiculos()
    {
        return veiculoService.ListarVeiculos();
    }

    @PostMapping
    public ResponseEntity<VeiculoOutputDTO> criarVeiculo(@Valid @RequestBody VeiculoInputDTO veiculoInputDTO) {
        VeiculoOutputDTO novoVeiculo = veiculoService.salvar(veiculoInputDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoVeiculo);

    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoOutputDTO> buscarPorId(@PathVariable Long id)
    {
        return veiculoService.buscarVeiculoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoOutputDTO> atualizarVeiculo(@PathVariable Long id, @Valid @RequestBody VeiculoInputDTO veiculoInputDTO) {
        return veiculoService.atualizar(id, veiculoInputDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        if (veiculoService.deletarVeiculoPorId(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
