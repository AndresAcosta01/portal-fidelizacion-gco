package com.gco.fidelizacion.dtos.pais;

import com.gco.fidelizacion.models.Pais;

public record PaisRequestDTO(

        String nombre,
        Boolean activo) {

    public Pais toEntity() {

        Pais pais = new Pais();

        pais.setNombre(nombre);
        pais.setActivo(activo);

        return pais;
    }
}
