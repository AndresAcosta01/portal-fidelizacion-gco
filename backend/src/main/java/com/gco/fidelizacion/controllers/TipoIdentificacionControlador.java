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

import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionRequestDTO;
import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionResponseDTO;
import com.gco.fidelizacion.services.tipoidentificacion.IServicioTipoIdentificacion;

@RestController
@RequestMapping("/api/tipos-identificacion")
public class TipoIdentificacionControlador {

    private final IServicioTipoIdentificacion servicioTipoIdentificacion;

    public TipoIdentificacionControlador(IServicioTipoIdentificacion servicioTipoIdentificacion) {
        this.servicioTipoIdentificacion = servicioTipoIdentificacion;
    }

    @PostMapping
    public ResponseEntity<TipoIdentificacionResponseDTO> crear(@RequestBody TipoIdentificacionRequestDTO dto) {
        TipoIdentificacionResponseDTO tipoIdentificacionCreada = servicioTipoIdentificacion.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoIdentificacionCreada);
    }

    @GetMapping
    public ResponseEntity<List<TipoIdentificacionResponseDTO>> listar() {
        return ResponseEntity.ok(servicioTipoIdentificacion.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioTipoIdentificacion.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponseDTO> actualizar(@PathVariable UUID id,
            @RequestBody TipoIdentificacionRequestDTO dto) {
        return ResponseEntity.ok(servicioTipoIdentificacion.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioTipoIdentificacion.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
