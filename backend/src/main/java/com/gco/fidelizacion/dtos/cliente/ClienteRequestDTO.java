package com.gco.fidelizacion.dtos.cliente;

import java.time.LocalDate;
import java.util.UUID;

import com.gco.fidelizacion.models.Cliente;

public record ClienteRequestDTO(

    String numeroIdentificacion,
    String nombres,
    String apellidos,
    LocalDate fechaNacimiento,
    String direccion,
    UUID idTipoIdentificacion,
    UUID idCiudad,
    UUID idMarca
) {
    public Cliente toEntity(){

        Cliente cliente = new Cliente();

        cliente.setNumeroIdentificacion(numeroIdentificacion);
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setFechaNacimiento(fechaNacimiento);
        cliente.setDireccion(direccion);

        return cliente;
    }
}
