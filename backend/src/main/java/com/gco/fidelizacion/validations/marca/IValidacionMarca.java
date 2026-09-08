package com.gco.fidelizacion.validations.marca;

import java.util.UUID;

import com.gco.fidelizacion.models.Marca;

public interface IValidacionMarca {

    Object validarClienteActualizacion = null;

    void validarNombreObligatorio(String nombre);

    void validarNombreNoDuplicado(String nombre);

    void validarActivoObligatorio(Boolean activo);

    void validarMarca(Marca marca);

    void validarNombreNoDuplicadoActualizacion(String nombre, UUID idMarca);

    void validarMarcaActualizacion(UUID idMarca, Marca marca);
}
