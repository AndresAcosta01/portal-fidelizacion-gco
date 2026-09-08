package com.gco.fidelizacion.dtos.pais;

import java.util.UUID;

import com.gco.fidelizacion.models.Pais;

public record PaisResponseDTO(

        UUID id,
        String nombre,
        Boolean activo) {

    public static PaisResponseDTO fromEntity(Pais pais) {

        return new PaisResponseDTO(
                pais.getId(),
                pais.getNombre(),
                pais.getActivo());
    }
}
