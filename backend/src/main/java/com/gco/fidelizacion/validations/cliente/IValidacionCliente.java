package com.gco.fidelizacion.validations.cliente;

import java.time.LocalDate;

import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Cliente;
import com.gco.fidelizacion.models.Marca;
import com.gco.fidelizacion.models.TipoIdentificacion;

public interface IValidacionCliente {

    void validarTipoIdentificacionObligatorio(TipoIdentificacion tipoIdentificacion);

    void validarIdentificacionNoDuplicada(TipoIdentificacion tipoIdentificacion, String numeroIdentificacion);

    void validarNumeroIdentificacionObligatorio(String numeroIdentificacion);

    void validarNombresObligatorios(String nombres);

    void validarApellidosObligatorios(String apellidos);

    void validarFechaNacimientoObligatoria(LocalDate fechaNacimiento);

    void validarFechaNacimientoNoFutura(LocalDate fechaNacimiento);

    void validarCiudadObligatoria(Ciudad ciudad);

    void validarDireccionObligatoria(String direccion);

    void validarMarcaObligatoria(Marca marca);

    void validarCliente(Cliente cliente);
}
