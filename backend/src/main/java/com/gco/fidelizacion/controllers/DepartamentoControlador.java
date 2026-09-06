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

import com.gco.fidelizacion.dtos.departamento.DepartamentoRequestDTO;
import com.gco.fidelizacion.dtos.departamento.DepartamentoResponseDTO;
import com.gco.fidelizacion.services.departamento.IServicioDepartamento;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoControlador {

    private final IServicioDepartamento servicioDepartamento;

    public DepartamentoControlador(IServicioDepartamento servicioDepartamento) {
        this.servicioDepartamento = servicioDepartamento;
    }

    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> crear(@RequestBody DepartamentoRequestDTO dto) {
        DepartamentoResponseDTO departamentoCreado = servicioDepartamento.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoCreado);
    }

    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> listar() {
        return ResponseEntity.ok(servicioDepartamento.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioDepartamento.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> actualizar(@PathVariable UUID id,
            @RequestBody DepartamentoRequestDTO dto) {
        return ResponseEntity.ok(servicioDepartamento.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioDepartamento.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
