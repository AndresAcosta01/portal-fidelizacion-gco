package com.gco.fidelizacion.validations.ciudad;

import java.util.UUID;

import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Departamento;

public interface IValidacionCiudad {

    void validarNombreObligatorio(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarDepartamentoObligatorio(Departamento departamento);

    void validarCiudadNoDuplicada(Departamento departamento, String nombre);

    void validarCiudad(Ciudad ciudad);

    void validarCiudadActualizacion(UUID idCiudad , Ciudad ciudad);

    void validarCiudadNoDuplicadaActualizacion(UUID idCiudad, Departamento departamento, String nombre);
}
