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

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {

    private final IServicioCliente servicioCliente;

    public ClienteControlador(IServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO clienteCreado = servicioCliente.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(servicioCliente.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(servicioCliente.buscarPorId(id));
    }

    @GetMapping("/documento")
    public ResponseEntity<ClienteResponseDTO> buscarPorDocumento(@RequestParam UUID idTipoIdentificacion,
            @RequestParam String numeroIdentificacion) {
        return ResponseEntity.ok(servicioCliente.buscarPorDocumento(idTipoIdentificacion, numeroIdentificacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable UUID id, @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(servicioCliente.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        servicioCliente.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
