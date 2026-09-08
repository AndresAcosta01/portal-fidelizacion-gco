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

import com.gco.fidelizacion.dtos.cliente.ClienteRequestDTO;
import com.gco.fidelizacion.dtos.cliente.ClienteResponseDTO;
import com.gco.fidelizacion.services.cliente.IServicioCliente;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Operaciones para registrar, consultar, actualizar y eliminar clientes del programa de fidelización.")
public class ClienteControlador {
    private final IServicioCliente servicioCliente;

    public ClienteControlador(IServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    @Operation(summary = "Registrar cliente", description = "Registra un cliente en el programa de fidelización relacionándolo con un tipo de identificación, una ciudad y una marca.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del cliente inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de identificación, ciudad o marca no encontrados"),
            @ApiResponse(responseCode = "409", description = "Ya existe un cliente con el mismo tipo y número de identificación")
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO clienteCreado = servicioCliente.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados en el programa de fidelización.")
    @ApiResponse(responseCode = "200", description = "Clientes obtenidos correctamente")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(servicioCliente.listar());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Obtiene un cliente utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioCliente.buscarPorId(id));
    }

    @Operation(summary = "Buscar cliente por documento", description = "Obtiene un cliente mediante la combinación de tipo de identificación y número de identificación.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/documento")
    public ResponseEntity<ClienteResponseDTO> buscarPorDocumento(
            @Parameter(description = "UUID del tipo de identificación", example = "550e8400-e29b-41d4-a716-446655440000") @RequestParam UUID idTipoIdentificacion,
            @Parameter(description = "Número de identificación del cliente", example = "1032456789") @RequestParam String numeroIdentificacion) {
        return ResponseEntity.ok(servicioCliente.buscarPorDocumento(idTipoIdentificacion, numeroIdentificacion));
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos personales y las relaciones de un cliente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del cliente inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente, tipo de identificación, ciudad o marca no encontrados"),
            @ApiResponse(responseCode = "409", description = "Ya existe otro cliente con el mismo tipo y número de identificación")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable UUID id, @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(servicioCliente.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente utilizando su identificador UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioCliente.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}