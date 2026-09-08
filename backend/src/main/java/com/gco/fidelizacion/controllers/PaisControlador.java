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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/paises")
@Tag(name = "Países", description = "Operaciones para consultar y administrar el catálogo de países.")
public class PaisControlador {
    private final IServicioPais servicioPais;

    public PaisControlador(IServicioPais servicioPais) {
        this.servicioPais = servicioPais;
    }

    @Operation(summary = "Crear país", description = "Registra un nuevo país en el catálogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "País creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del país inválidos"),
            @ApiResponse(responseCode = "409", description = "El país ya existe")
    })
    @PostMapping
    public ResponseEntity<PaisResponseDTO> crear(@RequestBody PaisRequestDTO dto) {
        PaisResponseDTO paisCreado = servicioPais.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paisCreado);
    }

    @Operation(summary = "Listar países", description = "Obtiene los países disponibles en el catálogo.")
    @ApiResponse(responseCode = "200", description = "Países obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<PaisResponseDTO>> listar() {
        return ResponseEntity.ok(servicioPais.listar());
    }

    @Operation(summary = "Buscar país por ID", description = "Obtiene un país utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "País encontrado"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioPais.buscarPorId(id));
    }

    @Operation(summary = "Actualizar país", description = "Actualiza la información de un país existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "País actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del país inválidos"),
            @ApiResponse(responseCode = "404", description = "País no encontrado"),
            @ApiResponse(responseCode = "409", description = "El nombre del país ya está registrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PaisResponseDTO> actualizar(@PathVariable UUID id, @RequestBody PaisRequestDTO dto) {
        return ResponseEntity.ok(servicioPais.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar país", description = "Elimina un país del catálogo utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "País eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "País no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioPais.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}