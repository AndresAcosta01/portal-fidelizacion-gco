package com.gco.fidelizacion.dtos.cliente;

import java.time.LocalDate;
import java.util.UUID;

import com.gco.fidelizacion.models.Cliente;

public record ClienteResponseDTO(

        UUID id,
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String direccion,
        UUID idTipoIdentificacion,
        UUID idCiudad,
        UUID idMarca) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNumeroIdentificacion(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getFechaNacimiento(),
                cliente.getDireccion(),
                cliente.getTipoIdentificacion().getId(),
                cliente.getCiudad().getId(),
                cliente.getMarca().getId());
    }

}
