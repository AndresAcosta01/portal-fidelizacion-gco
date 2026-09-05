package com.gco.fidelizacion.validations.ciudad;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Departamento;
import com.gco.fidelizacion.repositories.ICiudadRepositorio;

@Component
public class ValidacionCiudadImpl implements IValidacionCiudad {

    private final ICiudadRepositorio repositorioCiudad;

    public ValidacionCiudadImpl(ICiudadRepositorio repositorioCiudad) {
        this.repositorioCiudad = repositorioCiudad;
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
    public void validarDepartamentoObligatorio(Departamento departamento) {
        if (departamento == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El departamento es obligatorio");
        }
    }

    @Override
    public void validarCiudadNoDuplicada(Departamento departamento, String nombre) {

        Boolean existe = repositorioCiudad
                .existsByDepartamento_IdAndNombreIgnoreCase(departamento.getId(), nombre);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe la ciudad con ese nombre en el departamento seleccionado");
        }
    }

    @Override
    public void validarCiudad(Ciudad ciudad) {
        if (ciudad == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La ciudad es obligatoria");
        }

        validarNombreObligatorio(ciudad.getNombre());
        validarActivoObligatorio(ciudad.getActivo());
        validarDepartamentoObligatorio(ciudad.getDepartamento());
        validarCiudadNoDuplicada(ciudad.getDepartamento(), ciudad.getNombre());
    }

}
