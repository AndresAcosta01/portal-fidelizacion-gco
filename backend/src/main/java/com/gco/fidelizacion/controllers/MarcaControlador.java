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

import com.gco.fidelizacion.dtos.marca.MarcaRequestDTO;
import com.gco.fidelizacion.dtos.marca.MarcaResponseDTO;
import com.gco.fidelizacion.services.marca.IServicioMarca;

@RestController
@RequestMapping("/api/marcas")
public class MarcaControlador {

    private final IServicioMarca servicioMarca;

    public MarcaControlador(IServicioMarca servicioMarca) {
        this.servicioMarca = servicioMarca;
    }

    @PostMapping
    public ResponseEntity<MarcaResponseDTO> crear(@RequestBody MarcaRequestDTO dto){
        MarcaResponseDTO marcaCreada = servicioMarca.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaCreada);
    }

    @GetMapping
    public  ResponseEntity<List<MarcaResponseDTO>> listar(){
        return ResponseEntity.ok(servicioMarca.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> buscarPorId(@PathVariable UUID id){
        return ResponseEntity.ok(servicioMarca.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> actualizar(@PathVariable UUID id, @RequestBody MarcaRequestDTO dto){
        return ResponseEntity.ok(servicioMarca.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id){
        servicioMarca.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
