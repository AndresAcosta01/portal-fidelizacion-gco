package com.gco.fidelizacion.dtos.marca;

import com.gco.fidelizacion.models.Marca;

public record MarcaRequestDTO(

        String nombre,
        String descripcion,
        String logoUrl,
        String sitioWeb,
        Boolean activo) {

    public Marca toEntity() {

        Marca marca = new Marca();

        marca.setNombre(nombre);
        marca.setDescripcion(descripcion);
        marca.setLogoUrl(logoUrl);
        marca.setSitioWeb(sitioWeb);
        marca.setActivo(activo);

        return marca;
    }

}