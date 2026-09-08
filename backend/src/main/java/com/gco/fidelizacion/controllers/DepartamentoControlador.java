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

import com.gco.fidelizacion.dtos.departamento.DepartamentoRequestDTO;
import com.gco.fidelizacion.dtos.departamento.DepartamentoResponseDTO;
import com.gco.fidelizacion.services.departamento.IServicioDepartamento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/departamentos")
@Tag(name = "Departamentos", description = "Operaciones para consultar y administrar departamentos, estados o divisiones administrativas asociadas a un país.")
public class DepartamentoControlador {
    private final IServicioDepartamento servicioDepartamento;

    public DepartamentoControlador(IServicioDepartamento servicioDepartamento) {
        this.servicioDepartamento = servicioDepartamento;
    }

    @Operation(summary = "Crear departamento", description = "Registra un departamento asociado a un país existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Departamento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del departamento inválidos"),
            @ApiResponse(responseCode = "404", description = "País no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un departamento con ese nombre en el país")
    })
    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> crear(@RequestBody DepartamentoRequestDTO dto) {
        DepartamentoResponseDTO departamentoCreado = servicioDepartamento.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(departamentoCreado);
    }

    @Operation(summary = "Listar departamentos", description = "Obtiene todos los departamentos. Si se envía idPais, devuelve únicamente los departamentos activos asociados a ese país.")
    @ApiResponse(responseCode = "200", description = "Departamentos obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> listar(
            @Parameter(description = "UUID del país utilizado para filtrar los departamentos", example = "550e8400-e29b-41d4-a716-446655440000") @RequestParam(required = false) UUID idPais) {
        if (idPais != null) {
            return ResponseEntity.ok(servicioDepartamento.listarPorPais(idPais));
        }
        return ResponseEntity.ok(servicioDepartamento.listar());
    }

    @Operation(summary = "Buscar departamento por ID", description = "Obtiene un departamento utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioDepartamento.buscarPorId(id));
    }

    @Operation(summary = "Actualizar departamento", description = "Actualiza la información de un departamento y su relación con un país.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departamento actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del departamento inválidos"),
            @ApiResponse(responseCode = "404", description = "Departamento o país no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un departamento con ese nombre en el país")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> actualizar(@PathVariable UUID id,
            @RequestBody DepartamentoRequestDTO dto) {
        return ResponseEntity.ok(servicioDepartamento.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar departamento", description = "Elimina un departamento utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Departamento eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Departamento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioDepartamento.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}