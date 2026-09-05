package com.gco.fidelizacion.validations.departamento;

import com.gco.fidelizacion.models.Departamento;
import com.gco.fidelizacion.models.Pais;

public interface IValidacionDepartamento {

    void validarNombreObligatorio(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarPaisObligatorio(Pais pais);

    void validarDepartamentoNoDuplicado(Pais pais, String nombre);

    void validarDepartamento(Departamento departamento);
}
