package com.gco.fidelizacion.dtos.ciudad;

import java.util.UUID;

import com.gco.fidelizacion.models.Ciudad;

public record CiudadRequestDTO(

        String nombre,
        Boolean activo,
        UUID idDepartamento) {

    public Ciudad toEntity() {

        Ciudad ciudad = new Ciudad();

        ciudad.setNombre(nombre);
        ciudad.setActivo(activo);

        return ciudad;
    }
}
