package com.gco.fidelizacion.services.tipoidentificacion;

import java.util.List;
import java.util.UUID;

import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionRequestDTO;
import com.gco.fidelizacion.dtos.tipoidentificacion.TipoIdentificacionResponseDTO;

public interface IServicioTipoIdentificacion {

    TipoIdentificacionResponseDTO crear(TipoIdentificacionRequestDTO dto);

    List<TipoIdentificacionResponseDTO> listar();

    TipoIdentificacionResponseDTO buscarPorId(UUID id);

    TipoIdentificacionResponseDTO actualizar(UUID id, TipoIdentificacionRequestDTO dto);

    void eliminar(UUID id);
}
