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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tipos-identificacion")
@Tag(name = "Tipos de identificación", description = "Operaciones para consultar y administrar los tipos de identificación disponibles para el registro de clientes.")
public class TipoIdentificacionControlador {
    private final IServicioTipoIdentificacion servicioTipoIdentificacion;

    public TipoIdentificacionControlador(IServicioTipoIdentificacion servicioTipoIdentificacion) {
        this.servicioTipoIdentificacion = servicioTipoIdentificacion;
    }

    @Operation(summary = "Crear tipo de identificación", description = "Registra un nuevo tipo de identificación en el catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de identificación creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del tipo de identificación inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un tipo de identificación con ese código")
    })
    @PostMapping
    public ResponseEntity<TipoIdentificacionResponseDTO> crear(@RequestBody TipoIdentificacionRequestDTO dto) {
        TipoIdentificacionResponseDTO tipoIdentificacionCreada = servicioTipoIdentificacion.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoIdentificacionCreada);
    }

    @Operation(summary = "Listar tipos de identificación", description = "Obtiene los tipos de identificación registrados en el catálogo.")
    @ApiResponse(responseCode = "200", description = "Tipos de identificación obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<TipoIdentificacionResponseDTO>> listar() {
        return ResponseEntity.ok(servicioTipoIdentificacion.listar());
    }

    @Operation(summary = "Buscar tipo de identificación por ID", description = "Obtiene un tipo de identificación utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de identificación encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de identificación no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioTipoIdentificacion.buscarPorId(id));
    }

    @Operation(summary = "Actualizar tipo de identificación", description = "Actualiza la información de un tipo de identificación existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de identificación actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del tipo de identificación inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de identificación no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un tipo de identificación con ese código")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoIdentificacionResponseDTO> actualizar(@PathVariable UUID id,
            @RequestBody TipoIdentificacionRequestDTO dto) {
        return ResponseEntity.ok(servicioTipoIdentificacion.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar tipo de identificación", description = "Elimina un tipo de identificación utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de identificación eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de identificación no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioTipoIdentificacion.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}