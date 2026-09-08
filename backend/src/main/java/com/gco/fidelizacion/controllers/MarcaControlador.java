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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/marcas")
@Tag(name = "Marcas", description = "Operaciones para consultar y administrar las marcas disponibles en el programa de fidelización.")
public class MarcaControlador {
    private final IServicioMarca servicioMarca;

    public MarcaControlador(IServicioMarca servicioMarca) {
        this.servicioMarca = servicioMarca;
    }

    @Operation(summary = "Crear marca", description = "Registra una nueva marca en el catálogo del programa de fidelización.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Marca creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la marca inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    @PostMapping
    public ResponseEntity<MarcaResponseDTO> crear(@RequestBody MarcaRequestDTO dto) {
        MarcaResponseDTO marcaCreada = servicioMarca.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaCreada);
    }

    @Operation(summary = "Listar marcas", description = "Obtiene las marcas registradas en el catálogo.")
    @ApiResponse(responseCode = "200", description = "Marcas obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<MarcaResponseDTO>> listar() {
        return ResponseEntity.ok(servicioMarca.listar());
    }

    @Operation(summary = "Buscar marca por ID", description = "Obtiene una marca utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca encontrada"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioMarca.buscarPorId(id));
    }

    @Operation(summary = "Actualizar marca", description = "Actualiza la información de una marca existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marca actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la marca inválidos"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una marca con ese nombre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MarcaResponseDTO> actualizar(@PathVariable UUID id, @RequestBody MarcaRequestDTO dto) {
        return ResponseEntity.ok(servicioMarca.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar marca", description = "Elimina una marca utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marca eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Marca no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioMarca.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}