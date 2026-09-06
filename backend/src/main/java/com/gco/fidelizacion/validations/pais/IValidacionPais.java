package com.gco.fidelizacion.validations.pais;

import java.util.UUID;

import com.gco.fidelizacion.models.Pais;

public interface IValidacionPais {

    void validarNombreObligatorio(String nombre);

    void validarNombreNoDuplicado(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarPais(Pais pais);

    void validarNombreNoDuplicadoActualizacion(String nombre, UUID idPais);

    void validarPaisActualizacion(UUID idPais, Pais pais);
}
