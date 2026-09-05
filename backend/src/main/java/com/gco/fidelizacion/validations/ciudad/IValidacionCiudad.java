package com.gco.fidelizacion.validations.ciudad;

import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Departamento;

public interface IValidacionCiudad {

    void validarNombreObligatorio(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarDepartamentoObligatorio(Departamento departamento);

    void validarCiudadNoDuplicada(Departamento departamento, String nombre);

    void validarCiudad(Ciudad ciudad);
}
