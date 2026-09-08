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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gco.fidelizacion.dtos.ciudad.CiudadRequestDTO;
import com.gco.fidelizacion.dtos.ciudad.CiudadResponseDTO;
import com.gco.fidelizacion.services.ciudad.IServicioCiudad;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ciudades")
@Tag(name = "Ciudades", description = "Operaciones para consultar y administrar ciudades asociadas a un departamento.")
public class CiudadControlador {
    private final IServicioCiudad servicioCiudad;

    public CiudadControlador(IServicioCiudad servicioCiudad) {
        this.servicioCiudad = servicioCiudad;
    }

    @Operation(summary = "Crear ciudad", description = "Registra una nueva ciudad asociada a un departamento existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ciudad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la ciudad inválidos"),
            @ApiResponse(responseCode = "404", description = "Departamento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ciudad con ese nombre en el departamento")
    })
    @PostMapping
    public ResponseEntity<CiudadResponseDTO> crear(@RequestBody CiudadRequestDTO dto) {
        CiudadResponseDTO ciudadCreada = servicioCiudad.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadCreada);
    }

    @Operation(summary = "Listar ciudades", description = "Obtiene todas las ciudades. Si se envía idDepartamento, devuelve únicamente las ciudades activas asociadas a ese departamento.")
    @ApiResponse(responseCode = "200", description = "Ciudades obtenidas correctamente")
    @GetMapping
    public ResponseEntity<List<CiudadResponseDTO>> listar(
            @Parameter(description = "UUID del departamento utilizado para filtrar las ciudades", example = "550e8400-e29b-41d4-a716-446655440000") @RequestParam(required = false) UUID idDepartamento) {
        if (idDepartamento != null) {
            return ResponseEntity.ok(servicioCiudad.listarPorDepartamento(idDepartamento));
        }
        return ResponseEntity.ok(servicioCiudad.listar());
    }

    @Operation(summary = "Buscar ciudad por ID", description = "Obtiene una ciudad utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad encontrada"),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioCiudad.buscarPorId(id));
    }

    @Operation(summary = "Actualizar ciudad", description = "Actualiza la información de una ciudad y su relación con un departamento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ciudad actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la ciudad inválidos"),
            @ApiResponse(responseCode = "404", description = "Ciudad o departamento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ciudad con ese nombre en el departamento")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CiudadResponseDTO> actualizar(@PathVariable UUID id, @RequestBody CiudadRequestDTO dto) {
        return ResponseEntity.ok(servicioCiudad.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar ciudad", description = "Elimina una ciudad utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ciudad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioCiudad.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}