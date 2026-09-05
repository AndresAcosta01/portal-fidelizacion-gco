package com.gco.fidelizacion.dtos.tipoidentificacion;

import com.gco.fidelizacion.models.TipoIdentificacion;

public record TipoIdentificacionRequestDTO(

        String codigo,
        String nombre,
        Boolean activo) {

    public TipoIdentificacion toEntity() {

        TipoIdentificacion tipoIdentificacion = new TipoIdentificacion();

        tipoIdentificacion.setCodigo(codigo);
        tipoIdentificacion.setNombre(nombre);
        tipoIdentificacion.setActivo(activo);

        return tipoIdentificacion;
    }
}
