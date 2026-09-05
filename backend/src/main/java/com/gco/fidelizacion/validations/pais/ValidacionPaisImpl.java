package com.gco.fidelizacion.validations.pais;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.Pais;
import com.gco.fidelizacion.repositories.IPaisRepositorio;

@Component
public class ValidacionPaisImpl implements IValidacionPais {

    private final IPaisRepositorio repositorioPais;

    public ValidacionPaisImpl(IPaisRepositorio repositorioPais) {
        this.repositorioPais = repositorioPais;
    }

    @Override
    public void validarNombreObligatorio(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
    }

    @Override
    public void validarNombreNoDuplicado(String nombre) {

        Boolean existe = repositorioPais.existsByNombreIgnoreCase(nombre);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un pais con ese nombre");
        }
    }

    @Override
    public void validarActivoObligatorio(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo activo es obligatorio");
        }
    }

    @Override
    public void validarPais(Pais pais) {

        if (pais == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El pais es obligatorio");
        }

        validarNombreObligatorio(pais.getNombre());
        validarActivoObligatorio(pais.getActivo());
        validarNombreNoDuplicado(pais.getNombre());
    }

}
