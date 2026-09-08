package com.gco.fidelizacion.dtos.departamento;

import java.util.UUID;

import com.gco.fidelizacion.models.Departamento;

public record DepartamentoRequestDTO(

        String nombre,
        Boolean activo,
        UUID idPais) {

    public Departamento toEntity() {

        Departamento departamento = new Departamento();

        departamento.setNombre(nombre);
        departamento.setActivo(activo);

        return departamento;
    }
}
