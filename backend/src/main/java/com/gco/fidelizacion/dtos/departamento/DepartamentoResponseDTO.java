package com.gco.fidelizacion.dtos.departamento;

import java.util.UUID;

import com.gco.fidelizacion.models.Departamento;

public record DepartamentoResponseDTO(

        UUID id,
        String nombre,
        Boolean activo,
        UUID idPais) {

    public static DepartamentoResponseDTO fromEntity(Departamento departamento) {

        return new DepartamentoResponseDTO(
                departamento.getId(),
                departamento.getNombre(),
                departamento.getActivo(),
                departamento.getPais().getId());
    }
}
