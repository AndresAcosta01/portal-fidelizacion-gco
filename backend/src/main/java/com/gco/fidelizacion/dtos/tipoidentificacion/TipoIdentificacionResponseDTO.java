package com.gco.fidelizacion.dtos.tipoidentificacion;

import java.util.UUID;

import com.gco.fidelizacion.models.TipoIdentificacion;

public record TipoIdentificacionResponseDTO(

        UUID id,
        String codigo,
        String nombre,
        Boolean activo) {

    public static TipoIdentificacionResponseDTO fromEntity(TipoIdentificacion tipoIdentificacion) {

        return new TipoIdentificacionResponseDTO(
                tipoIdentificacion.getId(),
                tipoIdentificacion.getCodigo(),
                tipoIdentificacion.getNombre(),
                tipoIdentificacion.getActivo());
    }
}
