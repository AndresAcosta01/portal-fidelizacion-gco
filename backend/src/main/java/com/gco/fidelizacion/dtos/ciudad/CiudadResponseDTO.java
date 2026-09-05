package com.gco.fidelizacion.dtos.ciudad;

import java.util.UUID;

import com.gco.fidelizacion.models.Ciudad;

public record CiudadResponseDTO(

        UUID id,
        String nombre,
        Boolean activo,
        UUID idDepartamento) {

    public static CiudadResponseDTO fromEntity(Ciudad ciudad) {

        return new CiudadResponseDTO(
                ciudad.getId(),
                ciudad.getNombre(),
                ciudad.getActivo(),
                ciudad.getDepartamento().getId());
    }
}
