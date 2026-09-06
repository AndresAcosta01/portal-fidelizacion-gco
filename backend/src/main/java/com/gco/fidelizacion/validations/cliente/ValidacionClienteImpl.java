package com.gco.fidelizacion.validations.cliente;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.gco.fidelizacion.models.Ciudad;
import com.gco.fidelizacion.models.Cliente;
import com.gco.fidelizacion.models.Marca;
import com.gco.fidelizacion.models.TipoIdentificacion;
import com.gco.fidelizacion.repositories.IClienteRepositorio;

@Component
public class ValidacionClienteImpl implements IValidacionCliente {

    private final IClienteRepositorio repositorioCliente;

    public ValidacionClienteImpl(IClienteRepositorio repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    @Override
    public void validarTipoIdentificacionObligatorio(TipoIdentificacion tipoIdentificacion) {
        if (tipoIdentificacion == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El tipo de identificacion es obligatorio");
        }
    }

    @Override
    public void validarIdentificacionNoDuplicada(TipoIdentificacion tipoIdentificacion, String numeroIdentificacion) {

        Boolean existe = repositorioCliente
                .existsByTipoIdentificacion_IdAndNumeroIdentificacion(tipoIdentificacion.getId(), numeroIdentificacion);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con ese tipo y número de identificación");
        }
    }

    @Override
    public void validarNumeroIdentificacionObligatorio(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El número de identificacion es obligatorio");
        }
    }

    @Override
    public void validarNombresObligatorios(String nombres) {
        if (nombres == null || nombres.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre es obligatorio");
        }
    }

    @Override
    public void validarApellidosObligatorios(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El apellido es obligatorio");
        }
    }

    @Override
    public void validarFechaNacimientoObligatoria(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de nacimiento es obligatoria");
        }
    }

    @Override
    public void validarFechaNacimientoNoFutura(LocalDate fechaNacimiento) {
        if (fechaNacimiento != null && fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de nacimiento no puede ser una fecha futura");
        }
    }

    @Override
    public void validarCiudadObligatoria(Ciudad ciudad) {
        if (ciudad == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La ciudad es obligatoria");
        }
    }

    @Override
    public void validarDireccionObligatoria(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La dirección es obligatoria");
        }
    }

    @Override
    public void validarMarcaObligatoria(Marca marca) {
        if (marca == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La marca es obligatoria");
        }
    }

    @Override
    public void validarIdentificacionNoDuplicadaActualizacion(UUID idCliente, TipoIdentificacion tipoIdentificacion,
            String numeroIdentificacion) {

        Boolean existe = repositorioCliente
                .existsByTipoIdentificacion_IdAndNumeroIdentificacionAndIdNot(tipoIdentificacion.getId(),
                        numeroIdentificacion, idCliente);

        if (Boolean.TRUE.equals(existe)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con ese tipo y número de identificación");
        }
    }

    private void validarCamposBasicos(Cliente cliente) {

        validarTipoIdentificacionObligatorio(cliente.getTipoIdentificacion());
        validarNumeroIdentificacionObligatorio(cliente.getNumeroIdentificacion());
        validarNombresObligatorios(cliente.getNombres());
        validarApellidosObligatorios(cliente.getApellidos());
        validarFechaNacimientoObligatoria(cliente.getFechaNacimiento());
        validarFechaNacimientoNoFutura(cliente.getFechaNacimiento());
        validarCiudadObligatoria(cliente.getCiudad());
        validarDireccionObligatoria(cliente.getDireccion());
        validarMarcaObligatoria(cliente.getMarca());
    }

    @Override
    public void validarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El cliente es obligatorio");
        }

        validarCamposBasicos(cliente);

        validarIdentificacionNoDuplicada(cliente.getTipoIdentificacion(), cliente.getNumeroIdentificacion());
    }

    @Override
    public void validarClienteActualizacion(UUID idCliente, Cliente cliente) {

        if (cliente == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El cliente es obligatorio");
        }

        validarCamposBasicos(cliente);

        validarIdentificacionNoDuplicadaActualizacion(
                idCliente,
                cliente.getTipoIdentificacion(),
                cliente.getNumeroIdentificacion());
    }
}
