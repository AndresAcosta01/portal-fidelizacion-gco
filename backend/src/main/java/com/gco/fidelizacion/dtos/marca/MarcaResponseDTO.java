package com.gco.fidelizacion.dtos.marca;

import java.util.UUID;

import com.gco.fidelizacion.models.Marca;

public record MarcaResponseDTO(
        UUID id,
        String nombre,
        String descripcion,
        String logoUrl,
        String sitioWeb,
        Boolean activo) {

    public static MarcaResponseDTO fromEntity(Marca marca) {

        return new MarcaResponseDTO(
                marca.getId(),
                marca.getNombre(),
                marca.getDescripcion(),
                marca.getLogoUrl(),
                marca.getSitioWeb(),
                marca.getActivo());
    }
}
