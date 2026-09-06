package com.gco.fidelizacion.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gco.fidelizacion.dtos.pais.PaisRequestDTO;
import com.gco.fidelizacion.dtos.pais.PaisResponseDTO;
import com.gco.fidelizacion.services.pais.IServicioPais;

@RestController
@RequestMapping("/api/paises")
public class PaisControlador {

    private final IServicioPais servicioPais;

    public PaisControlador(IServicioPais servicioPais) {
        this.servicioPais = servicioPais;
    }

    @PostMapping
    public ResponseEntity<PaisResponseDTO> crear(@RequestBody PaisRequestDTO dto) {
        PaisResponseDTO paisCreado = servicioPais.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paisCreado);
    }

    @GetMapping
    public ResponseEntity<List<PaisResponseDTO>> listar() {
        return ResponseEntity.ok(servicioPais.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioPais.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> actualizar(@PathVariable UUID id, @RequestBody PaisRequestDTO dto) {
        return ResponseEntity.ok(servicioPais.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioPais.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
