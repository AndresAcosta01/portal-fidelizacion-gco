package com.gco.fidelizacion.validations.tipoidentificacion;

import java.util.UUID;

import com.gco.fidelizacion.models.TipoIdentificacion;

public interface IValidacionTipoIdentificacion {

    void validarCodigoObligatorio(String codigo);

    void validarCodigoNoDuplicado(String codigo);

    void validarNombreObligatorio(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarTipoIdentificacion(TipoIdentificacion tipoIdentificacion);

    void validarCodigoNoDuplicadoActualizacion(String codigo, UUID idTipoIdentificacion);

    void validarTipoIdentificacionActualizacion(UUID idTipoIdentificacion, TipoIdentificacion tipoIdentificacion);
}
