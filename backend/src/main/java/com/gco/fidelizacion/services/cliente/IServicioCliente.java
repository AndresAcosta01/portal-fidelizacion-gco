package com.gco.fidelizacion.services.cliente;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.cliente.ClienteRequestDTO;
import com.gco.fidelizacion.dtos.cliente.ClienteResponseDTO;

public interface IServicioCliente {

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    List<ClienteResponseDTO> listar();

    ClienteResponseDTO buscarPorId(UUID id);

    ClienteResponseDTO buscarPorDocumento(UUID idTipoIdentificacion, String numeroIdentificacion);

    ClienteResponseDTO actualizar(UUID id, ClienteRequestDTO dto);

    void eliminar(UUID id);
}
