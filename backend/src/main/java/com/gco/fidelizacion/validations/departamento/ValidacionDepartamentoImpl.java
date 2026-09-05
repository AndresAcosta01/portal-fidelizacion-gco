package com.gco.fidelizacion.validations.departamento;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.Departamento;
import com.gco.fidelizacion.models.Pais;
import com.gco.fidelizacion.repositories.IDepartamentoRepositorio;

@Component
public class ValidacionDepartamentoImpl implements IValidacionDepartamento {

    private final IDepartamentoRepositorio repositorioDepartamento;

    public ValidacionDepartamentoImpl(IDepartamentoRepositorio repositorioDepartamento) {
        this.repositorioDepartamento = repositorioDepartamento;
    }

    @Override
    public void validarNombreObligatorio(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
    }

    @Override
    public void validarActivoObligatorio(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo activo es obligatorio");
        }
    }

    @Override
    public void validarPaisObligatorio(Pais pais) {
        if (pais == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pais es obligatorio");
        }
    }

    @Override
    public void validarDepartamentoNoDuplicado(Pais pais, String nombre) {

        Boolean existe = repositorioDepartamento.existsByPais_IdAndNombreIgnoreCase(pais.getId(), nombre);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un departamento con ese nombre en el pais seleccionado");
        }
    }

    @Override
    public void validarDepartamento(Departamento departamento) {
        if (departamento == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El departamento es obligatorio");
        }

        validarNombreObligatorio(departamento.getNombre());
        validarActivoObligatorio(departamento.getActivo());
        validarPaisObligatorio(departamento.getPais());
        validarDepartamentoNoDuplicado(departamento.getPais(), departamento.getNombre());
    }
}
