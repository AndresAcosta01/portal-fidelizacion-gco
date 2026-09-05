package com.gco.fidelizacion.validations.tipoidentificacion;

import com.gco.fidelizacion.models.TipoIdentificacion;

public interface IValidacionTipoIdentificacion {

    void validarCodigoObligatorio(String codigo);

    void validarCodigoNoDuplicado(String codigo);

    void validarNombreObligatorio(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarTipoIdentificacion(TipoIdentificacion tipoIdentificacion);
}
