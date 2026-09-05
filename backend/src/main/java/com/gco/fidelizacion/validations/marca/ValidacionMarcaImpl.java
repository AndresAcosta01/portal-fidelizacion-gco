package com.gco.fidelizacion.validations.marca;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.Marca;
import com.gco.fidelizacion.repositories.IMarcaRepositorio;

@Component
public class ValidacionMarcaImpl implements IValidacionMarca{

    private final IMarcaRepositorio repositorioMarca;

    public ValidacionMarcaImpl(IMarcaRepositorio repositorioMarca){
        this.repositorioMarca = repositorioMarca;
    }

    @Override
    public void validarNombreObligatorio(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }
    }

    @Override
    public void validarNombreNoDuplicado(String nombre) {

        Boolean existe = repositorioMarca.existsByNombreIgnoreCase(nombre);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una marca con ese nombre");
        }
    }

    @Override
    public void validarActivoObligatorio(Boolean activo) {
        if (activo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El campo activo es obligatorio");
        }
    }

    @Override
    public void validarMarca(Marca marca) {
        if (marca == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La marca es obligatorio");
        }
        validarNombreObligatorio(marca.getNombre());
        validarActivoObligatorio(marca.getActivo());
        validarNombreNoDuplicado(marca.getNombre());
    }

}
