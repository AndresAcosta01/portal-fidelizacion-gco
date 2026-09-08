package com.gco.fidelizacion.validations.tipoidentificacion;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.TipoIdentificacion;
import com.gco.fidelizacion.repositories.ITipoIdentificacionRepositorio;

@Component
public class ValidacionTipoIdentificacionImpl implements IValidacionTipoIdentificacion {

    private final ITipoIdentificacionRepositorio repositorioTipoIdentificacion;

    public ValidacionTipoIdentificacionImpl(ITipoIdentificacionRepositorio repositorioTipoIdentificacion) {
        this.repositorioTipoIdentificacion = repositorioTipoIdentificacion;
    }

    @Override
    public void validarCodigoObligatorio(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El codigo es obligatorio");
        }
    }

    @Override
    public void validarCodigoNoDuplicado(String codigo) {

        Boolean existe = repositorioTipoIdentificacion
                .existsByCodigoIgnoreCase(codigo);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un tipo de identificacion con ese codigo");
        }
    }

    @Override
    public void validarNombreObligatorio(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio");
        }
    }

    @Override
    public void validarActivoObligatorio(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El campo activo es obligatorio");
        }
    }

    @Override
    public void validarCodigoNoDuplicadoActualizacion(String codigo, UUID idTipoIdentificacion) {

        Boolean existe = repositorioTipoIdentificacion
                .existsByCodigoIgnoreCaseAndIdNot(codigo, idTipoIdentificacion);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un tipo de identificación con ese código");
        }
    }

    private void validarCamposBasicos(TipoIdentificacion tipoIdentificacion) {
        validarCodigoObligatorio(tipoIdentificacion.getCodigo());
        validarNombreObligatorio(tipoIdentificacion.getNombre());
        validarActivoObligatorio(tipoIdentificacion.getActivo());
    }

    @Override
    public void validarTipoIdentificacionActualizacion(UUID idTipoIdentificacion,
            TipoIdentificacion tipoIdentificacion) {

        if (tipoIdentificacion == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de identificación es obligatorio");
        }

        validarCamposBasicos(tipoIdentificacion);
        validarCodigoNoDuplicadoActualizacion(tipoIdentificacion.getCodigo(), idTipoIdentificacion);
    }

    @Override
    public void validarTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        if (tipoIdentificacion == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de identificacion es obligatorio");
        }

        validarCamposBasicos(tipoIdentificacion);
        validarCodigoNoDuplicado(tipoIdentificacion.getCodigo());
    }

}
