package com.gco.fidelizacion.validations.marca;

import com.gco.fidelizacion.models.Marca;

public interface IValidacionMarca {

    void validarNombreObligatorio(String nombre);

    void validarNombreNoDuplicado(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarMarca(Marca marca);
}
