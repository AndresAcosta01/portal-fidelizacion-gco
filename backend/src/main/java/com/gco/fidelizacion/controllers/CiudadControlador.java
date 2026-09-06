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

import com.gco.fidelizacion.dtos.ciudad.CiudadRequestDTO;
import com.gco.fidelizacion.dtos.ciudad.CiudadResponseDTO;
import com.gco.fidelizacion.services.ciudad.IServicioCiudad;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadControlador {

    private final IServicioCiudad servicioCiudad;

    public CiudadControlador(IServicioCiudad servicioCiudad) {
        this.servicioCiudad = servicioCiudad;
    }

    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(@RequestBody CiudadRequestDTO dto) {
        CiudadResponseDTO ciudadCreada = servicioCiudad.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadCreada);
    }

    @GetMapping
    public ResponseEntity<List<CiudadResponseDTO>> listar() {
        return ResponseEntity.ok(servicioCiudad.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioCiudad.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> actualizar(@PathVariable UUID id, @RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(servicioCiudad.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioCiudad.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
